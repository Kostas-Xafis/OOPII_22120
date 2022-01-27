package components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

import Perceptrons.PerceptronMiddleTraveller;
import Perceptrons.PerceptronTraveller;
import Perceptrons.PerceptronYoungTraveller;
import main.City;
import main.Logs;


/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 23-1-2022**/
public class Recommendations extends JPanel {

    private static Globals g = new Globals();

    private Integer age = null;
    private String caseStr = "Default";
    private CheckBox sort = null;

    public Recommendations(){
        setLayout(new BorderLayout());

        // JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel mainContainer = new JPanel(new GridBagLayout());
        JPanel inputsContainer = new JPanel(new GridBagLayout());
        JPanel recommendationsContainer = new JPanel(new GridBagLayout());

        g.addElementToGrid(mainContainer, inputsContainer, 0, 0, 1, 1, GridBagConstants.CENTER, GridBagConstants.HORIZONTAL, 1, 1);
        g.addElementToGrid(mainContainer, recommendationsContainer, 1, 0, 1, 1, GridBagConstants.WEST, GridBagConstants.BOTH, 1, 1);
        add(mainContainer, BorderLayout.CENTER);
        g.setWhiteBg(this, mainContainer, inputsContainer, recommendationsContainer);


        { //Recommnedations input fields
            JPanel ageMainContainer = new JPanel(new GridBagLayout());
            JPanel ageContainer1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel ageContainer2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel ageLabel = new JLabel("Specify your age:");
            JTextField ageInput = new JTextField("", 2);

            JPanel caseMainContainer = new JPanel(new GridBagLayout());
            JPanel letterCasing1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel letterCasing2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel caseLabel = new JLabel("Letter case:");
            JComboBox<String> caseInput = new JComboBox<String>(new String[]{"Default", "Lower", "Upper"});

            JPanel sortMainContainer = new JPanel(new GridBagLayout());
            JPanel sortContainer1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel sortContainer2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel sortLabel = new JLabel("Sort cities:");
            CheckBox sortInput = new CheckBox(18);
            sort = sortInput;

            ageContainer1.add(ageLabel);
            ageContainer2.add(ageInput);
            g.addElementToGrid(ageMainContainer, ageContainer1, 0, 0, 1, 1, GridBagConstants.WEST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(ageMainContainer, ageContainer2, 1, 0, 1, 1, new Insets(0, 20, 0, 0), GridBagConstants.EAST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(inputsContainer, ageMainContainer, 0, 0, 1, 1, new Insets(0, 0, 50, 0), GridBagConstants.WEST, GridBagConstants.HORIZONTAL);


            letterCasing1.add(caseLabel);
            letterCasing2.add(caseInput);
            g.addElementToGrid(caseMainContainer, letterCasing1, 0, 0, 1, 1, GridBagConstants.WEST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(caseMainContainer, letterCasing2, 1, 0, 1, 1, GridBagConstants.EAST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(inputsContainer, caseMainContainer, 0, 1, 1, 1, new Insets(0, 0, 50, 0), GridBagConstants.WEST, GridBagConstants.HORIZONTAL);

            sortContainer1.add(sortLabel);
            sortContainer2.add(sortInput);
            g.addElementToGrid(sortMainContainer, sortContainer1, 0, 0, 1, 1, GridBagConstants.WEST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(sortMainContainer, sortContainer2, 1, 0, 1, 1, GridBagConstants.EAST, GridBagConstants.HORIZONTAL, 1, 1);
            g.addElementToGrid(inputsContainer, sortMainContainer, 0, 2, 1, 1, new Insets(0, 0, 50, 0), GridBagConstants.WEST, GridBagConstants.HORIZONTAL);


            g.setWhiteBg(ageMainContainer, ageContainer1,  ageContainer2, ageInput, caseMainContainer, letterCasing1, letterCasing2,
                         caseInput, sortMainContainer, sortContainer1, sortContainer2, sortLabel, sortInput);
            g.setTxtCol(ageLabel, ageInput, caseLabel, caseInput, sortLabel, sortInput);

            ageLabel.setFont(ageLabel.getFont().deriveFont(24f));
            ageInput.setFont(ageInput.getFont().deriveFont(20f));
            ageInput.setHorizontalAlignment(SwingConstants.CENTER);

            caseLabel.setFont(caseLabel.getFont().deriveFont(24f));
            caseInput.setFont(caseInput.getFont().deriveFont(20f));

            sortLabel.setFont(sortLabel.getFont().deriveFont(24f));

            ageInput.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    age = Integer.parseInt(ageInput.getText());
                }
            });
            caseInput.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    caseStr = (String) caseInput.getSelectedItem();
                }
            });
        }
        {
            JLabel title = new JLabel("Recommendations List:");
            JPanel recommendationsList = new RecommendationsList(new ArrayList<City>());
            g.addElementToGrid(recommendationsContainer, title, 0, 0, 1, 1, GridBagConstants.NORTH, GridBagConstants.HORIZONTAL, 1, 0);
            g.addElementToGrid(recommendationsContainer, recommendationsList, 0, 1, 1, 1, GridBagConstants.NORTH, GridBagConstants.HORIZONTAL, 1, 1);

            title.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
            title.setFont(title.getFont().deriveFont(28f));
        }
        {
            JPanel upperContainer = new JPanel(new GridLayout());
            add(upperContainer, BorderLayout.SOUTH);
            BackButton backButton = new BackButton("images/leftArrow.svg", (i) -> {
                JComponent mainPanel = (JComponent) getParent();
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "HOME");
            });
            upperContainer.add(backButton);

