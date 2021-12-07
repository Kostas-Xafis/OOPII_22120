package src.Perceptrons;

import java.util.ArrayList;
import src.City;

/** PerceptronTraveller interface
 *@author Konstantinos Xafis it22120
 *@version 1.1
 *@since 22-10-2021*/
public interface PerceptronTraveller {
    /**
     * Creates the recommendations based on the {@code weightBias} and the {@code perc_city}
     *@return The {@code ArrayList<City>} of the recomendations*/
    public ArrayList<City> recommend();
    
    /**
     * Creates the recommendations based on the {@code weightBias} and the {@code perc_city}
     * And sets the letter casing
     *@return The {@code ArrayList<City>} of the recomendations*/
    public ArrayList<City> recommend(Boolean bool);
    
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
