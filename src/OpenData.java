package src;

import java.io.IOException;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.weather.OpenWeatherMap;
import src.wikipedia.MediaWiki;
/**City description and weather information using OpenData with Jackson JSON processor.
* @since 29-2-2020
* @version 1.0
* @author John Violos */
public class OpenData {
	private static String weather_api_key = "6c0c23031468c1ee225c8b935c579b64";
	private static ObjectMapper mapper = new ObjectMapper();
	/**Retrieves weather information, geotag (lan, lon) and a Wikipedia article for a given city.
	* @param city The Wikipedia article and OpenWeatherMap city. 
	* @param country The country initials (i.e. gr, it, de).*/ 
	public static void RetrieveData(String city, String country){
		try{
			OpenWeatherMap weather_obj = RetrieveWeatherData(city, country);
			MediaWiki mediaWiki_obj =  RetrieveWikiData(city);
			City.extractData(weather_obj, mediaWiki_obj, city);
		}catch(IOException e){
			System.out.println("Invalid city name or country initials" );
		}
	}
	public static OpenWeatherMap RetrieveWeatherData(String city, String country)throws IOException{
		return mapper.readValue(new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&APPID="+weather_api_key+""), OpenWeatherMap.class);
	}
	public static MediaWiki RetrieveWikiData(String city) throws IOException{
		return mapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles="+ city.replaceAll(" ", "_") +"&format=json&formatversion=2"), MediaWiki.class);
	}

}
