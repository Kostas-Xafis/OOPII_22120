package src;

import java.io.IOException;

public class UI {
    
    UI(){};

    public static void init(){
        try{
            City city;
            while(true){
                String cityname = Scan.scanString("Type a city's name: ", true);
                String countryInitial = Scan.scanString("And the country's initial (uk, gr etc): ", true);
                OpenData.RetrieveData(cityname, countryInitial);
                break;
            }
        }catch(IOException err){
            System.out.println("Error: " + err);
        }
    }
}
