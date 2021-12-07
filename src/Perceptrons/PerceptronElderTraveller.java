package src.Perceptrons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import src.City;
import src.citySorters.descGeoDist;

/**Class Desciption
 * {@code PerceptronElderTraveller} contains functions for recommendations to elderly travellers
 *@author Konstantinos Xafis it22120
 *@version 1.1
 *@since 22-10-2021*/
public class PerceptronElderTraveller implements PerceptronTraveller{
    private static double[] weightBias = new double[]{-.2, .1, .5, .6, -.4, .4, -.1, .3, -.2, -.1};
    private City percCity; //Perceptron's city
    private ArrayList<City> recommendations;
        /*-----------
          Constructor
          -----------*/
    /**
     *@param perc_city The city where the recommendations will be based on
    */
    public PerceptronElderTraveller(City perc_city){
        this.percCity = perc_city;
    }
        /*---------------
          Getters/Setters
          ---------------*/
    public ArrayList<City> getRecommendations(){
        return this.recommendations;
    }    

    public City getPercCity(){
        return this.percCity;
    }

    /** Setter
     *@param recommendations An {@code ArrayList<City>} to set the objects's recommendations*/
    public void setRecommendations(ArrayList<City> recommendations) {
        this.recommendations = recommendations;
    }
        /*---------------
          Recommendations
          ---------------*/
    @Override
    public ArrayList<City> recommend(){
        double[] res = new double[City.Cities.size()];
        ArrayList<City> recommendedCities = new ArrayList<City>();
        int j = 0;
        for(Map.Entry<String, City> entry : City.Cities.entrySet()){
            City c = entry.getValue();
            if(this.percCity.equals(c)) continue;

            double[] dt = c.compareDistance(this.percCity).getNormData();
            int i = 0;
            for(double val : dt) res[j] += val * weightBias[i++];

            if(res[j] > 0) recommendedCities.add(c);            
            // System.out.println("Weighted sum " + j + ": " + res[j]);
            ++j;
        }
        this.setRecommendations(recommendedCities);
        return recommendedCities;
    }

    @Override
    public ArrayList<City> recommend(Boolean bool) {
        ArrayList<City> recommendations =  this.recommend();
        for(City city : recommendations) city.setLetterCase(bool);
        return recommendations;
    }

    @Override
    public ArrayList<City> sortRecommendations() {
        if(this.recommendations.size() == 0) return null;
        Collections.sort(this.recommendations, new descGeoDist());
        return this.recommendations;
    }
}