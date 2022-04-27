package Mechanic;

import Libraries.Methods;

import javax.swing.*;
import java.awt.*;

import static Mechanic.MainVariables.*;

public class NNVPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        Font font = Methods.getFont(25);
        FontMetrics metrics = g.getFontMetrics(font);
        int width;
        int height = metrics.getHeight();

        g2D.setFont(Methods.getFont(25));

        for (int layer = 0; layer < Neurons.size(); layer++) {
            for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                int totalHeight = Neurons.get(layer).size()*200;
                g2D.drawOval(layer*200+200 + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*200 + nnvYDisplacement, 100, 100);
                if (Neurons.get(layer).get(neuron) != null) {
                    String label = String.valueOf(Math.round(Neurons.get(layer).get(neuron) * 10000.0) / 10000.0);
                    width = metrics.stringWidth(label);
                    g2D.drawString(label, layer*200+200+50-width/2 + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*200+50+height/4 + nnvYDisplacement);
                    g2D.fillRect(layer*200+200 + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*200-40 + nnvYDisplacement, (int) (Neurons.get(layer).get(neuron)*100), 20);
                    g2D.drawRect(layer*200+200 + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*200-40 + nnvYDisplacement, 100, 20);
                }
            }
        }
    }
}
