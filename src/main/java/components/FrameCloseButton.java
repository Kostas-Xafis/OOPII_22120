package components;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 23-1-2022**/
public class FrameCloseButton extends JPanel {

    private static Globals g = new Globals();

    public FrameCloseButton(String btnTxt, Consumer<Integer> onClickFunc){
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel txtContainer = new JPanel(new GridBagLayout());
        JLabel txt = new JLabel(btnTxt);

        g.addElementToGrid(txtContainer, txt, 0, 0, 1, 1);
        add(txtContainer);

        g.setWhiteBg(txtContainer, this);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 15));

        txt.setFont(txt.getFont().deriveFont(20f));
        g.setTxtCol(txt);
        txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(g.txtCol, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        txt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                txtContainer.setBackground(g.txtCol);
                txt.setForeground(g.white);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                g.setTxtCol(txt);
                g.setWhiteBg(txtContainer);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickFunc.accept(0);
            }
        });
    }
}
