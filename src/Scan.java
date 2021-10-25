package src;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Scan {
    private static Scanner inp = new Scanner(System.in); 

        //Clears the input buffer when needed
    private static void clearBuffer(){
        inp.nextLine();
    }

    //scanInt, scanFloat, scanString are functions to easily 
    //scan for something by removing repetitive chunks of code

    public static int scanInt(String msg, boolean nonNegative){ //The parameters i believe are self explanatory
        //Scan for an integer and clearing the buffer inside a try catch block
        //so that the instance doesn't close 
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

    public static String scanString(String msg, Boolean strict){
        //Same thing with the above but here i don't need a try catch block
        //since everything is converted to text
        System.out.print(msg);
        String s = inp.nextLine();
        if(strict && s.matches(".*(\\d|[^ \\w\\n]).*")){ //Contains a number or a special character
            System.out.println("This field can not contain numbers or special characters\n");
            return scanString(msg, strict);
        }
        return s;
    }
}
