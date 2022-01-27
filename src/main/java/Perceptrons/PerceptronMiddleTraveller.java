package Perceptrons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import main.City;
import citySorters.ascTimestamp;

/**
 * {@code PerceptronMiddleTraveller} contains functions for recommendations to middle aged travellers
 *@author Konstantinos Xafis it22120
 *@version 1.1
 *@since 22-10-2021*/
public class PerceptronMiddleTraveller implements PerceptronTraveller{
    private double[] weightBias = new double[]{.1, .2, .4, .4, -.2, .2, .2, .3, -.2, -.2};
    private double[] personalBias = new double[7];
    private City percCity; //Perceptron's city
    private ArrayList<City> recommendations;
        /*-----------
          Constructor
          -----------*/
    /**
     *@param perc_city The city where the recommendations will be based on
    */
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

    /** Setter
     *@param recommendations An {@code ArrayList<City>} to set the objects's recommendations*/
    public void setRecommendations(ArrayList<City> recommendations) {
        this.recommendations = recommendations;
    }

    /** Setter
     *@param biases An {@code Integer[]} to set the user's bias*/
    public void setBiases(Integer[] biases){
        for (int i = 0; i < biases.length; i++) {
            personalBias[i] = biases[i].intValue() / 10.0;
        }
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
    public City personalizedRecommend(Collection<City> collection) {
        City[] cityArr = (City[]) Array.newInstance(City.class, City.Cities.size());
        collection.toArray(cityArr);
        Stream<City> stream = Stream.of(cityArr);
        HashMap<String, Double> citiesProduct = new HashMap<>();
        Optional<City> recommendation = stream.filter((City c) -> {
            if(this.percCity.equals(c)) return false;
            double res = 0;
            double[] dt = c.compareDistance(this.percCity).getNormData();

            for (int i = 0; i < personalBias.length; i++) {
                res += dt[i] * personalBias[i];
            }

            if(res > 0) {
                citiesProduct.put(c.getName()+c.getInitials(), res);
                return true;
            }
            return false;
        }).max((City c1, City c2) -> {
            double product1 = citiesProduct.get(c1.getName()+c1.getInitials());
            double product2 = citiesProduct.get(c2.getName()+c2.getInitials());
            return product1 > product2 ? 1 : -1;
        });

        return recommendation.isPresent() ? recommendation.get() : null;
    }

    @Override
    public City personalizedRecommend(Collection<City> collection, Boolean bool) {
        City c = personalizedRecommend(collection);
        c.setLetterCase(bool);
        return c;
    }

    @Override
    public ArrayList<City> sortRecommendations() {
        if(this.recommendations.size() == 0) return null;
        Collections.sort(this.recommendations, new ascTimestamp());
        return this.recommendations;
    }

}
