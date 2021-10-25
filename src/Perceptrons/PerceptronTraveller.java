package src.Perceptrons;

import java.util.ArrayList;
import src.City;

public interface PerceptronTraveller {
    public ArrayList<City> recommend();
    public ArrayList<City> recommend(Boolean bool);
    public void setRecommendations(ArrayList<City> recommendations);
    public City getPercCity();
    public ArrayList<City> getRecommendations();
}
