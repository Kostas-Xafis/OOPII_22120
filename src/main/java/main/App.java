package main;

import java.io.File;

import Exceptions.JSONReadWriteException;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 21-10-2021**/
public final class App {
    public static void main(String[] args) {
        Logs.init();
        try{
            City.readJSON();
            Logs.fine("Cities data were successfully loaded");
        } catch(JSONReadWriteException e){
            File file = new File("./logs/Cities.json");
            if(file.length() == 0)
                Logs.info("JSON error while trying to read empty file.");
            else
                Logs.severe("JSON error while trying to read non empty file.");
        }

        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        new GUI().init();

    }
}
