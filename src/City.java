package src;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import src.weather.OpenWeatherMap;
import src.wikipedia.MediaWiki;
/**
 * Creates a {@code City} object to with all the necessary tools for the travel agency
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 21-10-2021*/
public class City {    
    private static final String[] features = new String[]{"cafe", "sea", "museum", "restaurant", "stadium", "culture", "transport", "temp", "clouds", "dist"};
    private static final String[] features_plural = new String[]{"cafes", "seas", "museums", "restaurants", "stadiums", "cultures", "transports"};
    private static final double maxdist = geodesic_distance(new double[]{0, 0}, new double[]{0, 180}); //which is about ~= max_earth's_circumference/2

    public static HashMap<String, City> Cities = new HashMap<String, City>();
    private static Map<String, ArrayList<String>> registeredDay = new HashMap<String, ArrayList<String>>();
    private double[] norm_data;
    private double[] coords;
    private String name;
    private String countryInitials;
    private Boolean letterCase = null;
    private Long timestamp;
    private Boolean isDuplicate = false;
        /* ------------
        ?  Constructors
           ------------*/
    /**Main Constructor
     *@param name Is the name of the given city
     *@param countryInitials Are the initials of the country that the city is located*/
    City(String name, String countryInitials) {
        try {
            this.timestamp = (Long) new Date().getTime(); //remove millis
            this.name = name;
            this.countryInitials = countryInitials;
            City city = getCity(name, countryInitials);
            if(city != null) {
                System.out.println("Date registered: " + new Date(city.timestamp).toString() + "\n");
                isDuplicate = true;
                return;
            }
            extractData(name, countryInitials);
            addCity();
            setRegisteredDay();
        } catch (IOException e) {
            System.out.println("Invalid city. Try giving a correct name and country initials\n");
        }
    }
    /**Constructor for {@code json} extracted data
     *@param name Is the name of the given city
     *@param countryInitials Are the initials of the country that the city is located
     *@param coords Geographical coordinates of the city
     *@param normData Normalized data for the recommendations
     *@param timestamp Unix representation of the date the city was registered*/
    City(String name, String countryInitials, double[] coords, double[] normData, Long timestamp){
        this.name = name;
        this.countryInitials = countryInitials;
        this.coords = coords;
        this.norm_data = normData;
        this.timestamp = timestamp;
        addCity();
        setRegisteredDay();
    }
        /* ---------------
        ?  Getters/Setters
           --------------*/
    /**Getter
     *@return Returns the normalized data of the city's features*/
    public double[] getNormData(){
        return this.norm_data;
    }

    /**Getter
     *@return Returns the geographical coordinates of the city*/
    public double[] getCoords(){
        return this.coords;
    }

    /**Getter
     *@return Returns the name of the city with upper/lower case if specified */
    public String getName(){
        return this.letterCase == null ? this.name : (this.letterCase == true ? this.name.toUpperCase() : this.name.toLowerCase());
    }

    /**Getter
     *@return Returns the name of the city with upper/lower case if specified */ 
    public String getCountryInitials(){
        return this.countryInitials;
    }

    /**Getter
     *@return Returns the timestamp of when the city was registered*/
    public Long getTimestamp() {
        return this.timestamp;
    }
    
    /**Getter
     *@return Returns the maximum possible geodesic distance */
    public static double getMaxDist(){
        return City.maxdist;
    }

    /**Getter
     *@return Returns  */
    public Boolean getIsDuplicate() {
        return this.isDuplicate;
    }

    /**Sets the preference of the letter casing
     *@param bool The given preference*/
    public void setLetterCase(Boolean bool){
        this.letterCase = bool;
    }

    /**Adds name of the registrated city to the appropriate week day */
    private void setRegisteredDay(){
        registeredDay.get(new SimpleDateFormat("E").format(new Date(this.timestamp))).add(this.name);
    }
    
    /**Adds the current city to the {@code Cities} hashmap*/
    private void addCity(){
        Cities.put(this.name+"_"+this.countryInitials, this);
    }

        /* --------------------
        !  Duplication checking
           --------------------*/
    /** Checks if 2 cities have the same name
     *@param city The city to check equality with
     *@return A boolean of the equality result*/
    @Override
    public boolean equals(Object city){
        City c = (City) city;
        boolean res = this.name.equalsIgnoreCase(c.name);
        return res;
    }

    /**
     *@param name The city to check equality with
     *@param countryInitials The city to check equality with
     *@return Returns the city or null if it doesn't exist or if it's duplicate*/
    public City getCity(String name, String countryInitials){
        City city = Cities.get(name+"_"+countryInitials);
        if(city != null && this.equals(city) == false) return null;
        return city;
    }

    /**Search city by name and initials for global use 
     *@param name The city to check equality with
     *@param countryInitials The city to check equality with
     *@return Returns the city or null if it doesn't exist */
    public static City findCity(String name, String countryInitials) {
        return Cities.get(name+"_"+countryInitials);
    }

