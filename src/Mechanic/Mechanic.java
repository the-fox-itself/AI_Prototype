package Mechanic;

import java.awt.*;
import java.io.*;
import java.util.Vector;

import static Libraries.Methods.visTrue;

public class Mechanic extends MainVariables {
    void preparation() {
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

        for (int i = 0; i < 5000; i++) {
            butchBackpropagation();

            System.out.println("A butch has been completed");
            saveNeuralNetwork(neuralNetworkSave);
            if (i % 10 == 0) {
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
//                    System.out.println(Neurons.get(i).get(i1));
                }
//                System.out.println();
            }
        }
        double highestValue = 0;
        int result = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            if (Neurons.get(Neurons.size()-1).get(i) > highestValue) {
                highestValue = Neurons.get(Neurons.size()-1).get(i);
                result = i;
            }
//            System.out.println(i + ": " + Neurons.get(Neurons.size()-1).get(i));
        }
//        System.out.println("Final guess: " + result + "\n");
        neuralNetworkAnswer = result;
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

    static void readImage(int num) {
        try {
            FileInputStream fileInputStream = new FileInputStream(images);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] f = bufferedInputStream.readAllBytes();
            int index = 0;
            for (int i = 16+784*num; i < 16+784*(num+1); i++) {
                double color = f[i];
                if (f[i] < 0) {
                    color = 127+f[i]-(-128);
                }
                Neurons.get(0).set(index, color/255);
                index++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void readAnswer(int num) {
        try {
            FileInputStream fileInputStream = new FileInputStream(labels);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] f = bufferedInputStream.readAllBytes();
            int answer = f[8+num];
            for (int i = 0; i < perfectOutput.size(); i++) {
                if (i == answer)
                    perfectOutput.set(i, 1.0);
                else
                    perfectOutput.set(i, 0.0);
            }
//            System.out.println(perfectOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static double costFunction() {
        double cost = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            System.out.println(perfectOutput.get(i));
            cost += Math.pow(Neurons.get(Neurons.size()-1).get(i)-perfectOutput.get(i), 2);
        }
        return cost;    //0.0 = Perfect     10.0 = Wrong
    }

    static void butchBackpropagation() {
//        System.out.println("Total cost: " + costFunction());
        for (int image = 0; image < butch; image++) {
            imageNumber = (int) (Math.random()*60000);
            readImage(imageNumber);
            readAnswer(imageNumber);
            neuralNetwork();
            imageNumber += 1;

            for (int i = 0; i < Neurons.get(Neurons.size() - 1).size(); i++) {
                NeuronNudges.get(image).get(Neurons.size() - 1).set(i, Neurons.get(Neurons.size() - 1).get(i) - perfectOutput.get(i));
            }

            for (int layer = Neurons.size() - 1; layer > 0; layer--) {    //Going from the last layer to second
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {     //Calculating Synapse derivatives for each layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double current_neuron = Neurons.get(layer).get(neuron);
                        double previous_neuron = Neurons.get(layer - 1).get(prNeuron);
                        double z = Biases.get(layer).get(neuron);
                        for (int i = 0; i < Neurons.get(layer - 1).size(); i++) {
                            z += Neurons.get(layer - 1).get(i) * Synapses.get(layer - 1).get(i).get(neuron);
                        }
                        double synapse_derivative = previous_neuron * sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron);
                        SynapseNudges.get(image).get(layer - 1).get(prNeuron).set(neuron, -synapse_derivative);    //Operation -a needed to be applied
                    }
                }
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {    //Calculating Bias derivatives for each layer
                    double current_neuron = Neurons.get(layer).get(neuron);
                    double z = Biases.get(layer).get(neuron);
                    for (int i = 0; i < Neurons.get(layer - 1).size(); i++) {
                        z += Neurons.get(layer - 1).get(i) * Synapses.get(layer - 1).get(i).get(neuron);
                    }
                    double bias_derivative = sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron);
                    BiasNudges.get(image).get(layer).set(neuron, -bias_derivative);    //Operation -a needed to be applied
                }
                if (layer > 1) {    //Calculating Neuron value derivatives for iteration to the previous layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double neuron_derivative = 0.0;
                        for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                            double z = Biases.get(layer).get(neuron);
                            for (int i = 0; i < Neurons.get(layer - 1).size(); i++) {
                                z += Neurons.get(layer - 1).get(i) * Synapses.get(layer - 1).get(i).get(neuron);
                            }
                            neuron_derivative += sigmoid_derivative(z) * 2 * NeuronNudges.get(image).get(layer).get(neuron) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                        }
                        NeuronNudges.get(image).get(layer - 1).set(prNeuron, -neuron_derivative);
                    }
                }
            }

//            System.out.println(SynapseNudges);
            neuralNetwork();
        }
//        System.out.println("New total cost: " + costFunction());

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
}
