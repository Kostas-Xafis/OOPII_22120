package components;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 23-1-2021**/
public class ValidFrame extends JFrame {

    private static Globals g = new Globals();

    public ValidFrame(String successMessage, int width, int height){
        setSize(width, height);
        setTitle("Valid input");
        setLocation(g.getWindowSize().width / 2 - width / 2,  g.getWindowSize().height / 2 - height / 2);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel msg = new JLabel(successMessage, SwingConstants.CENTER);
        FrameCloseButton exitButton = new FrameCloseButton("OK", (i) -> {
            setVisible(false);
            dispose();
        });

        mainPanel.add(exitButton, BorderLayout.PAGE_END);
        mainPanel.add(msg, BorderLayout.CENTER);
        g.setWhiteBg(mainPanel);

        msg.setFont(msg.getFont().deriveFont(24f));
        g.setTxtCol(msg);


        add(mainPanel);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}
