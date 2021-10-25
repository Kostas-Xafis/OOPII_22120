package src.Perceptrons;

import java.util.ArrayList;
import src.City;

public class PerceptronElderTraveller implements PerceptronTraveller{
    private static double[] weightBias = new double[]{-.2, .1, .5, .6, -.4, .4, -.1, .3, -.2, -.1};
    private City percCity; //Perceptron's city
    private ArrayList<City> recommendations;
        /*-----------
          Constructor
          -----------*/
    public PerceptronElderTraveller(City perc_city){
        this.percCity = perc_city;
    }
        /*---------------
          Getters/Setters
          ---------------*/
    public ArrayList<City> getRecommendations(){
        return this.recommendations;
    }

    @Override
    public void setRecommendations(ArrayList<City> recommendations) {
        this.recommendations = recommendations;
    }

    public City getPercCity(){
        return this.percCity;
    }
        /*---------------
          Recommendations
          ---------------*/    
    @Override
    public ArrayList<City> recommend(){
        double[] res = new double[10];
        ArrayList<City> recommendedCities = new ArrayList<City>();
        int j = 0;
        for(City city : City.Cities){
            double[] dt = city.compareDistance(this.percCity).getNormData();
            int i = 0;
            for(double val : dt) res[j] += val * weightBias[i++];
            if(res[j] > 0) recommendedCities.add(city);
            System.out.println("Weighted sum " + j + ": " + res[j]);
            ++j;
        }
        return recommendedCities;
    }

    @Override
    public ArrayList<City> recommend(Boolean bool) {
        ArrayList<City> recommendations =  this.recommend();
        for(City city : recommendations) city.setLetterCase(bool);
        return recommendations;
    }
}