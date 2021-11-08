package src;

import java.io.IOException;
import java.util.ArrayList;
import src.weather.OpenWeatherMap;
import src.wikipedia.MediaWiki;

public class City {
    private static final String[] features = new String[]{"cafe", "sea", "museum", "restaurant", "stadium", "culture", "transport", "temp", "clouds", "dist"};
    private static final String[] features_plural = new String[]{"cafes", "seas", "museums", "restaurants", "stadiums", "cultures", "transports"};
    private static final double maxdist = geodesic_distance(new double[]{0, 0}, new double[]{0, 180});
    
    public static ArrayList<City> Cities = new ArrayList<City>();
    private double[] norm_data;
    private double[] coords;
    private String name;
    private Boolean letterCase = null;
        /*------------
          Constructors
          ------------*/
    City(double[] norm_data, double[] coords, String name){
        this.norm_data = norm_data;
        this.coords = coords;
        this.name = name;
    }

    City(double[] coords){//When the user gives his location
        this.coords = coords;
    }
        /*---------------
          Getters/Setters
          --------------*/
    public double[] getNormData(){
        return this.norm_data;
    }

    public double[] getCoords(){
        return this.coords;
    }

    public String getName(){
        return this.letterCase == null ? this.name : (this.letterCase == true ? this.name.toUpperCase() : this.name.toLowerCase());
    }

    public void setLetterCase(Boolean bool){
        this.letterCase = bool;
    }

    public static double getMaxDist(){
        return City.maxdist;
    }
        /*-------------------------------
          Data Collection & Normalization
          -------------------------------*/
    public static void extractData(OpenWeatherMap weather, MediaWiki wiki, String city_name)throws IOException{
        double[][] data = new double[10][3];
        double[] coords = new double[]{weather.getCoord().getLat(), weather.getCoord().getLon()};
        int[] featuresCount = getWords(wiki.getQuery().getPages().get(0).getExtract());
        for(int i = 0; i < 7; i++){ //Setting the feature's data:[value, max, min]
            data[i][0] = featuresCount[i] > 10 ? 10 : featuresCount[i];
            data[i][1] = 10;
            data[i][2] = 0;
        }

        data[7] = new double[]{weather.getMain().getTemp(), weather.getMain().getTempMax(), weather.getMain().getTempMin()};
        data[8] = new double[]{weather.getClouds().getAll(), 100, 0};
        data[9] = new double[]{0, 1, 0}; //Defaults 0 for now until the user give a location

        double[] normalized_data = normalizeData(data);
        Cities.add(new City(normalized_data, coords, city_name));
    }
        
    private static double[] normalizeData(double[][] data){
        double[] normalized_values = new double[10];
        for(int i = 0; i < 10; i++){
            double[] temp = data[i];
            normalized_values[i] = (temp[0] - temp[2]) 
                                    / (temp[1] - temp[2]);
        }
        return normalized_values;
    }

    private static int[] getWords(String article){//Didn't manage to do it with regex :(
        int[] word_count = new int[7];
        String[] words =  article.split("\\w+");
        for(int i = 0; i < 7; i++){
            int s = 0;
            for(int j = 0; j < words.length; j++){
                s += (words[j].equals(features[i]) || words[j].equals(features_plural[i])) ? 1 : 0;
            }
            word_count[i] = s; 
        }
        return word_count;
    }
        /*------------------
          Distance Functions
          ------------------*/
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
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			return dist;
		}
    }

    private void setGeodesicDist(double dist){
        this.norm_data[9] = dist / maxdist; //normalizing the distance
    }

    public City compareDistance(City user_city){
        double[] city1_coords = user_city.getCoords();
        double[] city2_coords = this.coords;
        this.setGeodesicDist(geodesic_distance(city1_coords, city2_coords));
        return this;
    }

    public void printData(){
        System.out.println(this.name + " data: ");
        for(int i = 0; i < 10; ){
            System.out.println("\t" + features[i] + ": " + this.norm_data[i++]);
        }
    }
}

