package Mechanic;

import java.util.Vector;

import static Libraries.Methods.*;

public class Mechanic extends MainVariables {
    void preparation() {
        Neurons.setSize(4);
        Neurons.set(0, new Vector<>());
        Neurons.set(1, new Vector<>());
        Neurons.set(2, new Vector<>());
        Neurons.set(3, new Vector<>());
        Neurons.get(0).setSize(784);
        Neurons.get(1).setSize(16);
        Neurons.get(2).setSize(16);
        Neurons.get(3).setSize(10);
        for (int i = 0; i < 784; i++) {
            Neurons.get(0).set(i, 0.0);
        }

        frame.add(drawPanel);
        drawPanel.setBounds(frame.getWidth()/2-28*25/2, frame.getHeight()/2-37-28*25/2, 28*25, 28*25);

        drawPanel.addMouseListener(new DrawMouseListener());
        drawPanel.addMouseMotionListener(new DrawMouseMotionListener());

        visTrue(frame);

        gameLoop.start();
        gameLoopOn = true;
    }

    void randomInitialize() {

    }

    double sigmoid(double x) {
        return 1/(1+Math.pow(Math.E, -x));
    }
    double ReLU(double x) {
        return Math.max(0, x);
    }

    void neuralNetwork() {

    }
    void neuronActivationCalculation(int layer, int index) {
        if (layer != 0) {
            int numberOfInputs = Neurons.get(layer - 1).size();
            double sum = 0;
            for (int i = 0; i < numberOfInputs; i++) {
                sum += Synapses.get(layer - 1).get(i).get(index) * Neurons.get(layer - 1).get(i);
            }
            sum += Biases.get(layer).get(index);
            sum = sigmoid(sum);
            Neurons.get(layer).set(index, sum);
        }
    }
}
