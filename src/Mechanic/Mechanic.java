package Mechanic;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.Vector;

import static Libraries.Methods.visTrue;

public class Mechanic extends MainVariables {
    void preparation() {
        byte c = 0;
        c += 128;
        System.out.println(c);

        if (!loadNeuralNetwork(neuralNetworkSave)) {
            Neurons.setSize(layers.length);                 //Full Neurons list setup
            for (int i = 0; i < Neurons.size(); i++) {
                Neurons.set(i, new Vector<>());
                Neurons.get(i).setSize(layers[i]);
            }
            Neurons.get(0).replaceAll(ignored -> 0.0);
            NeuronNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                NeuronNudges.set(i, new Vector<>());
                NeuronNudges.get(i).setSize(Neurons.size());
                for (int i1 = 0; i1 < Neurons.size(); i1++) {
                    NeuronNudges.get(i).set(i1, new Vector<>());
                    NeuronNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                }
            }

            zs.setSize(Neurons.size());
            for (int i = 0; i < zs.size(); i++) {
                zs.set(i, new Vector<>());
                zs.get(i).setSize(Neurons.get(i).size());
            }

            SynapseNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                SynapseNudges.set(i, new Vector<>());
                SynapseNudges.get(i).setSize(Neurons.size() - 1);
                for (int i1 = 0; i1 < Neurons.size() - 1; i1++) {
                    SynapseNudges.get(i).set(i1, new Vector<>());
                    SynapseNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                    for (int i2 = 0; i2 < Neurons.get(i1).size(); i2++) {
                        SynapseNudges.get(i).get(i1).set(i2, new Vector<>());
                        SynapseNudges.get(i).get(i1).get(i2).setSize(Neurons.get(i1 + 1).size());
                    }
                }
            }
            BiasNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                BiasNudges.set(i, new Vector<>());
                BiasNudges.get(i).setSize(Neurons.size());
                for (int i1 = 1; i1 < Neurons.size(); i1++) {
                    BiasNudges.get(i).set(i1, new Vector<>());
                    BiasNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                }
            }

            perfectOutput.setSize(Neurons.get(Neurons.size()-1).size());


            Synapses.setSize(Neurons.size() - 1);              //Full Synapses list setup
            for (int i = 0; i < Neurons.size() - 1; i++) {
                Synapses.set(i, new Vector<>());
                Synapses.get(i).setSize(Neurons.get(i).size());
                for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                    Synapses.get(i).set(i1, new Vector<>());
                    Synapses.get(i).get(i1).setSize(Neurons.get(i + 1).size());
                }
            }

            Biases.setSize(Neurons.size());              //Full Biases list setup
            for (int i = 1; i < Neurons.size(); i++) {
                Biases.set(i, new Vector<>());
                Biases.get(i).setSize(Neurons.get(i).size());
            }

            randomInitialize();


        } else {
            System.out.println("Layer setup inside the program has been ignored");
            Neurons.setSize(Synapses.size()+1);                 //Neurons list setup according to the read file
            for (int i = 0; i < Neurons.size(); i++) {
                Neurons.set(i, new Vector<>());
                if (i < Synapses.size())
                    Neurons.get(i).setSize(Synapses.get(i).size());
                else
                    Neurons.get(i).setSize(Synapses.get(i-1).get(0).size());
            }
            Neurons.get(0).replaceAll(ignored -> 0.0);
            NeuronNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                NeuronNudges.set(i, new Vector<>());
                NeuronNudges.get(i).setSize(Neurons.size());
                for (int i1 = 0; i1 < Neurons.size(); i1++) {
                    NeuronNudges.get(i).set(i1, new Vector<>());
                    NeuronNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                }
            }

            zs.setSize(Neurons.size());
            for (int i = 0; i < zs.size(); i++) {
                zs.set(i, new Vector<>());
                zs.get(i).setSize(Neurons.get(i).size());
            }

            SynapseNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                SynapseNudges.set(i, new Vector<>());
                SynapseNudges.get(i).setSize(Neurons.size() - 1);
                for (int i1 = 0; i1 < Neurons.size() - 1; i1++) {
                    SynapseNudges.get(i).set(i1, new Vector<>());
                    SynapseNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                    for (int i2 = 0; i2 < Neurons.get(i1).size(); i2++) {
                        SynapseNudges.get(i).get(i1).set(i2, new Vector<>());
                        SynapseNudges.get(i).get(i1).get(i2).setSize(Neurons.get(i1 + 1).size());
                    }
                }
            }
            BiasNudges.setSize(butch);
            for (int i = 0; i < butch; i++) {
                BiasNudges.set(i, new Vector<>());
                BiasNudges.get(i).setSize(Neurons.size());
                for (int i1 = 1; i1 < Neurons.size(); i1++) {
                    BiasNudges.get(i).set(i1, new Vector<>());
                    BiasNudges.get(i).get(i1).setSize(Neurons.get(i1).size());
                }
            }

            perfectOutput.setSize(Neurons.get(Neurons.size()-1).size());
        }
        System.out.println("All lists has been successfully set up");


        if (mode == MODE_VISUALIZATION) {
            try {
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, FONT_SPEAK_HEAVY_TEXT));
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }

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

        try {
            if (images.exists()) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(images));
                imageBytes = bufferedInputStream.readAllBytes();
            }
            if (labels.exists()) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(labels));
                labelBytes = bufferedInputStream.readAllBytes();
            }
        } catch (IOException ignored) {}

        testNeuralNetwork();


        for (int i = 0; i < 5000; i++) {
            butchBackpropagation();

//            System.out.println("A butch has been completed");
            if (i % 100 == 0) {
                saveNeuralNetwork(neuralNetworkSave);
                saveNeuralNetwork(neuralNetworkBackup);
            }
        }
    }

    static void randomInitialize() {
        for (int i = 0; i < Synapses.size(); i++) {
            for (int i1 = 0; i1 < Synapses.get(i).size(); i1++) {
                for (int i2 = 0; i2 < Synapses.get(i).get(i1).size(); i2++) {
                    Synapses.get(i).get(i1).set(i2, Math.random()*4-2);
                }
            }
        }
        for (int i = 1; i < Biases.size(); i++) {
            for (int i1 = 0; i1 < Biases.get(i).size(); i1++) {
                Biases.get(i).set(i1, Math.random());
            }
        }
//        System.out.println(Synapses);
//        System.out.println(Biases);
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
                }
            }
        }
        double highestValue = 0;
        int result = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            double neuron = Neurons.get(Neurons.size()-1).get(i);
            if (neuron > highestValue) {
                highestValue = neuron;
                result = i;
            }
        }
        neuralNetworkAnswer = result;
    }
    static void neuronActivationCalculation(int layer, int index) {
        if (layer != 0) {
            double z = -Biases.get(layer).get(index);
            for (int i = 0; i < Neurons.get(layer - 1).size(); i++) {
                z += Neurons.get(layer - 1).get(i) * Synapses.get(layer - 1).get(i).get(index);
            }
            zs.get(layer).set(index, z);
            Neurons.get(layer).set(index, sigmoid(z));
        }
    }

    static void readImage(int num) {
        int index = 0;
        for (int i = 16 + 784 * num; i < 16 + 784 * (num + 1); i++) {
            double color = imageBytes[i];
            if (imageBytes[i] < 0) {
                color += 256;
            }
            Neurons.get(0).set(index, color / 255);
            index++;
        }
    }
    static void readAnswer(int num) {
        rightAnswer = labelBytes[8 + num];
        perfectOutput.replaceAll(ignored -> 0.0);
        perfectOutput.set(rightAnswer, 1.0);
    }

    static double costFunction() {
        double cost = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            cost += Math.pow(Neurons.get(Neurons.size()-1).get(i)-perfectOutput.get(i), 2);
        }
        return cost;    //0.0 = Perfect     10.0 = Wrong
    }

    static void butchBackpropagation() {
        for (int image = 0; image < butch; image++) {
            imageNumber = (int) (Math.random()*60000);
            readImage(imageNumber);
            readAnswer(imageNumber);
            neuralNetwork();
            imagesUsed.add(imageNumber);
            costsBefore.add(costFunction());

            for (int i = 0; i < Neurons.get(Neurons.size() - 1).size(); i++) {
                NeuronNudges.get(image).get(Neurons.size() - 1).set(i, Neurons.get(Neurons.size() - 1).get(i) - perfectOutput.get(i));
            }


            for (int layer = Neurons.size() - 1; layer > 0; layer--) {    //Going from the last layer to second
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {     //Calculating Synapse derivatives for each layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double previous_neuron = Neurons.get(layer - 1).get(prNeuron);
                        double z = zs.get(layer).get(neuron);
                        double synapse_derivative = previous_neuron * sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron);
                        SynapseNudges.get(image).get(layer - 1).get(prNeuron).set(neuron, -synapse_derivative);    //Operation -a needed to be applied
                    }
                }
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {    //Calculating Bias derivatives for each layer
                    double z = zs.get(layer).get(neuron);
                    double bias_derivative = sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron);
                    BiasNudges.get(image).get(layer).set(neuron, -bias_derivative);    //Operation -a needed to be applied
                }
                if (layer > 1) {    //Calculating Neuron value derivatives for iteration to the previous layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double neuron_derivative = 0.0;
                        for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                            double z = zs.get(layer).get(neuron);
                            neuron_derivative += sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                        }
                        NeuronNudges.get(image).get(layer - 1).set(prNeuron, -neuron_derivative);
                    }
                }
            }


            neuralNetwork();
        }


        for (int i = 0; i < SynapseNudges.get(0).size(); i++) {      //Applying Synapse nudges
            for (int i1 = 0; i1 < SynapseNudges.get(0).get(i).size(); i1++) {
                for (int i2 = 0; i2 < SynapseNudges.get(0).get(i).get(i1).size(); i2++) {
                    double average_nudge = 0.0;
                    for (int image = 0; image < butch; image++) {
                        average_nudge += SynapseNudges.get(image).get(i).get(i1).get(i2);
                    }
                    average_nudge /= butch;
                    Synapses.get(i).get(i1).set(i2, Synapses.get(i).get(i1).get(i2) + average_nudge);
                }
            }
        }
        for (int i = 1; i < BiasNudges.get(0).size(); i++) {   //Applying Bias nudges
            for (int i1 = 0; i1 < BiasNudges.get(0).get(i).size(); i1++) {
                double average_nudge = 0.0;
                for (int image = 0; image < butch; image++) {
                    average_nudge += BiasNudges.get(image).get(i).get(i1);
                }
                average_nudge /= butch;
                Biases.get(i).set(i1, Biases.get(i).get(i1) + average_nudge);
            }
        }


