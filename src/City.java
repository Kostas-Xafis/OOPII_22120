package src;

import java.util.HashMap;

public class City {
    public HashMap<String, Number> data;

    public City(int cafe, int sea, int museums, int restaurant, 
                int stadium, int bars, int a, float temp, float clouds, float dist){
        data.put("cafe", cafe);
        data.put("sea", sea);
        data.put("museums", museums);
        data.put("restaurant", restaurant);
        data.put("stadium", stadium);
        data.put("bars", bars);
        data.put("a", a);
        data.put("temp", temp);
        data.put("clouds", clouds);
        data.put("dist", dist);

    }
}
