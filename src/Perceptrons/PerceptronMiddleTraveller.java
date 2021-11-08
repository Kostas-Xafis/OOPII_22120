package src.Perceptrons;

import java.util.ArrayList;
import src.City;

public class PerceptronMiddleTraveller implements PerceptronTraveller{
    private static double[] weightBias = new double[]{.1, .2, .4, .4, -.2, .2, .2, .3, -.2, -.2};
    private City percCity; //Perceptron's city
    private ArrayList<City> recommendations;
        /*-----------
          Constructor
          -----------*/
    public PerceptronMiddleTraveller(City perc_city){
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

    public void setRecommendations(ArrayList<City> recommendations) {
        this.recommendations = recommendations;
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
        this.recommendations = recommendations;
        return recommendedCities;
    }

    @Override
    public ArrayList<City> recommend(Boolean bool) {
        ArrayList<City> recommendations =  this.recommend();
        for(City city : recommendations) city.setLetterCase(bool);
        return recommendations;
    }
}