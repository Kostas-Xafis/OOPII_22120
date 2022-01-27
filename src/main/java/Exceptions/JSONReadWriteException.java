package Exceptions;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 24-1-2022**/
public class JSONReadWriteException extends CityException {

    public JSONReadWriteException(String title, String errorMsg, int w, int h){
        super();
        throwErrorPopup(title, errorMsg, w, h);
    }
}
