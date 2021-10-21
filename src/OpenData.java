package src;
import java.net.*;
import com.fasterxml.jackson.databind.*;

public class OpenData {    
    private static ObjectMapper mapper = new ObjectMapper();

    private static String baseUrl = "http://en.wikipedia.org/w/api.php?";

    public OpenData(){}

    public static void queryCity(String city){
        try{
            URL url = new URL(baseUrl + "action=query&srsearch=England&format=json");
            // mapper.readValue(url, WikiData.class);
            System.out.println(url + url.toString());
        } catch(MalformedURLException err){
            System.out.println("URL error:" + err);
        }
    }
}
