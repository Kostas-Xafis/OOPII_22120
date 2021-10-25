package src;

import java.io.IOException;
public class App{
    public static void main(String[] args) throws IOException {
        // Add dummy cities for testing
        OpenData.RetrieveData("New York City", "us");
        OpenData.RetrieveData("Athens", "gr");
        OpenData.RetrieveData("London", "uk");
        OpenData.RetrieveData("Paris", "fr");
        OpenData.RetrieveData("Tokyo", "jp");
        OpenData.RetrieveData("Beijing", "cn");
        OpenData.RetrieveData("California", "us");
        UI.init();
    }
}