//        double sum = 0;
//        for (double d : costsBefore) {
//            sum += d;
//        }
//        double initial_cost = sum/costsBefore.size();
//        costsBefore = new Vector<>();
//
//        sum = 0;
//        for (int i : imagesUsed) {
//            readImage(i);
//            readAnswer(i);
//            neuralNetwork();
//            sum += costFunction();
//        }
//        double new_cost = sum/imagesUsed.size();
//        imagesUsed = new Vector<>();
//
//        System.out.println((double) ((int)((initial_cost-new_cost)*1000))/1000);
    }

    static void setPerfectOutput(int correctResult) {
        for (int i = 0; i < 10; i++) {
            if (i == correctResult)
                perfectOutput.set(i, 1.0);
            else
                perfectOutput.set(i, 0.0);
        }
    }

    static boolean loadNeuralNetwork(File load) {
        try {
            FileInputStream fileInputStream = new FileInputStream(load);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Biases = (Vector<Vector<Double>>) objectInputStream.readObject();
            Synapses = (Vector<Vector<Vector<Double>>>) objectInputStream.readObject();
            System.out.println("Neural network ["+load+"] has been successfully loaded");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    static void saveNewNeuralNetwork() {

    }

    static void saveNeuralNetwork(File save) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(save);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Biases);
            objectOutputStream.writeObject(Synapses);
            objectOutputStream.close();
            System.out.println("Neural network ["+save+"] has been successfully saved");
        } catch (IOException ignored) {}
    }

    void testNeuralNetwork() {
        double successes = 0;
        for (int i = 0; i < 30000; i++) {
            readImage(i);
            readAnswer(i);
            neuralNetwork();
            if (rightAnswer == neuralNetworkAnswer) {
                successes += 1;
            }
        }
        System.out.println((successes/30000*100)+"%");
    }       // 10.9%/11%      9.9%/10.4%/13.5%/15.2%
}
