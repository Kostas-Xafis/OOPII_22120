package src.Perceptrons;

import java.util.ArrayList;
import src.City;

public class PerceptronMiddleTraveller implements PerceptronTraveller{
    private static double[] weightBias = new double[]{.4, .8, .2, .1, .3, -.2, .4, .7, -.3, -.5};
    private City percCity; //Perceptron's city
    private ArrayList<City> recommendations;

    public PerceptronMiddleTraveller(City perc_city){
        this.percCity = perc_city;
    }

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

    public static City getClosestCity(PerceptronTraveller perc){
        double min = City.getMaxDist();
        City closest_city = null;
        City percCity = ((PerceptronYoungTraveller) perc).getPercCity();
        ArrayList<City> recommendations = ((PerceptronYoungTraveller) perc).getRecommendations();

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