        /* -------------------------------
        !  Data Collection & Normalization
           -------------------------------*/
    /**
     * ! Calls the Wiki and OpenWeather apis and stores the data to the appropriate variables
     *@param name Is the name of the given city
     *@param countryInitials Are the initials of the country that the city is located
     *@exception IOException */
    public void extractData(String city_name, String country_initials) throws IOException{
        OpenWeatherMap weather = OpenData.RetrieveWeatherData(city_name, country_initials);
        MediaWiki wiki =  OpenData.RetrieveWikiData(city_name);

        double[][] data = new double[10][3];
        int[] featuresCount = getWords(wiki.getQuery().getPages().get(0).getExtract());
        for(int i = 0; i < 7; i++){ //Setting the feature's data:[value, max, min]
            data[i][0] = featuresCount[i] >= 10 ? 10 : featuresCount[i];
            data[i][1] = 10;
            data[i][2] = 0;
        }

        data[7] = new double[]{weather.getMain().getTemp(), weather.getMain().getTempMax(), weather.getMain().getTempMin()};
        data[8] = new double[]{weather.getClouds().getAll(), 100, 0};
        data[9] = new double[]{0, 1, 0}; //Defaults 0 for now until the user asks for recommendations

            //Normalize the data and create the new city
        this.norm_data = normalizeData(data);
        this.coords = new double[]{weather.getCoord().getLat(), weather.getCoord().getLon()};
    }
    /**
     * Normalizes the data extracted from the apis
     *@param data A {@code double[][]} that contains [features][value, min, max]
     *@return A {@code double[]} values of the normalized data*/
    private static double[] normalizeData(double[][] data){
        double[] normalized_values = new double[10];
        for(int i = 0; i < 10; i++){
            double[] temp = data[i];
            normalized_values[i] = (temp[0] - temp[2]) 
                                    / (temp[1] - temp[2]);
        }
        return normalized_values;
    }

    /**
     * Finds how many times a features is seen in a wiki article
     *@param article is the text of the wiki article  
     *@return The an {@code int[]} of the times a feature is seen in a wiki page */
    private static int[] getWords(String article){//Didn't manage to do it with regex 😢
        int[] word_count = new int[7];
        String[] words =  article.split("\\s+");
        for(int i = 0; i < 7; i++){
            int s = 0;
            for(int j = 0; j < words.length; j++){
                s += (words[j].equals(features[i]) || words[j].equals(features_plural[i])) ? 1 : 0;
            }
            word_count[i] = s;
        }
        return word_count;
    }

        /* ------------------
        !  Distance Functions
           ------------------*/
    /**
     * Calculates the geodesic distance between 2 cities
     *@param c1 The first city
     *@param c2 The second city
     *@return The geodesic distance between 2 cities*/
    public static double geodesic_distance(double[] c1, double[] c2){
        double lat1 = c1[0];
        double lon1 = c1[1];
        double lat2 = c2[0];
        double lon2 = c2[1];

        if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + 
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			return dist;
		}
    }

    /**
     * Sets the normalized geodesic distance to the normalized data array
     *@param dist is the calculated geodesic distance between 2 cities */
    private void setNormGeodesicDist(double dist){
        this.norm_data[9] = dist / maxdist; //normalizing the distance
    }

    /**
     * Calculates the geodesic distance between 2 cities based on their coordinates
     *@param user_city is the city to compare the with
     *@return itself to make it chainable*/
    public City compareDistance(City user_city){
        double[] city1_coords = user_city.getCoords();
        double[] city2_coords = this.coords;
        this.setNormGeodesicDist(geodesic_distance(city1_coords, city2_coords));
        return this;
    }
        /* ----------------
        !  Handle Json data
           ----------------*/
    /**
     * Saves all the registered cities from the cities hashmap to a json file  
     *@exception JsonGenerationException
     *@exception JsonMappingException
     *@exception IOException */
    public static void writeJSON() throws JsonGenerationException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("Cities.json"), Cities);
    }

    /**
     * Reads the json file that contains the cities data  and creates them
     * if the the json file doesn't exist or if it's empty, it creates some dummies {@code loadDummies()}
     *@exception JsonGenerationException
     *@exception JsonMappingException
     *@exception IOException */
    @SuppressWarnings("unchecked")
    public static void readJSON() throws JsonParseException, JsonMappingException, IOException{
        //* https://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
        ObjectMapper mapper = new ObjectMapper();
        for(int i = 0; i < 7; i++){//!Initialization of registeredDay
            //milliseconds in a day * day
            registeredDay.put(new SimpleDateFormat("E").format(new Date(86400000 * i)), new ArrayList<String>());
        }
        if(new File("Cities.json").length() == 0) { loadDummies(); return; } //Check if file is empty
        HashMap<String, HashMap<String, Object>> jsonCities = mapper.readValue(new File("Cities.json"), HashMap.class);
        if(jsonCities.size() == 0) { loadDummies(); return; } //Check if they are no registered cities

        for(HashMap.Entry<String, HashMap<String, Object>> entry : jsonCities.entrySet()){ //Iterate and add the cities
            HashMap<String, Object> city_obj = entry.getValue();

            String name = String.valueOf(city_obj.get("name"));
            String countryInitials = String.valueOf(city_obj.get("countryInitials"));
            double[] coords = ((ArrayList<Double>) city_obj.get("coords")).stream().mapToDouble(d->d).toArray(); 
            double[] normData = ((ArrayList<Double>) city_obj.get("normData")).stream().mapToDouble(d->d).toArray();
            Long timestamp = (Long) city_obj.get("timestamp");

            new City(name, countryInitials, coords, normData, timestamp);
        }
    }

    /**
     * Creates dummy cities if none exist in the json file */
    private static void loadDummies(){
        System.out.println("Loading dummies...");
        new City("Athens", "gr");
        new City("London", "uk");
        new City("Tokyo", "jp");
        new City("California", "us");
        return;
    }
}