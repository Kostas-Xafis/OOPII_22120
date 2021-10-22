package src;

import java.io.IOException;
import src.weather.OpenWeatherMap;
import src.wikipedia.MediaWiki;
import java.util.HashMap;

public class City {
    private static final String[] features = new String[]{"cafe", "sea", "museum", "restaurant", "stadium", "culture", "transport", "temp", "clouds", "dist"};
    private static final double maxdist = geodesic_distance(new Number[]{0, 0}, new Number[]{0, 180});
    
    private HashMap<String, Number> data = new HashMap<String, Number>();

    public City(double[] norm_data){
        int i = 0;
        for(String feature : features){
            data.put(feature, (Number) norm_data[i++]);
        }
        this.printData();
    }

    public static void extractData(OpenWeatherMap weather, MediaWiki wiki)throws IOException{
        String wikistr = wiki.getQuery().getPages().get(0).getExtract();
        Number[][] data = new Number[10][3];
      
        for(int i = 0; i < 7; i++){
            int len = wikistr.split(" " + features[i] + "( |s|\\.)").length; //the feature's name + plural form
            data[i][0] = len > 10 ? 10 : len;
            data[i][1] = 10;
            data[i][2] = 0;
        }

        data[7] = new Number[]{weather.getMain().getTemp(), weather.getMain().getTempMax(), weather.getMain().getTempMin()};
        data[8] = new Number[]{(Number)weather.getClouds().getAll(), 100, 0};

        String cityname = Scan.scanString("Type a city's name to compare: ", true);
        String countryInitial = Scan.scanString("And the country's initial (uk, gr etc): ", true);
        OpenWeatherMap city2_data = OpenData.RetrieveWeatherData(cityname, countryInitial);
        Number[] city1_coords = new Number[]{weather.getCoord().getLat(), weather.getCoord().getLon()};
        Number[] city2_coords = new Number[]{city2_data.getCoord().getLat(), city2_data.getCoord().getLon()};
        data[9] = new Number[]{geodesic_distance(city1_coords, city2_coords), maxdist, 0};

        normalizeData(data);
    }

    private static void normalizeData(Number[][] data){
        double[] normalized_values = new double[10];
        for(int i = 0; i < 10; i ++){
            Number[] temp = data[i];
            normalized_values[i] = (temp[0].floatValue() - temp[2].floatValue()) 
                                    / (temp[1].floatValue() - temp[2].floatValue());
        }
        new City(normalized_values);
    }

    private static double geodesic_distance(Number[] c1, Number[] c2){
        double lat1 = c1[0].doubleValue();
        double lon1 = c1[1].doubleValue();
        double lat2 = c2[0].doubleValue();
        double lon2 = c2[1].doubleValue();

        if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			return dist;
		}
    }
    public void printData(){
        System.out.println("City data: ");
        for(String feature: features){
            System.out.println("\t" + feature + ": " + this.data.get(feature));
        }
    }

}

