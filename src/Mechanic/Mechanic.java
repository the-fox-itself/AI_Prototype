package Mechanic;

import java.awt.*;
import java.io.IOException;
import java.util.Vector;

import static Libraries.Methods.*;

public class Mechanic extends MainVariables {
    void preparation() {
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, FONT_SPEAK_HEAVY_TEXT));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        Neurons.setSize(layers.length);                 //Full Neurons list setup
        for (int i = 0; i < layers.length; i++) {
            Neurons.set(i, new Vector<>());
            Neurons.get(i).setSize(layers[i]);
        }
        for (int i = 0; i < inputs; i++) {
            Neurons.get(0).set(i, 0.0);
        }

        Synapses.setSize(layers.length-1);              //Full Synapses list setup
        SynapseDerivatives.setSize(layers.length-1);
        for (int i = 0; i < layers.length-1; i++) {
            Synapses.set(i, new Vector<>());
            Synapses.get(i).setSize(Neurons.get(i).size());
            SynapseDerivatives.set(i, new Vector<>());
            SynapseDerivatives.get(i).setSize(Neurons.get(i).size());
            for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                Synapses.get(i).set(i1, new Vector<>());
                Synapses.get(i).get(i1).setSize(Neurons.get(i+1).size());
                SynapseDerivatives.get(i).set(i1, new Vector<>());
                SynapseDerivatives.get(i).get(i1).setSize(Neurons.get(i+1).size());
            }
        }

        Biases.setSize(layers.length);              //Full Biases list setup
        for (int i = 1; i < layers.length; i++) {
            Biases.set(i, new Vector<>());
            Biases.get(i).setSize(layers[i]);
        }

        randomInitialize();


        frame.add(drawPanel);
        drawPanel.setBounds(frame.getWidth()/2-28*25/2, frame.getHeight()/2-37-28*25/2, 28*25, 28*25);

        frame.addKeyListener(new DrawKeyListener());
        drawPanel.addMouseListener(new DrawMouseListener());
        drawPanel.addMouseMotionListener(new DrawMouseMotionListener());

        visTrue(frame);


        nnvFrame.add(nnvPanel);
        nnvPanel.setBounds(0, 0, nnvFrame.getWidth(), nnvFrame.getHeight());

        nnvFrame.addMouseListener(new NNVMouseListener());
        nnvFrame.addMouseMotionListener(new NNVMouseMotionListener());
        nnvFrame.addMouseWheelListener(new NNVMouseWheelListener());

        visTrue(nnvFrame);

        gameLoop.start();
        gameLoopOn = true;
    }

    static void randomInitialize() {
        for (int i = 0; i < Synapses.size(); i++) {
            for (int i1 = 0; i1 < Synapses.get(i).size(); i1++) {
                for (int i2 = 0; i2 < Synapses.get(i).get(i1).size(); i2++) {
                    Synapses.get(i).get(i1).set(i2, Math.random()*2-1);
                }
            }
        }
        for (int i = 1; i < Biases.size(); i++) {
            for (int i1 = 0; i1 < Biases.get(i).size(); i1++) {
                Biases.get(i).set(i1, Math.random());
            }
        }
        System.out.println(Synapses);
        System.out.println(Biases);
    }

    static double sigmoid(double x) {
        return 1/(1+Math.pow(Math.E, -x));
    }
    static double sigmoid_derivative(double x) {
        return sigmoid(x)*(1-sigmoid(x));
    }
    static double ReLU(double x) {
        return Math.max(0, x);
    }

    static void neuralNetwork() {
        if (Neurons.size() > 1) {
            for (int i = 1; i < Neurons.size(); i++) {
                for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                    neuronActivationCalculation(i, i1);
                    System.out.println(Neurons.get(i).get(i1));
                }
                System.out.println();
            }
        }
        double highestValue = 0;
        int result = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            if (Neurons.get(Neurons.size()-1).get(i) > highestValue) {
                highestValue = Neurons.get(Neurons.size()-1).get(i);
                result = i;
            }
            System.out.println(i + ": " + Neurons.get(Neurons.size()-1).get(i));
        }
        System.out.println("Final guess: " + result + "\n");
    }
    static void neuronActivationCalculation(int layer, int index) {
        if (layer != 0) {
            int numberOfInputs = Neurons.get(layer - 1).size();
            double sum = 0;
            for (int i = 0; i < numberOfInputs; i++) {
                sum += Synapses.get(layer - 1).get(i).get(index) * Neurons.get(layer - 1).get(i);
            }
            sum -= Biases.get(layer).get(index);
            sum = sigmoid(sum);
            Neurons.get(layer).set(index, sum);
        }
    }

    static double costFunction() {
        double cost = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            cost += Math.pow(Neurons.get(Neurons.size()-1).get(i)-perfectOutput.get(i), 2);
        }
        return cost;    //0.0 = Perfect     10.0 = Wrong
    }

    static void backpropagation() {
        double totalCost = costFunction();

        Vector<Vector<Double>> NeuronDerivatives = new Vector<>();
        NeuronDerivatives.setSize(layers.length-1);     //NeuronDerivatives list setup without the last layer
        for (int i = 0; i < layers.length-1; i++) {
            NeuronDerivatives.set(i, new Vector<>());
            NeuronDerivatives.get(i).setSize(layers[i]);
        }

        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {     //Calculating Synapse derivatives of the last layer
            for (int i1 = 0; i1 < Neurons.get(Neurons.size()-2).size(); i1++) {
                double previous_neuron = Neurons.get(Neurons.size()-2).get(i1);
                double current_neuron = Neurons.get(Neurons.size()-1).get(i);
                double synapse = Synapses.get(Neurons.size()-2).get(i1).get(i);
                double synapse_derivative = previous_neuron * sigmoid_derivative(synapse * previous_neuron) * 2 * (current_neuron - perfectOutput.get(i));
                SynapseDerivatives.get(Neurons.size()-2).get(i1).set(i, synapse_derivative);
            }
        }

//        for (int i = )
    }

    static void setPerfectOutput(int correctResult) {
        for (int i = 0; i < 10; i++) {
            if (i == correctResult)
                perfectOutput.set(i, 1.0);
            else
                perfectOutput.set(i, 0.0);
        }
    }
}
