package main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 25-1-2022**/
public class Logs {

    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    public static void init(){
        Calendar cal = Calendar.getInstance();
        String logFileName = "./logs/logfile_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + (cal.get(Calendar.MONTH)+1) + "_" + cal.get(Calendar.YEAR) + ".log";
        FileHandler fileHandler = null;
        File file = new File(logFileName);
        try{
            if(!file.exists())
                file.createNewFile();
            fileHandler = new FileHandler(logFileName);
        } catch (IOException|SecurityException e){

        } finally {
            logger.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);

            info("============Program Started==============");
        }
    }

    public static void fine(String fine){
        logger.fine(fine);
    }

    public static void info(String msg){
        logger.info(msg);
    }

    public static void warning(String warning){
        logger.warning(warning);
    }

    public static void severe(String severe){
        logger.severe(severe);
    }
}
