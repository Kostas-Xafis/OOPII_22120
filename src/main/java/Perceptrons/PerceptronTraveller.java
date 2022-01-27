package Perceptrons;

import java.util.ArrayList;
import java.util.Collection;

import main.City;

/** PerceptronTraveller interface
 *@author Konstantinos Xafis it22120
 *@version 1.1
 *@since 22-10-2021*/
public interface PerceptronTraveller {
    /**
     * Creates the recommendations based on the {@code weightBias}
     *@return The {@code ArrayList<City>} of the recomendations*/
    public ArrayList<City> recommend();

    /**
     * Creates the recommendations based on the {@code weightBias}
     * And sets the letter casing
     *@return The {@code ArrayList<City>} of the recomendations*/
    public ArrayList<City> recommend(Boolean bool);

    /**
     * Creates the recommendations based on the {@code weightBias}
     *@return Returns a city based on the personalized weightBias*/
    public City personalizedRecommend(Collection<City> collection);

    /**
     * Creates the recommendations based on the {@code weightBias}
     * And sets the letter casing
     *@return Returns a city based on the personalized weightBias*/
    public City personalizedRecommend(Collection<City> collection, Boolean bool);



    /**
     * Sets the weight biases based on the user preferences
     *@return The {@code Integer[]} of the weight biases*/
    public void setBiases(Integer[] biases);

    /** Getter
     *@return The perceptron's city */
    public City getPercCity();

    /** Getter
     *@return The {@code ArrayList<City>} of the recomendations*/
    public ArrayList<City> getRecommendations();

    /**
     * Sorts the recommendations based on a {@code src.citySorters} class
     *@return The {@code ArrayList<City>} of the sorted recomendations */
    public ArrayList<City> sortRecommendations();
}
