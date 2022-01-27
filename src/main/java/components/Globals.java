package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 21-1-2022**/
public class Globals {

    public Color txtCol = new Color(30, 30, 30);
    public Color white  = new Color(255, 255, 255);

    public Globals(){}

    public void addElementToGrid(JPanel p, JComponent e, int x, int y, int spanx, int spany, int... aligning){
        GridBagConstraints c = aligning.length == 0 ? new GridBagConstraints() : setGridAlignments(aligning);
        c.gridx = x;
        c.gridy = y;
        c.gridheight = spany;
        c.gridwidth = spanx;
        p.add(e, c);
    }

    public void addElementToGrid(JPanel p, JComponent e, int x, int y, int spanx, int spany, Insets i, int... aligning){
        GridBagConstraints c = aligning.length == 0 ? new GridBagConstraints() : setGridAlignments(aligning);
        c.gridx = x;
        c.gridy = y;
        c.gridheight = spany;
        c.gridwidth = spanx;
        c.insets = i;
        p.add(e, c);
    }

    private GridBagConstraints setGridAlignments(int... aligning){
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < aligning.length; i++) {
            switch (i) {
                case 0:
                    c.anchor = aligning[0];
                    break;
                case 1:
                    c.fill = aligning[1];
                    break;
                case 2:
                    c.weightx = aligning[2];
                    break;
                case 3:
                    c.weighty = aligning[3];
                    break;
                default:
                    break;
            }
        }
        return c;
    }

    public void addElementToGrid(JPanel p, JComponent e, GridBagConstraints c){
        p.add(e, c);
    }

    public void setTxtCol(JComponent... c) {
        for (int i = 0; i < c.length; i++) {
            c[i].setForeground(txtCol);
        }
    }

    public Dimension getWindowSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public void setWhiteBg(JComponent... c){
        for (int i = 0; i < c.length; i++) {
            c[i].setBackground(white);
        }
    }
}
