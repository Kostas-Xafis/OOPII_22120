package components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.batik.ext.swing.GridBagConstants;

import main.Logs;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 19-1-2022**/
public class UserPreferences extends JPanel {

    private static Globals g = new Globals();

    private HashMap<String, Integer> features = new HashMap<>();
    private Integer[] biases = (Integer[]) Array.newInstance(Integer.class, 7);
    public boolean hasValidInputs = false;
    private String[] strFeatures = new String[]{"cafe", "sea", "museum", "restaurant", "stadium", "culture", "transport"};
    private String[] screenFeat = new String[]{"Bistro", "Sea", "Museum", "Restaurant", "Stadium", "Culture", "Transport"};



    public UserPreferences() {
        loadFeatures();
        setLayout(new BorderLayout());
        { //Title
            JPanel labelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel title = new JLabel("User Preferences:");
            labelContainer.add(title);
            add(labelContainer, BorderLayout.PAGE_START);

            title.setFont(title.getFont().deriveFont(38f));
            title.setBorder(new EmptyBorder(25, 50, 0, 0));

            g.setWhiteBg(this, labelContainer);
        }
        {
            JPanel alignContainer = new JPanel(new GridBagLayout());
            JLabel rateLabel = new JLabel("Rate these trip factors from 0-10");
            JPanel fieldsContainer = new JPanel(new GridBagLayout());
            InputField[] fields = (InputField[]) Array.newInstance(InputField.class, strFeatures.length);
            for(int i = 0; i < strFeatures.length; i++){
                String feat = strFeatures[i];
                fields[i] = new InputField(screenFeat[i] + ":", (k) -> {
                    features.put(feat, k);
                    setBias(feat, k);
                });
                g.addElementToGrid(fieldsContainer, fields[i], (int) (i/4), i % 4, 1, 1, GridBagConstants.WEST, GridBagConstants.HORIZONTAL, 1, 1);
            }

            g.addElementToGrid(alignContainer, rateLabel, 0, 0, 1, 1, new Insets(20,0,20,0), GridBagConstants.CENTER, GridBagConstants.HORIZONTAL);
            g.addElementToGrid(alignContainer, fieldsContainer, 0, 1, 1, 1, GridBagConstants.CENTER, GridBagConstants.HORIZONTAL);
            add(alignContainer, BorderLayout.CENTER);

            //Window Height / 2 - (25px Title pad - 38px font size) - (75px Button pad - 5px inset pad - 4 border height - 32px font size) - 10px actual padding
            int alignContainerPad = (int) (-1) * ((g.getWindowSize().height / 2) - 63 - 116 - 10);
            alignContainer.setBorder(new EmptyBorder(alignContainerPad, 25, 0, 0));
            g.setWhiteBg(fieldsContainer, alignContainer);

            g.setTxtCol(rateLabel);
            rateLabel.setFont(rateLabel.getFont().deriveFont(28f));
            rateLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
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
                JLabel buttonTxt = new JLabel("Save");

                g.addElementToGrid(buttonContainer, buttonTxt, 0, 0, 1, 1, new Insets(5, 5, 5, 5));
                alignContainer.add(buttonContainer);
                upperContainer.add(alignContainer);

                g.setWhiteBg(upperContainer, alignContainer, buttonContainer);

                alignContainer.setBorder(new EmptyBorder(new Insets(0, 0, 50, 75)));
                buttonContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));

                buttonTxt.setFont(buttonTxt.getFont().deriveFont(32f));
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
                        validateFeatures();
                    }
                });
            }
        }
    }

    private void loadFeatures(){
        for (int i = 0; i < strFeatures.length; i++) {
            features.put(strFeatures[i], null);
        }
    }

    private void validateFeatures(){
        int size = features.size();
        boolean isFaulty = false;
        String faultyFields = "";
        for (int i = 0; i < size; i++) {
            if(features.get(strFeatures[i]) == null){
                isFaulty = true;
                faultyFields += "<li>" + screenFeat[i] + "</li>";
            }
        }
        if(isFaulty){
            hasValidInputs = false;
            Logs.info("User gave invalid input at the UserPreferences fields.");
            new InvalidFrame("Invalid input", "These factors are not correctly rated:", "<html><ul>" + faultyFields + "</ul></html>", 500, 350);
        } else{
            hasValidInputs = true;
            Logs.info("User gave valid input at the UserPreferences fields.");
            new ValidFrame("Saved !", 400, 200);
        }
    }


    private void setBias(String feat, Integer k){
        for(int i = 0; i < screenFeat.length; i++){
            if(strFeatures[i].equals(feat)){
                biases[i] = k;
                return;
            }
        }
    }

    public Integer[] getBiases(){
        return biases;
    }

    private class InputField extends JPanel{

        public InputField(String labelTxt, Consumer<Integer> score){
            setLayout(new GridBagLayout());
            JPanel alignContainer1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel alignContainer2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel label = new JLabel(labelTxt);
            JTextField input = new JTextField("", 2);

            alignContainer1.add(label);
            alignContainer2.add(input);
            g.addElementToGrid(this, alignContainer1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstants.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            g.addElementToGrid(this, alignContainer2, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstants.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            g.setWhiteBg(this, alignContainer1, alignContainer2);
            g.setTxtCol(label, input);

            label.setFont(label.getFont().deriveFont(24f));

            input.setFont(input.getFont().deriveFont(18f));
            input.setHorizontalAlignment(JTextField.CENTER);

            input.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    Integer i = Integer.parseInt(input.getText());

                    if(0 <= i.intValue() && i.intValue() <= 10){
                        score.accept(i);
                    } else
                        score.accept(null);
                }
            });
        }

    }
}
