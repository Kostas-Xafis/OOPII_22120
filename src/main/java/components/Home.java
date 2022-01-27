package components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;


/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 20-1-2022**/
public class Home extends JPanel {

    private static Globals g = new Globals();

    public Home(){
        setLayout(new BorderLayout());
        setSize(this.getWidth(), this.getHeight());
        { //Add user preferences svg in PAGE_START
            JPanel prefContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JSVGCanvas svgPref = new JSVGCanvas();
            g.setWhiteBg(this, prefContainer);
            prefContainer.add(svgPref);
            add(prefContainer, BorderLayout.PAGE_START);

            prefContainer.setBorder(new EmptyBorder(5, 10, 0, 0));

            svgPref.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
            String s = new File("images/userCog.svg").toString();
            svgPref.setMySize(new Dimension(50, 50));
            svgPref.setBackground(new Color(255, 255, 255));
            svgPref.setURI(s);
            svgPref.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e){
                    SVGDocument doc = svgPref.getSVGDocument();
                    doc.getElementById("userCog").setAttribute("fill", "rgb(100, 100, 100)");
                    svgPref.setSVGDocument(doc);
                }

                @Override
                public void mouseExited(MouseEvent e){
                    SVGDocument doc = svgPref.getSVGDocument();
                    doc.getElementById("userCog").setAttribute("fill", "rgb(30, 30, 30)");
                    svgPref.setSVGDocument(doc);
                }

                @Override
                public void mouseClicked(MouseEvent e){
                    JComponent mainPanel = (JComponent) getParent();
                    ((CardLayout) mainPanel.getLayout()).show(mainPanel, "USER_PREFERENCES");
                }
            });
        }
        {
            JPanel mainContainer = new JPanel(new GridBagLayout());
            JLabel name = new JLabel("Travelling Agency");
            JPanel buttonsContainer = new JPanel(new GridBagLayout());
            HomeButton leftButton = new HomeButton("Add a City", "images/leftArrow.svg", "ADD_CITY", true);
            HomeButton rightButton = new HomeButton("Get a recommendation", "images/rightArrow.svg", "RECOMMENDATIONS", false);


            g.addElementToGrid(mainContainer, name, 0, 0, 1, 2, new Insets(0, 0, 100, 0), GridBagConstants.CENTER);
            g.addElementToGrid(mainContainer, buttonsContainer, 0, 2, 1, 3, new Insets(0, 0, 50, 0));
            g.addElementToGrid(buttonsContainer, leftButton , 0, 0, 1, 1, new Insets(0, 0, 0, 15));
            g.addElementToGrid(buttonsContainer, rightButton, 1, 0, 1, 1, new Insets(0, 15, 0, 0));
            add(mainContainer, BorderLayout.CENTER);

            mainContainer.setSize(new Dimension((int) g.getWindowSize().getWidth(), (int) g.getWindowSize().getHeight() - 50));

            g.setTxtCol(name);
            name.setFont(name.getFont().deriveFont(48f));

            g.setWhiteBg(mainContainer, buttonsContainer);
        }
    }

    private class HomeButton extends JPanel {

        public HomeButton(String labelTxt, String svgFile, String cardName, boolean isLeft){
            setLayout(new GridBagLayout());
            JSVGCanvas arrow = new JSVGCanvas();
            JLabel label = new JLabel(labelTxt);
            g.addElementToGrid(this, label, isLeft ? 1 : 0, 0, 3, 1, new Insets(4, 4, 4, 4));
            g.addElementToGrid(this, arrow, isLeft ? 0 : 3, 0, 1, 1, new Insets(4, 4, 4, 4));

            g.setWhiteBg(this);
            setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));

            arrow.setMySize(new Dimension(24, 24));
            arrow.setURI(new File(svgFile).toString());
            arrow.setBackground(new Color(0, 0, 0, 0));

            label.setFont(label.getFont().deriveFont(24f));
            g.setTxtCol(label);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));
                }
                @Override
                public void mouseExited(MouseEvent e){
                    setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    JComponent mainPanel = (JComponent) getParent().getParent().getParent().getParent();
                    ((CardLayout) mainPanel.getLayout()).show(mainPanel, cardName);
                }
            });
        }
    }

}
