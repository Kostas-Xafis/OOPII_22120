package src;

import java.io.IOException;
import java.util.ArrayList;

import src.Perceptrons.PerceptronElderTraveller;
import src.Perceptrons.PerceptronMiddleTraveller;
import src.Perceptrons.PerceptronTraveller;
import src.Perceptrons.PerceptronYoungTraveller;
import src.weather.OpenWeatherMap;

public class UI {
    //Temporary class for testing purposes
    public static void init(){
        boolean loop = true;
        while(loop){
            int ans = Scan.scanInt("Give one of the following: \n" +
            "-1 To add a city\n" +
            "-2 To get a recommandation\n" +
            "-3 To exit\n-", true);
            switch(ans){
                case 1:{
                    add_city();
                    break;
                }
                case 2:{
                    getRecommendations();
                    break;
                }
                case 3:{
                    loop = false;
                    break;
                }
                default:{
                    System.out.println("Incorrect input");
                }
            }
        }
    }

    private static void add_city() { //This function is for testing purposes
        String[] city_data = getCityData(false);
        OpenData.RetrieveData(city_data[0], city_data[1]);
    }

    private static void getRecommendations(){
        try{
            int age;
            PerceptronTraveller perc = null;
            ArrayList<City> recommendations;
            
                //Get user's age
            while(true){
                age = Scan.scanInt("Specify your age: ", true);
                if(age >= 16 && age <= 115) break;
                System.out.println("You need to be over 16 and under 115 yo to apply.");
            }  

                //Get user's city coords
            String[] user_city_data = getCityData(true);
            OpenWeatherMap weather =  OpenData.RetrieveWeatherData(user_city_data[0], user_city_data[1]);
            City user_city = new City(new double[]{weather.getCoord().getLat(), weather.getCoord().getLon()});
            String letter_case = Scan.scanString("Do you want the cities name in uppercase or lowercase(U|L)?: ", true);  

                //Initializing a different perceptron based on the users age
            if(age <= 25) perc = new PerceptronYoungTraveller(user_city);
            else if(age <= 60) perc = new PerceptronMiddleTraveller(user_city);
            else perc = new PerceptronElderTraveller(user_city);
                
                //Get the recommendations from the perceptrons
            if(letter_case.equals("U") || letter_case.equals("u")) recommendations = perc.recommend(true);
            else if(letter_case.equals("L") || letter_case.equals("l")) recommendations = perc.recommend(false);
            else recommendations = perc.recommend();
                //Print recommendations
            System.out.println("Recommendations: \n");
            for(City city: recommendations){
                System.out.println("-"+city.getName()+"\n");
            }
            if(recommendations.size() != 0){
                City closestCity = getClosestCity(perc);
                System.out.println("Closest city: " + closestCity.getName());
            }
            
        }catch(IOException e){
			System.out.println("Invalid city name or country initials" );
        }
    }

    private static City getClosestCity(PerceptronTraveller perc){
        double min = City.getMaxDist();
        City closest_city = null;
        City percCity = perc.getPercCity();
        ArrayList<City> recommendations = perc.getRecommendations();

        for(City city: recommendations){
            double temp_dist = City.geodesic_distance(city.getCoords(), percCity.getCoords()); 
            if(min > temp_dist){
                min = temp_dist;
                closest_city = city;
            }
        }
        System.out.println("Closest city in recommendations: " + closest_city.getName());
        return closest_city;
    }
    
    private static String[] getCityData(Boolean user_city){
        String cityname = Scan.scanString(user_city ? "Give your city's name: ": "Type a city's name: ", true);
        String countryInitial = Scan.scanString("And the country's initial (uk, gr etc): ", true);
        return new String[]{cityname, countryInitial};
    }
}
