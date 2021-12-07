package src;

import java.io.IOException;
import java.util.ArrayList;

import src.Perceptrons.PerceptronElderTraveller;
import src.Perceptrons.PerceptronMiddleTraveller;
import src.Perceptrons.PerceptronTraveller;
import src.Perceptrons.PerceptronYoungTraveller;

/**
* City class contains all necessary to create 
*@author Konstantinos Xafis it22120
*@version 1.0
*@since 21-10-2021*/
public class UI {
    /** Initializes the main ui loop */
    public static void init() {
        //Load stored cities
        try{
            City.readJSON();
            boolean loop = true;
            while(loop){
                int ans = Scan.scanInt("Give one of the following: \n" +
                "-1 To add a city\n" +
                "-2 To get a recommandation\n" +
                "-3 To exit\n-", true);
                switch(ans){
                    case 1:{
                        UI.add_city(false);
                        break;
                    }
                    case 2:{
                        UI.getRecommendations();
                        break;
                    }
                    case 3:{
                        City.writeJSON();
                        loop = false;
                        break;
                    }
                    default:{
                        System.out.println("Incorrect input");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + e.getStackTrace());
        }
    }

    /**
     * Asks for user input, then creates and returns the new city if it's not already registered.
     * else if it's a duplicate it returns the original
     *@param userPick True if it's going to ask for the user's city
     *@return Returns the city with the given user input*/
    private static City add_city(Boolean userPick)  { //This function is for testing purposes
        String[] city_data = getCityData(userPick);
        City new_city = new City(city_data[0], city_data[1]); //Checks if the city exists
        if(new_city.getIsDuplicate() == true) return City.findCity(city_data[0], city_data[1]);
        return new_city;
    }

    /**
     * Asks for the appropriate user input, then calculates and displays the recommended cities
    */
    private static void getRecommendations(){
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
        City user_city = UI.add_city(true);
        String letter_case = Scan.scanString("Display cities name in uppercase or lowercase(U|L)?: ", true);
        String sort = Scan.scanString("Display cities in sorted order(Y|N)?: ", true);

            //Initializing a different perceptron based on the users age
        if(age <= 25) perc = new PerceptronYoungTraveller(user_city);
        else if(age <= 60) perc = new PerceptronMiddleTraveller(user_city);
        else perc = new PerceptronElderTraveller(user_city);

            //Get the recommendations from the perceptrons
        if(letter_case.equals("U") || letter_case.equals("u")) recommendations = perc.recommend(true);
        else if(letter_case.equals("L") || letter_case.equals("l")) recommendations = perc.recommend(false);
        else recommendations = perc.recommend();
        if(sort.equals("Y") || sort.equals("y")) recommendations = perc.sortRecommendations();

            //Print recommendations
        if(recommendations.size() != 0){
            System.out.println("Recommendations: \n");
            for(City city: recommendations){
                System.out.println("-"+city.getName()+"\n");
            }
            City closestCity = getClosestCity(perc);
            System.out.println("Closest city: " + closestCity.getName());
        } else System.out.println("Could not find any good recommendations");
    }

    /**
     * Finds the closest recommended city from the traveller
     *@param perc The perceptron of the traveller
     *@return Returns the closest recommended city from traveller*/
    private static City getClosestCity(PerceptronTraveller perc){
        double min = City.getMaxDist();
        City closest_city = null;
        City perc_city = perc.getPercCity();
        ArrayList<City> recommendations = perc.getRecommendations();

        for(City city: recommendations){
            double temp_dist = City.geodesic_distance(city.getCoords(), perc_city.getCoords()); 
            if(min > temp_dist){
                min = temp_dist;
                closest_city = city;
            }
        }
        System.out.println("Closest city in recommendations: " + closest_city.getName());
        return closest_city;
    }

    /**
     * User's input for his city name and country initials 
     *@param user_city if it's going for the user's city or not
     *@return A string array containing [the city's name, the country's initials where the city is located] */
    private static String[] getCityData(Boolean user_city){
        String city_name = Scan.scanString(user_city ? "Give your city's name: ": "Type a city's name: ", true);
        String countryInitials = Scan.scanString("And the country's initial (uk, ge etc): ", true);
        return new String[]{city_name, countryInitials};
    }
}