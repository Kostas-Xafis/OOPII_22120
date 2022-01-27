package Exceptions;

import components.InvalidFrame;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 24-1-2022**/
public class ThreadException extends Exception  {

    public ThreadException(String title, String errorMsg, int w, int h){
        super();
        new InvalidFrame("Thread Error", title, errorMsg, w, h);
    }
}
