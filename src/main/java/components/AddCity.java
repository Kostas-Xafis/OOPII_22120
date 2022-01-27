package components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

import main.City;
import main.Logs;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 20-1-2022**/
public class AddCity extends JPanel {
    private static Globals g = new Globals();

    private static Color greenCol = new Color(15, 217, 40);

    private String cityInput;
    private String initialsInput;


    public AddCity(){
        setLayout(new BorderLayout());
        g.setWhiteBg(this);
        {
            JPanel fieldContainer = new JPanel(new GridBagLayout());
            InputField fieldCity = new InputField("City Name :", (s) -> cityInput = s);
            InputField fieldInitials = new InputField("Country Initials :", (s) -> initialsInput = s);

            g.addElementToGrid(fieldContainer, fieldCity, 0, 0, 1, 1, new Insets(0, 0, 0, 100));
            g.addElementToGrid(fieldContainer, fieldInitials, 1, 0, 1, 1, new Insets(0, 100, 0, 0));

            g.setWhiteBg(fieldContainer);

            fieldCity.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    System.out.println(e.toString());
                }
                @Override
                public void focusLost(FocusEvent e) {
                    System.out.println(e.toString());
                }
            });

            add(fieldContainer, BorderLayout.CENTER);
        }
        {
            JPanel upperContainer = new JPanel(new GridLayout());
            g.setWhiteBg(upperContainer);
            add(upperContainer, BorderLayout.SOUTH);

            BackButton backButton = new BackButton("images/leftArrow.svg", (i) -> {
                JComponent mainPanel = (JComponent) getParent();
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "HOME");
            } );
            upperContainer.add(backButton);


            { // Right-aligned "Add City" button
                JPanel alignContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JPanel buttonContainer = new JPanel(new GridBagLayout());
                JLabel buttonTxt = new JLabel("Add City");
                JSVGCanvas svg = new JSVGCanvas();

                g.addElementToGrid(buttonContainer, buttonTxt, 0, 0, 3, 1, new Insets(5, 5, 5, 5));
                g.addElementToGrid(buttonContainer, svg, 3, 0, 1, 1, new Insets(5, 5, 5, 5));
                alignContainer.add(buttonContainer);
                upperContainer.add(alignContainer);


                g.setWhiteBg(alignContainer);
                alignContainer.setBorder(new EmptyBorder(new Insets(0, 0, 50, 75)));

                g.setWhiteBg(buttonContainer);
                buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));


                buttonTxt.setFont(buttonTxt.getFont().deriveFont(32f));
                g.setTxtCol(buttonTxt);

                svg.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
                String s = new File("images/plus.svg").toString();
                svg.setMySize(new Dimension(32, 32));
                svg.setBackground(new Color(0, 0, 0, 0));
                svg.setURI(s);

                buttonContainer.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e){
                        buttonContainer.setBorder(BorderFactory.createLineBorder(greenCol, 2, true));
                        buttonContainer.setBackground(greenCol);
                        buttonTxt.setForeground(g.white);
                        SVGDocument doc = svg.getSVGDocument();
                        doc.getElementById("plus").setAttribute("fill", "rgb(255, 255, 255)");
                        svg.setSVGDocument(doc);
                    }

                    public void mouseExited(MouseEvent e){
                        buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));
                        g.setWhiteBg(buttonContainer);
                        g.setTxtCol(buttonTxt);
                        SVGDocument doc = svg.getSVGDocument();
                        doc.getElementById("plus").setAttribute("fill", "rgb(30, 30, 30)");
                        svg.setSVGDocument(doc);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e){
                        buttonContainer.requestFocus(true); //Remove focus from the last focused input field
                    }
                });
                buttonContainer.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        requestFocus(true);
                        validateInput();
                    }
                });
            }
        }
    }

    private void validateInput(){
        if(cityInput != null && initialsInput != null){
            int initLength = initialsInput.length();
            String[] errorMsg = (String[]) Array.newInstance(String.class, 2);

            if(cityInput.matches(".*(\\d|[^ \\w\\n]).*"))
                errorMsg[0] = "City names must not contain special characters or numbers.";

            if( 2 > initLength || initLength > 3 )
                errorMsg[1] = "Country initials must have either 2 or 3 characters.";

            if(errorMsg[0] != null || errorMsg[1] != null){
                Logs.info("User gave invalid input for the city's fields");
                new InvalidFrame("Invalid input","Input error:", errorMsg, 600, 250);
                return;
            }

            addCity(cityInput, initialsInput);
        } else {
            Logs.info("User tried to add a city with empty fields");
            new InvalidFrame("Invalid input", "Input error:", "You must fill all the fields.", 300, 200);
        }
    }

    private void addCity(String name, String initials){
        City new_city = new City(name, initials, (i) -> {
            Logs.fine("Successfully added " + name + "," + initials + " city");
            new ValidFrame("City added !", 400, 200);
        });
        if(new_city.getIsDuplicate()){
            Logs.info("User tried to add an already registered city");
            new ValidFrame("City was registered at " + new Date(new_city.getTimestamp()).toString() + ".", 800, 200);
        }
    }

    private class InputField extends JPanel {

        public InputField(String labelTxt, Consumer<String> returnText){
            setLayout(new GridBagLayout());
            JTextField input = new JTextField("", 30);
            JLabel label = new JLabel(labelTxt);

            g.addElementToGrid(this, label, 0, 0, 1, 1, new Insets(0, 0, 20, 0), GridBagConstraints.WEST);
            g.addElementToGrid(this, input, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.LINE_START, GridBagConstants.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            g.setWhiteBg(this);
            g.setTxtCol(label, input);

            label.setFont(label.getFont().deriveFont(38f));
            label.setBorder(new EmptyBorder(0, 0, 3, 0));

            input.setFont(input.getFont().deriveFont(24f));
            input.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e){
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(30, 30, 30)));
                }

                @Override
                public void focusLost(FocusEvent e){
                    label.setBorder(new EmptyBorder(0, 0, 3, 0));
                    returnText.accept(input.getText());
                }
            });
        }
    }
}
