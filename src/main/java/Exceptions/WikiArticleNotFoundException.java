package Exceptions;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 24-1-2022**/
public class WikiArticleNotFoundException extends CityException {

    public WikiArticleNotFoundException(String city) {
        super();
        System.out.println("Wiki error");
        throwErrorPopup("Wiki Article Error:", "The wiki article of "+ city + " was not found", 600, 200);
    }

    public WikiArticleNotFoundException(){
        super();
    }
}
