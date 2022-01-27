package citySorters;

import java.util.Comparator;

import main.City;

/**
 * Ascending order sort of the registration timestamp
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 10-07-2021*/
public class ascTimestamp implements Comparator<City>{

    /**
     * Compares the registration timestamps of 2 cities
     *@param c1 Compared city 1
     *@param c2 Compared city 2
     *@return Returns 1 if the c1 timestamp is bigger than c2 else -1*/
    @Override
    public int compare(City c1, City c2){
        return c1.getTimestamp() >= c2.getTimestamp() ? 1 : -1;
    }
}
