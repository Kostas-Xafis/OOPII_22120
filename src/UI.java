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
    UI(){};

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

    private static void add_city() {
        try{
            String[] city_name = getCityName(false);
            OpenData.RetrieveData(city_name[0], city_name[1]);
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void getRecommendations(){
        try{
            int age;
            PerceptronTraveller pt = null;
            ArrayList<City> recommendations;
                //Get user's age
            while(true){
                age = Scan.scanInt("Specify your age: ", true);
                if(age >= 16 && age <= 115) break;
                System.out.println("You need to be over 16 and under 115 yo to apply.");
            }            
                //Get user's city coords
            String[] user_city_name = getCityName(true);
            OpenWeatherMap weather =  OpenData.RetrieveWeatherData(user_city_name[0], user_city_name[1]);
            City user_city = new City(new double[]{weather.getCoord().getLat(), weather.getCoord().getLon()});
            String letter_case = Scan.scanString("Do you want the cities name in uppercase or lowercase(U|L)?: ", true);                        
                //Initializing a different perceptron based on the users age
            if(age <= 25) pt = new PerceptronYoungTraveller(user_city);
            else if(age <= 60) pt = new PerceptronMiddleTraveller(user_city);
            else pt = new PerceptronElderTraveller(user_city);
                
                //Get the recommendations from the perceptrons
            if(letter_case.equals("U") || letter_case.equals("u")) recommendations = pt.recommend(true);
            else if(letter_case.equals("L") || letter_case.equals("l")) recommendations = pt.recommend(false);
            else recommendations = pt.recommend();
            pt.setRecommendations(recommendations);

            System.out.println("Recommendations: \n");
            for(City city: recommendations){
                System.out.println("-"+city.getName()+"\n");
            }

            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    private static String[] getCityName(Boolean user_city){
        String cityname = Scan.scanString(user_city ? "Give your city's name: ": "Type a city's name: ", true);
        String countryInitial = Scan.scanString("And the country's initial (uk, gr etc): ", true);
        return new String[]{cityname, countryInitial};
    }
    
}
