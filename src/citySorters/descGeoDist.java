package src.citySorters;

import java.util.Comparator;

import src.City;

/**
 * Descending order sort of the geodesic distance
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 10-07-2021*/
public class descGeoDist implements Comparator<City>{

    /**
     * Compares the geodesic distance of 2 cities 
     *@param c1 Compared city 1
     *@param c2 Compared city 2
     *@return Returns 1 if the c1 geodesic distance is smaller than c2 else -1*/
    public int compare(City c1, City c2){
        return c1.getNormData()[9] <= c2.getNormData()[9] ? 1 : -1;
    }

}