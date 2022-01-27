package Exceptions;

import components.InvalidFrame;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 24-1-2022**/
public class CityException extends Exception  {

    public CityException(){
        super();
    }

    public void throwErrorPopup(String title, String errorMsg, int w, int h) {
        new InvalidFrame("City Error", title, errorMsg, w, h);
    }
}
