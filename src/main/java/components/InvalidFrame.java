package components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 23-1-2022**/
public class InvalidFrame extends JFrame {

    private static Globals g = new Globals();

    public InvalidFrame(String windowTitle, String title, String[] content, int width, int height){
        setSize(width, height);
        setTitle(windowTitle);
        setLocation(g.getWindowSize().width / 2 - width / 2,  g.getWindowSize().height / 2 - height / 2);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel alignPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel msgTitle = new JLabel(title);

        JPanel alignPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        FrameCloseButton exitButton = new FrameCloseButton("OK", (i) -> {
            setVisible(false);
            dispose();
        });

        alignPanel1.add(msgTitle);
        mainPanel.add(alignPanel1, BorderLayout.PAGE_START);
        mainPanel.add(alignPanel2, BorderLayout.CENTER);

        for (int i = 0; i < content.length; i++) {
            if(content[i] == null) continue;
            JLabel msgContent = new JLabel(content[i]);
            alignPanel2.add(msgContent);
            g.setTxtCol(msgContent);
            msgContent.setFont(msgContent.getFont().deriveFont(18f));
            msgContent.setBorder(new EmptyBorder(10, 40, 0, 0));
        }

        g.setWhiteBg(mainPanel, alignPanel1, alignPanel2);
        g.setTxtCol(msgTitle);

        msgTitle.setFont(msgTitle.getFont().deriveFont(20f));
        msgTitle.setBorder(new EmptyBorder(10, 15, 0, 0));


        mainPanel.add(exitButton, BorderLayout.PAGE_END);
        add(mainPanel);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    public InvalidFrame(String windowTitle, String title, String content, int width, int height){
        this(windowTitle, title, new String[]{content}, width, height);
    }
}
