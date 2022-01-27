package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import Exceptions.JSONReadWriteException;
import components.AddCity;
import components.Home;
import components.Recommendations;
import components.UserPreferences;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 18-1-2022**/
public class GUI extends JFrame {
    public void init(){
        initFrame();
        CardLayout cl = new CardLayout();
        JPanel mainPanel = new JPanel(cl);
        mainPanel.add(new Home(), "HOME");
        mainPanel.add(new AddCity(), "ADD_CITY");
        mainPanel.add(new UserPreferences(), "USER_PREFERENCES");
        mainPanel.add(new Recommendations(), "RECOMMENDATIONS");
        cl.show(mainPanel, "HOME");

        add(mainPanel);
        setSize(1280, 720);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev){
                try{
                    City.writeJSON();
                    Logs.fine("Cities data were successfully stored");
                } catch (JSONReadWriteException e){
                    Logs.severe("JSON error while rewriting to file");
                } finally {
                    Logs.info("============ Program Ended ==============");
                }
            }
        });
    }

    private void initFrame(){
        int width = 1280;
        int height = 720;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int swidth = screenSize.width, sheight = screenSize.height;

        setTitle("Travelling Agency");
        setLocation((swidth - width) / 2, (sheight - height) / 2); //Position it in the center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

