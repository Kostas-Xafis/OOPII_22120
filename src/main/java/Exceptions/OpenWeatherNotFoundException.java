package Exceptions;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 24-1-2022**/
public class OpenWeatherNotFoundException extends CityException {

    public OpenWeatherNotFoundException(String city, String initials) {
        super();
        throwErrorPopup("Open Weather Error:", "Open Weather API didn't find "+ city + ", " + initials, 600, 200);
    }

    public OpenWeatherNotFoundException(){
        super();
    }
}
