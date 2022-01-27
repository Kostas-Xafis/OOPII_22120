package main;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import Exceptions.CityException;
import Exceptions.OpenWeatherNotFoundException;
import Exceptions.ThreadException;
import Exceptions.WikiArticleNotFoundException;
import weather.OpenWeatherMap;
import wikipedia.MediaWiki;

/**City description and weather information using OpenData with Jackson JSON processor.
* @since 29-2-2020
* @version 1.0
* @author John Violos*/
public class OpenData {
	private static String weather_api_key = "6c0c23031468c1ee225c8b935c579b64";
	private static ObjectMapper mapper = new ObjectMapper();

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    private String city;
    private String initials;
    private Consumer<Void> onExecutionEnd;

    private OpenWeatherMap weatherData;
    private MediaWiki wikiData;


    public OpenData(String city, String initials, Consumer<Void> onExecutionEnd) {
        setFields(city, initials, onExecutionEnd);
    }

    public OpenData(){}

    private void setFields(String city, String initials, Consumer<Void> onExecutionEnd){
        this.city = city;
        this.initials = initials;
        this.onExecutionEnd = onExecutionEnd;
    }

    //Because I am using self reference within the lamda functions
    public void retrieveData(String city, String initials, Consumer<Void> onExecutionEnd) {
        setFields(city, initials, onExecutionEnd);
        retrieveData();
    }

    public void retrieveData(){
        executor.execute(new APIThreadsCaller());
    }

    /**Creates 2 separate threads to call the Wikipedia and Open Weather api whose results
     *@param city for the city's name
     *@param initials for the country's initials
     *@param onExecutionEnd a lamda function to call when all the execution of all threads is over
     *@exception CityException if there is an api error*/
    private void requestData() throws CityException, ThreadException{

        WeatherThread weather = new WeatherThread();
        WikiThread wiki = new WikiThread();
        Future<OpenWeatherMap> weatherFuture = executor.submit(weather);
        Future<MediaWiki> wikiFuture = executor.submit(wiki);

        try {
            wikiData = wikiFuture.get(5, TimeUnit.SECONDS);
            weatherData = weatherFuture.get(5, TimeUnit.SECONDS);
            executor.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    while(!wikiFuture.isDone() && !weatherFuture.isDone()){
                        wait(50);
                    }
                    onExecutionEnd.accept(null);
                    return null;
                }
            });
        } catch (InterruptedException|ExecutionException|TimeoutException e){
            executor.shutdownNow();
            if(e.getClass().equals(ExecutionException.class)) {
                Throwable cause = e.getCause();
                if(cause instanceof OpenWeatherNotFoundException){
                    Logs.info("Open Weather Data API could not find " + city + "," + initials + " city");
                    throw new OpenWeatherNotFoundException(city, initials);
                } else if(cause instanceof WikiArticleNotFoundException){
                    Logs.info("Wikipedia API could not find " + city + " city's article");
                    throw new WikiArticleNotFoundException(city);
                }
            } else if(e.getClass().equals(InterruptedException.class)){
                Logs.warning("Thread: " + Thread.currentThread().getId() +" was abruptly interrupted.");
                throw new ThreadException("Thread interruption:", "A thread was abruptly interrupted.", 500, 300);
            } else {
                Logs.warning("Thread: " + Thread.currentThread().getId() + " timed out.");
                throw new ThreadException("Thread timeout:", "Thread run longer than expected due to connection issues.", 600, 300);
            }
        }
    }


    /** When the threaded api call is done this function should be called to retrive the open weather data
     *@return Returns the requested data from the Open Weather api*/
    public OpenWeatherMap getWeatherData(){
        if(weatherData != null){
            OpenWeatherMap tmp = weatherData;
            weatherData = null;
            return tmp;
        }
        return null;
    }

    /** When the threaded api call is done this function should be called to retrive the Wikipedia data
     *@return Returns the requested data from the Wikipedia api */
    public MediaWiki getWikiData(){
        if(wikiData != null){
            MediaWiki tmp = wikiData;
            wikiData = null;
            return tmp;
        }
        return null;
    }

    /**
	 * Calls the openweather api with the given city name and country initials
	 *@param city The city's name
	 *@param initials Country's initials
	 *@return {@code OpenWeatherMap} object from the api
	 *@exception OpenWeatherNotFoundException */
    private OpenWeatherMap RetrieveWeatherData() throws OpenWeatherNotFoundException{
        try{
            return mapper.readValue(new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+","+initials+"&APPID="+weather_api_key+""), OpenWeatherMap.class);
        } catch (IOException e){
            throw new OpenWeatherNotFoundException();
        }
    }

    /**
     * Calls the wiki api with the given city name
     *@param city The city's name
     *@return {@code MediaWiki} object from the api
     *@exception WikiArticleNotFoundException */
    private MediaWiki RetrieveWikiData() throws WikiArticleNotFoundException{
        try{
            return mapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles="+ city.replaceAll(" ", "_") +"&format=json&formatversion=2"), MediaWiki.class);
        } catch (IOException e){
            throw new WikiArticleNotFoundException();
        }
    }

    public ExecutorService getExecutor(){
        return this.executor;
    }


    private class WeatherThread implements Callable<OpenWeatherMap>{

        @Override
        public OpenWeatherMap call() throws OpenWeatherNotFoundException {
            return RetrieveWeatherData();
        }
    }

    private class WikiThread implements Callable<MediaWiki>{

        @Override
        public MediaWiki call() throws WikiArticleNotFoundException {
            MediaWiki wiki = RetrieveWikiData();
            return wiki;
        }
    }

    private class APIThreadsCaller implements Runnable {
        @Override
        public void run() {
            try {
                requestData();
            } catch (Exception e){
            }
        }
    }
}
