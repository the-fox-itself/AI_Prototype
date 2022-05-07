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

        int fontGeneral = 25;
        int fontNeuralNetworkResult = 50;
        int neuronHeight = 100;
        int neuronWidth = 100;
        int verticalDistanceBetweenNeurons = 100;
        int horizontalDistanceBetweenNeurons = 100;
        int dxLeftSide = 200;
        int roundingDecimalPlaces = 4;
        int valueBarHeight = 20;
        int resultsDistance = 200;
        Color chosenResultColor = new Color(0x00EA00);

        int dx = horizontalDistanceBetweenNeurons+neuronWidth;
        int dy = verticalDistanceBetweenNeurons+neuronHeight;

        Font font = Methods.getFont(fontGeneral);
        Font fontResult = Methods.getFont(fontNeuralNetworkResult);
        FontMetrics metrics = g.getFontMetrics(font);
        FontMetrics metricsResult = g.getFontMetrics(fontResult);
        int width;
        int height = metrics.getHeight();
        int heightResult = metricsResult.getHeight();

        g2D.setFont(Methods.getFont(fontGeneral));

        for (int layer = 0; layer < Neurons.size(); layer++) {  //Neurons list display
            for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                int totalHeight = Neurons.get(layer).size()*dy;
                g2D.drawOval(layer*dx+dxLeftSide + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy + nnvYDisplacement, neuronWidth, neuronHeight);
                if (Neurons.get(layer).get(neuron) != null) {
                    String label = String.valueOf(Math.round(Neurons.get(layer).get(neuron) * Math.pow(10, roundingDecimalPlaces)) / Math.pow(10, roundingDecimalPlaces));
                    width = metrics.stringWidth(label);
                    g2D.drawString(label, layer*dx+dxLeftSide+neuronWidth/2-width/2 + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy+neuronHeight/2+height/4 + nnvYDisplacement);
                    g2D.fillRect(layer*dx+dxLeftSide + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy-valueBarHeight*2 + nnvYDisplacement, (int) (Neurons.get(layer).get(neuron)*neuronWidth), valueBarHeight);
                    g2D.drawRect(layer*dx+dxLeftSide + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy-valueBarHeight*2 + nnvYDisplacement, neuronWidth, valueBarHeight);
                    if (layer == Neurons.size()-1) {
                        if (neuron == neuralNetworkAnswer) {
                            g2D.setColor(chosenResultColor);
                            g2D.setFont(Methods.getFont(fontNeuralNetworkResult));
                            g2D.drawString(String.valueOf(neuron), layer*dx+dxLeftSide + nnvXDisplacement + resultsDistance, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy+neuronHeight/2+heightResult/4 + nnvYDisplacement);
                            g2D.setFont(Methods.getFont(fontGeneral));
                            g2D.setColor(Color.BLACK);
                        } else {
                            g2D.drawString(String.valueOf(neuron), layer*dx+dxLeftSide + nnvXDisplacement + resultsDistance, nnvFrame.getHeight()/2-totalHeight/2+neuron*dy+neuronHeight/2+height/4 + nnvYDisplacement);
                        }
                    }
                }
            }
        }

        for (int layer = 0; layer < Synapses.size(); layer++) {    //Synapses list display
            for (int startNeuron = 0; startNeuron < Synapses.get(layer).size(); startNeuron++) {
                for (int endNeuron = 0; endNeuron < Synapses.get(layer).get(startNeuron).size(); endNeuron++) {
                    int totalHeight = Neurons.get(layer).size()*dy;
                    int totalHeightNextLayer = Neurons.get(layer+1).size()*dy;
                    double synapseValue = Synapses.get(layer).get(startNeuron).get(endNeuron);
//                    if (synapseValue < 0) {
//                        g2D.setColor(new Color(0, 193, 255, (int) (Math.abs(synapseValue)/2*100)));
//                    } else {
//                        g2D.setColor(new Color(255, 105, 105, (int) (Math.abs(synapseValue)/2*100)));
//                    }
                    g2D.drawLine(layer*dx+dxLeftSide + neuronWidth + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeight/2+startNeuron*dy + neuronHeight/2 + nnvYDisplacement, (layer+1)*dx+dxLeftSide + nnvXDisplacement, nnvFrame.getHeight()/2-totalHeightNextLayer/2+endNeuron*dy + neuronHeight/2 + nnvYDisplacement);
                    g2D.setColor(Color.BLACK);
                }
            }
        }
    }
}
