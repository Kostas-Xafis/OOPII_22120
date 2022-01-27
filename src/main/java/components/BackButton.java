package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

/**
 *@author Konstantinos Xafis it22120
 *@version 1.0
 *@since 23-1-2022**/
public class BackButton extends JPanel {

    private static Globals g = new Globals();

    public BackButton(String svgFLoc, Consumer<Integer> mouseClickFunc){ // Left-aligned move back button
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel paddingContainer = new JPanel(new GridLayout());
        JSVGCanvas svg = new JSVGCanvas();
        paddingContainer.add(svg);
        this.add(paddingContainer);

        g.setWhiteBg(this);
        this.setBorder(BorderFactory.createEmptyBorder(0, 50, 75, 0));
        g.setWhiteBg(paddingContainer);
        paddingContainer.setBorder(BorderFactory.createLineBorder(g.txtCol, 2, true));

        svg.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        String s = new File(svgFLoc).toString();
        svg.setMySize(new Dimension(32, 32));
        svg.setBackground(new Color(0, 0, 0, 0));
        svg.setURI(s);

        svg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                SVGDocument doc = svg.getSVGDocument();
                doc.getElementById("lArrow").setAttribute("fill", "rgb(200, 82, 88)");
                svg.setSVGDocument(doc);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                SVGDocument doc = svg.getSVGDocument();
                doc.getElementById("lArrow").setAttribute("fill", "rgb(30, 30, 30)");
                svg.setSVGDocument(doc);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickFunc.accept(null);
            }
        });
    }

}