            { // Right-aligned "Save" button
                JPanel alignContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JPanel buttonContainer = new JPanel(new GridBagLayout());
                JLabel buttonTxt = new JLabel("Get recommendations");

                g.addElementToGrid(buttonContainer, buttonTxt, 0, 0, 1, 1, new Insets(5, 5, 5, 5));
                alignContainer.add(buttonContainer);
                upperContainer.add(alignContainer);

                g.setWhiteBg(upperContainer, alignContainer, buttonContainer);

                alignContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 75));
                buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));

                buttonTxt.setFont(buttonTxt.getFont().deriveFont(26f));
                g.setTxtCol(buttonTxt);

                buttonContainer.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e){
                        buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));
                        buttonContainer.setBackground(g.txtCol);
                        buttonTxt.setForeground(g.white);
                    }

                    public void mouseExited(MouseEvent e){
                        buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));
                        g.setWhiteBg(buttonContainer);
                        g.setTxtCol(buttonTxt);
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
        if(age == null){
            Logs.info("User requested for recommendations with empty age field");
            new InvalidFrame("Invalid input", "Empty age field:", "Specify your age to get recommendations", 500, 300);
            return;
        }

        if( 16 <= this.age.intValue() && this.age.intValue() <= 115) {
            //API CALL HERE
            int age = this.age.intValue();
            PerceptronTraveller perceptron = null;
            City athens = City.getCities().get("Athens_gr");
            ArrayList<City> recommendations = null;

            if(age <= 25) perceptron = new PerceptronYoungTraveller(athens);
            else if(age <= 60) perceptron = new PerceptronMiddleTraveller(athens);
            else perceptron = new PerceptronMiddleTraveller(athens);

            UserPreferences pref = (UserPreferences) getParent().getComponent(2);
            if(pref.hasValidInputs) {
                Collection<City> cities = City.Cities.values();
                perceptron.setBiases(pref.getBiases());

                City personalizedRecommend = null;
                if(caseStr.equals("Default")) personalizedRecommend = perceptron.personalizedRecommend(cities);
                else if(caseStr.equals("Upper")) personalizedRecommend = perceptron.personalizedRecommend(cities, true);
                else personalizedRecommend = perceptron.personalizedRecommend(cities, false);

                if(personalizedRecommend != null) {
                    recommendations = new ArrayList<>();
                    recommendations.add(personalizedRecommend);
                    Logs.info("User requested for a personalized recommendation.");
                }
            } else {

                if(caseStr.equals("Default")) perceptron.recommend();
                else if(caseStr.equals("Upper")) perceptron.recommend(true);
                else perceptron.recommend(false);


                if(sort.state == true) recommendations = perceptron.sortRecommendations();
                else recommendations = perceptron.getRecommendations();

                if(recommendations != null) {
                    Logs.info("User requested for recommendations.");
                }
            }

            if(recommendations == null) {
                Logs.warning("No recommendations could be found for the user.");
                new InvalidFrame("Recommendations", "Recommendation error: ", "Could not find a recommendation.", 500, 300);
                return;
            }

            JPanel recommendationsContainer = (JPanel)((JComponent) this.getComponent(0)).getComponent(1);
            recommendationsContainer.remove(1);
            g.addElementToGrid(recommendationsContainer, new RecommendationsList(recommendations), 0, 1, 1, 1, GridBagConstants.NORTH, GridBagConstants.HORIZONTAL, 1, 1);
            repaint();
            revalidate();
        } else {
            Logs.info("User tried to get recommendations with invalid age value.");
            new InvalidFrame("Invalid input", "Wrong age:", "You must be between 16-115 years old", 500, 300);
        }
    }

    private class RecommendationsList extends JPanel {

        public RecommendationsList(ArrayList<City> cities){
            setLayout(new GridBagLayout());
            for (int i = 0; i < cities.size(); i++) {
                City city = cities.get(i);
                JLabel cityLabel = new JLabel(city.getName() + ", " + city.getInitials());

                g.addElementToGrid(this, cityLabel, (int) (i/5), i%5, 1, 1, GridBagConstants.NORTH, GridBagConstants.BOTH, 1, 1);
                cityLabel.setFont(cityLabel.getFont().deriveFont(24f));
                cityLabel.setBorder(BorderFactory.createEmptyBorder(24, 50, 0, 0));
            }
            g.setWhiteBg(this);
        }
    }

    private class CheckBox extends JPanel{

        private boolean state = false;

        public CheckBox(int fontSize) {
            setLayout(new GridBagLayout());
            JPanel svgPadding = new JPanel();
            JSVGCanvas svg = new JSVGCanvas();

            g.addElementToGrid(this, svgPadding, 0, 0, 1, 1);
            svgPadding.add(svg);

            g.setWhiteBg(svgPadding);

            svg.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
            String s = new File("images/check.svg").toString();
            svg.setMySize(new Dimension(fontSize, fontSize));
            svg.setBackground(new Color(255, 255, 255));
            svg.setURI(s);

            svgPadding.setBorder(BorderFactory.createLineBorder(g.txtCol, 1));

            MouseAdapter mouseListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    state = !state;
                    if(state == true){
                        SVGDocument doc = svg.getSVGDocument();
                        doc.getElementById("check").setAttribute("fill", "rgb(30, 30, 30)");
                        svg.setSVGDocument(doc);
                    } else {
                        SVGDocument doc = svg.getSVGDocument();
                        doc.getElementById("check").setAttribute("fill", "rgb(255, 255, 255)");
                        svg.setSVGDocument(doc);
                    }
                }
            };

            svg.addMouseListener(mouseListener);
            svgPadding.addMouseListener(mouseListener);
        }
    }
}
