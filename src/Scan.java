package src;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Scan Class 
 * Contains functions to easily and safely scan for user input
 * while removing repetitive chunks of code
 *@author Konstantinos Xafis it22120
 *@version 1.1
 *@since 25-06-2021*/
public class Scan {
    private static Scanner inp = new Scanner(System.in); 

    /**
     * Clears the input buffer
    */
    private static void clearBuffer(){
        inp.nextLine();
    }

    /**
     * Scans for an integer
     *@param msg is the message to display before scanning for the input
     *@param nonNegative if the integer must be strictly positive
     *@return Returns the scanned int*/
    public static int scanInt(String msg, boolean nonNegative){
        try{
            System.out.print(msg);
            int i = inp.nextInt();
            clearBuffer();
            if(nonNegative && i < 0){
                System.out.println("Must give positive number\n");
                return scanInt(msg, nonNegative);
            }
            return i;
        }catch(InputMismatchException err){
            System.out.println("Wrong input\n");
            clearBuffer();
            return scanInt(msg, nonNegative);
        }
    }

    /**
     * Scans for a float
     *@param msg is the message to display before scanning for the input
     *@param nonNegative if the integer must be strictly positive
     *@return Returns the scanned float*/
    public static float scanFloat(String msg, boolean nonNegative){
        //Same stuff but with floating point numbers
        try{
            System.out.print(msg);
            float i = inp.nextFloat();
            clearBuffer();
            if(nonNegative && i < 0){
                System.out.println("Must give positive number\n");
                return scanFloat(msg, nonNegative);
            }
            return i;
        }catch(InputMismatchException err){
            System.out.println("Wrong input\n");
            clearBuffer();
            return scanFloat(msg, nonNegative);
        }
    }

    /**
     * Scans for a string
     *@param msg is the message to display before scanning for the input
     *@param strict if the input can contain any special character or number
     *@return Returns the scanned int*/
    public static String scanString(String msg, Boolean strict){
        //Same thing with the above but here i don't need a try catch block
        //since everything is converted to text
        System.out.print(msg);
        String s = inp.nextLine();
        if(strict && s.matches(".*(\\d|[^ \\w\\n]).*")){ //Contains a number or a special character except space
            System.out.println("This field can not contain numbers or special characters\n");
            return scanString(msg, strict);
        }
        return s;
    }
}
