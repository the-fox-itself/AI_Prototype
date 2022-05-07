package Mechanic;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import static Libraries.Methods.*;

public class Mechanic extends MainVariables {                                                                           //8.88%   20.09%   19.51%
    void preparation() {
        try {                                                                                                           //Loading required fonts
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, FONT_SPEAK_HEAVY_TEXT));
        } catch (IOException e) {
            System.out.println("! The font file ["+FONT_SPEAK_HEAVY_TEXT+"] cannot be read");
        } catch (FontFormatException e) {
            System.out.println("! An unexpected error occurred while trying to load font file ["+FONT_SPEAK_HEAVY_TEXT+"]");
        }

        frame.add(drawPanel);
        drawPanel.setBounds(frame.getWidth() / 2 - 28 * 25 / 2, frame.getHeight() / 2 - 37 - 28 * 25 / 2, 28 * 25, 28 * 25);

        frame.addKeyListener(new DrawKeyListener());
        drawPanel.addMouseListener(new DrawMouseListener());
        drawPanel.addMouseMotionListener(new DrawMouseMotionListener());

        nnvFrame.add(nnvPanel);
        nnvPanel.setBounds(0, 0, nnvFrame.getWidth(), nnvFrame.getHeight());

        nnvFrame.addMouseListener(new NNVMouseListener());
        nnvFrame.addMouseMotionListener(new NNVMouseMotionListener());
        nnvFrame.addMouseWheelListener(new NNVMouseWheelListener());

        try {                                                                                                           //Loading training data
            if (images.exists()) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(images));
                imageBytes = bufferedInputStream.readAllBytes();
            }
            if (labels.exists()) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(labels));
                labelBytes = bufferedInputStream.readAllBytes();
            }
        } catch (FileNotFoundException e) {
            System.out.println("! Training data files ["+images+"] and ["+labels+"] have not been found");
        } catch (IOException e) {
            System.out.println("! An unexpected error occurred while trying to load Training data files ["+images+"] and ["+labels+"]");
        }

        System.out.println(": Type \"help\" if you are new here!");
        while (true) {
            String user_command = readLine("- ");
            switch (user_command) {
                case "help":
                    String text = """
                            help            List all commands and their functions
                            exit/quit       Exit the program
                            new             Create a new Neural network
                            load            Load a saved Neural network file
                            save            Save current Neural network as a file
                            train           Train current Neural network
                            test            Test the accuracy of the current Neural network
                            visualization   Switch to Visualization mode (less efficient, not recommended for training)
                            optimization    Switch to Optimization mode (more efficient)
                            performance     [not finished]
                            """;
                    System.out.println(text);
                    break;
                case "exit":
                case "quit":
                    System.out.println(": You successfully quited the program");
                    System.exit(0);
                    break;
                case "new":
                    String l = readLine("- Number of layers: ");
                    if (!l.equals("")) {
                        layers = new int[Integer.parseInt(l)];
                        for (int i = 0; i < layers.length; i++) {
                            layers[i] = Integer.parseInt(readLine("- Number of neurons in layer " + (i + 1) + ": "));
                        }
                    } else {
                        System.out.println(": Default layer setup has been used");
                    }

                    Neurons.setSize(layers.length);                                                                     //Full Neurons list setup
                    for (int i = 0; i < Neurons.size(); i++) {
                        Neurons.set(i, new Vector<>());
                        Neurons.get(i).setSize(layers[i]);
                    }

                    Synapses.setSize(Neurons.size() - 1);                                                               //Full Synapses list setup
                    for (int i = 0; i < Neurons.size() - 1; i++) {
                        Synapses.set(i, new Vector<>());
                        Synapses.get(i).setSize(Neurons.get(i).size());
                        for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                            Synapses.get(i).set(i1, new Vector<>());
                            Synapses.get(i).get(i1).setSize(Neurons.get(i + 1).size());
                        }
                    }

                    Biases.setSize(Neurons.size());                                                                     //Full Biases list setup
                    for (int i = 1; i < Neurons.size(); i++) {
                        Biases.set(i, new Vector<>());
                        Biases.get(i).setSize(Neurons.get(i).size());
                    }

                    randomInitialize();

                    zs.setSize(Neurons.size());
                    for (int i = 0; i < zs.size(); i++) {
                        zs.set(i, new Vector<>());
                        zs.get(i).setSize(Neurons.get(i).size());
                    }

                    Neurons.get(0).replaceAll(ignored -> 0.0);

                    perfectOutput.setSize(Neurons.get(Neurons.size()-1).size());

                    changeButch(butch);

                    System.out.println(": All lists has been successfully set up");
                    System.out.println(": New Neural network has been successfully created with layer setup "+ Arrays.toString(layers));

                    neuralNetworkSetUp = true;
                    break;
                case "load":
                    if (loadNeuralNetwork(neuralNetworkSave)) {
                        System.out.println(": Layer setup of the loaded Neural network file has been used");
                        Neurons.setSize(Synapses.size()+1);                                                             //Neurons list setup according to the read file
                        for (int i = 0; i < Neurons.size(); i++) {
                            Neurons.set(i, new Vector<>());
                            if (i < Synapses.size())
                                Neurons.get(i).setSize(Synapses.get(i).size());
                            else
                                Neurons.get(i).setSize(Synapses.get(i-1).get(0).size());
                        }

                        zs.setSize(Neurons.size());
                        for (int i = 0; i < zs.size(); i++) {
                            zs.set(i, new Vector<>());
                            zs.get(i).setSize(Neurons.get(i).size());
                        }

                        Neurons.get(0).replaceAll(ignored -> 0.0);

                        perfectOutput.setSize(Neurons.get(Neurons.size()-1).size());

                        changeButch(butch);

                        System.out.println(": All lists has been successfully set up");
                        System.out.println(": Loaded Neural network is ready to be used");

                        neuralNetworkSetUp = true;
                    }
                    break;
                case "save":
                    if (neuralNetworkSetUp)
                        saveNeuralNetwork(neuralNetworkSave, true);
                    else
                        System.out.println("! There is no created or loaded Neural network to save");
                    break;
                case "train":
                    if (neuralNetworkSetUp) {
                        changeButch(Integer.parseInt(readLine("- Butch size: ")));
                        int butchesNumber = Integer.parseInt(readLine("- Number of butches: "));
                        System.out.println(": Rough estimate of training time: "+(butchesNumber*butch/100)+" s");
                        if (Boolean.parseBoolean(readLine("- Proceed? (true/false) "))) {
                            System.out.println(": Training... (neural network is constantly saved, so it is safe to exit the program any time)");
                            long before = new Date().getTime();
                            for (int i = 0; i < butchesNumber; i++) {
                                butchBackpropagation();

                                if (i % 1000 == 0) {
                                    saveNeuralNetwork(neuralNetworkSave, false);
                                }
                            }
                            saveNeuralNetwork(neuralNetworkSave, true);
                            System.out.println(": Training has been successfully finished [" + butch + "/butch; " + butchesNumber + " butches]");
                            System.out.println(": Time training: " + (new Date().getTime() - before) / 1000 + " s");
                        } else
                            System.out.println(": The operation was successfully cancelled");
                    } else
                        System.out.println("! There is no created or loaded Neural network to train");
                    break;
                case "test":
                    if (neuralNetworkSetUp) {
                        System.out.println(": Testing... (usually takes a couple of minutes)");
                        testNeuralNetwork();
                    } else
                        System.out.println("! There is no created or loaded Neural network to test");
                    break;
                case "visualization":
                case "visualisation":
                    if (neuralNetworkSetUp) {
                        if (mode != MODE_VISUALIZATION) {
                            mode = MODE_VISUALIZATION;

                            visTrue(frame);
                            visTrue(nnvFrame);

                            gameLoopOn = true;
                            gameLoop.start();

                            System.out.println(": The program has successfully switched to Visualization mode");
                        } else {
                            System.out.println("! The program is already running in Visualization mode");
                        }
                    } else
                        System.out.println("! There is no created or loaded Neural network to visualize");
                    break;
                case "optimization":
                case "optimisation":
                    if (mode != MODE_OPTIMIZATION) {
                        mode = MODE_OPTIMIZATION;

                        visFalse(frame);
                        visFalse(nnvFrame);

                        gameLoopOn = false;

                        System.out.println(": The program has successfully switched to Optimization mode");
                    } else
                        System.out.println("! The program is already running in Optimization mode");
                    break;
                case "performance":

                    break;
            }
        }
    }

    void changeButch(int newButch) {
        butch = newButch;

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
        setPerfectOutput(rightAnswer);
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
        perfectOutput.replaceAll(ignored -> 0.0);
        perfectOutput.set(correctResult, 1.0);
    }

    static boolean loadNeuralNetwork(File load) {
        try {
            FileInputStream fileInputStream = new FileInputStream(load);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Biases = (Vector<Vector<Double>>) objectInputStream.readObject();
            Synapses = (Vector<Vector<Vector<Double>>>) objectInputStream.readObject();
            System.out.println(": Neural network ["+load+"] has been successfully loaded");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("! Neural network file ["+load+"] has not been found");
            return false;
        } catch (IOException e) {
            System.out.println("! An unexpected error has occurred while trying to load Neural network file ["+load+"]");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("! Neural network file ["+load+"] is corrupted and cannot be read");
            return false;
        }
    }

    static void saveNeuralNetwork(File save, boolean display) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(save);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Biases);
            objectOutputStream.writeObject(Synapses);
            objectOutputStream.close();
            if (display)
                System.out.println(": Neural network ["+save+"] has been successfully saved");
        } catch (IOException e) {
            System.out.println("! An expected error occurred while trying to save the Neural network ["+save+"]");
        }
    }

    void testNeuralNetwork() {
        long before = new Date().getTime();
        double successes = 0;
        for (int i = 0; i < 60000; i++) {
            readImage(i);
            readAnswer(i);
            neuralNetwork();
            if (rightAnswer == neuralNetworkAnswer) {
                successes += 1;
            }
        }
        double result = successes/60000*100;
        System.out.println(": The test has been successfully finished");
        System.out.println(": Time testing: "+(new Date().getTime()-before)/1000+" s");
        System.out.println(": Neural network total accuracy - "+result+"%");
    }
}
