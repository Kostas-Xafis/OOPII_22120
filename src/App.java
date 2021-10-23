package src;

import java.io.IOException;
public class App{
    public static void main(String[] args) throws IOException {
        // Add dummy cities for testing
        OpenData.RetrieveData("Athens", "gr");
        OpenData.RetrieveData("Patra", "gr");
        OpenData.RetrieveData("Thessaloniki", "gr");
        OpenData.RetrieveData("Paris", "fr");
        OpenData.RetrieveData("Seattle", "us");
        UI.init();
    }
}
