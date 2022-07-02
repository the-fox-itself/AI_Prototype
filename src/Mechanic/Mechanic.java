package Mechanic;                                                                                                       //8.88%   20.09%   19.51%   20.56%   18.76%

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import static Libraries.Methods.*;

public class Mechanic extends MainVariables {                                                                           // 12.08%
    static void preparation() {
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

        modelFrame.add(modelPanel);
        modelPanel.setBounds(0, 0, modelFrame.getWidth(), modelFrame.getHeight());

        nnvFrame.addMouseListener(new ModelMouseListener());
        nnvFrame.addMouseMotionListener(new ModelMouseMotionListener());
        nnvFrame.addMouseWheelListener(new ModelMouseWheelListener());

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
                case "train derivatives":
                case "train d":
                    if (neuralNetworkSetUp) {
                        changeButch(Integer.parseInt(readLine("- Butch size: ")));
                        int butchesNumber = Integer.parseInt(readLine("- Number of butches: "));
                        System.out.println(": Rough estimate of training time: "+convertTime((long) butchesNumber *butch/100));
                        if (Boolean.parseBoolean(readLine("- Proceed? (true/false) "))) {
                            System.out.println(": Training... (neural network is constantly saved, so it is safe to exit the program any time)");
                            long before = new Date().getTime();
                            for (int i = 0; i < butchesNumber; i++) {
//                                imageNumber = (int) (Math.random()*60000);
//                                readImage(imageNumber);
//                                readAnswer(imageNumber);
//                                neuralNetwork();
//
//                                while (Neurons.get(Neurons.size()-1).get(rightAnswer) < 0.9)
//                                    backpropagation();
                                butchBackpropagation();

                                if (i % 100 == 0) {
                                    saveNeuralNetwork(neuralNetworkSave, true);
                                }
                            }
                            saveNeuralNetwork(neuralNetworkSave, true);
                            System.out.println(": Training has been successfully finished [" + butch + "/butch; " + butchesNumber + " butches]");
                            System.out.println(": Time training: " + convertTime((new Date().getTime() - before) / 1000));

                            System.out.println(": Testing... (usually takes a couple of minutes)");
                            testNeuralNetwork();

                            playSound(soundFile);
                        } else
                            System.out.println(": The operation was successfully cancelled");
                    } else
                        System.out.println("! There is no created or loaded Neural network to train");
                    break;
                case "train evolution":
                case "train e":
                    //Set up empty Neural Network
                    layers = new int[]{784, 10};
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

//                    Biases.setSize(Neurons.size());                                                                     //Full Biases list setup
//                    for (int i = 1; i < Neurons.size(); i++) {
//                        Biases.set(i, new Vector<>());
//                        Biases.get(i).setSize(Neurons.get(i).size());
//                    }

                    zs.setSize(Neurons.size());
                    for (int i = 0; i < zs.size(); i++) {
                        zs.set(i, new Vector<>());
                        zs.get(i).setSize(Neurons.get(i).size());
                    }
                    Neurons.get(0).replaceAll(ignored -> 0.0);
                    perfectOutput.setSize(Neurons.get(Neurons.size()-1).size());
                    neuralNetworkSetUp = true;

                    int clone_number = 100;
                    for (int generations = 0; generations < 100; generations++) {
                        //Create mutated clones
                        int action = (int) (Math.random() * 6);
                        switch (action) {
                            case 0:     //Add synapse                           (if there is a place to add one)

                                break;
                            case 1:     //Change synapse's weight               (if there is a synapse) + should be privileged

                                break;
                            case 2:     //Disable synapse                       (if there is a synapse)

                                break;
                            case 3:     //Add a hidden neuron

                                break;
                            case 4:     //Change neuron's activation function   (if there is a neuron)

                                break;
                            case 5:     //Remove a hidden neuron                (if there is a neuron)

                                break;

                        }

                        //Test mutated clones


                        //Save the best clone and pass its Neural Network to the next ones

                    }
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
                case "v":
                    if (neuralNetworkSetUp) {
                        if (mode != MODE_VISUALIZATION) {
                            mode = MODE_VISUALIZATION;

                            visTrue(frame);
                            visTrue(nnvFrame);

                            gameLoopOn = true;
                            gameLoop = new GameThreads.GameLoop();
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
                case "o":
                    if (mode != MODE_OPTIMIZATION) {
                        mode = MODE_OPTIMIZATION;

                        visFalse(frame);
                        visFalse(nnvFrame);

                        gameLoopOn = false;
                        gameLoop.interrupt();

                        System.out.println(": The program has successfully switched to Optimization mode");
                    } else
                        System.out.println("! The program is already running in Optimization mode");
                    break;
                case "performance":

                    break;
                case "model":
                    layers = new int[]{2, 2, 2, 1};

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

                    changeButch(1);

                    visTrue(modelFrame);

                    modelGameLoopOn = true;
                    modelGameLoop.start();
                    System.out.println(": The program has successfully launched a Neural Network model");
                    break;
            }
        }
    }

    static void changeButch(int newButch) {
        butch = newButch;

        NeuronNudges.setSize(Neurons.size());
        for (int i = 0; i < NeuronNudges.size(); i++) {
            NeuronNudges.set(i, new Vector<>());
            NeuronNudges.get(i).setSize(Neurons.get(i).size());
            for (int i1 = 0; i1 < NeuronNudges.get(i).size(); i1++) {
                NeuronNudges.get(i).set(i1, new Vector<>());
                NeuronNudges.get(i).get(i1).setSize(butch);
            }
        }
        SynapseNudges.setSize(Neurons.size() - 1);
        for (int i = 0; i < SynapseNudges.size(); i++) {
            SynapseNudges.set(i, new Vector<>());
            SynapseNudges.get(i).setSize(Neurons.get(i).size());
            for (int i1 = 0; i1 < SynapseNudges.get(i).size(); i1++) {
                SynapseNudges.get(i).set(i1, new Vector<>());
                SynapseNudges.get(i).get(i1).setSize(Neurons.get(i + 1).size());
                for (int i2 = 0; i2 < SynapseNudges.get(i).get(i1).size(); i2++) {
                    SynapseNudges.get(i).get(i1).set(i2, new Vector<>());
                    SynapseNudges.get(i).get(i1).get(i2).setSize(butch);
                }
            }
        }
        BiasNudges.setSize(Neurons.size());
        for (int i = 1; i < BiasNudges.size(); i++) {
            BiasNudges.set(i, new Vector<>());
            BiasNudges.get(i).setSize(Neurons.get(i).size());
            for (int i1 = 0; i1 < BiasNudges.get(i).size(); i1++) {
                BiasNudges.get(i).set(i1, new Vector<>());
                BiasNudges.get(i).get(i1).setSize(butch);
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
    static double ReLU_derivative(double x) {
        if (x < 0)
            return 0;
        else if (x > 0) {
            return 1;
        } else {
            return Double.NaN;
        }
    }
    static double SIG(double x) {
        return sigmoid(x);
    }
    static double LIN(double x) {
        return LIN(x, 1);
    }
    static double LIN(double x, double multiplier) {
        return x*multiplier;
    }
    static double SQR(double x) {
        return x*x;
    }
    static double SIN(double x) {
        return Math.sin(x);
    }
    static double ABS(double x) {
        return Math.abs(x);
    }
    static double REL(double x) {
        return ReLU(x);
    }
    static double GAU(double x, double height, double center, double standard_deviation) {
        return height * Math.pow(Math.E, -Math.pow(x-center, 2)/(2*Math.pow(standard_deviation, 2)));
    }
    static double LAT(double x) {
        return 0;
    }


    static void neuralNetwork() {
        for (int i = 1; i < Neurons.size(); i++) {
            for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                neuronActivationCalculation(i, i1);
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
            if (layer < Neurons.size()-1)
                Neurons.get(layer).set(index, ReLU(z));
            else
                Neurons.get(layer).set(index, sigmoid(z));
        }
    }

    static void readImage(int num) {
        short index = 0;
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

    static void setPerfectOutput(int correctResult) {
        perfectOutput.replaceAll(ignored -> 0.0);
        perfectOutput.set(correctResult, 1.0);
    }

    static double costFunction() {
        double cost = 0;
        for (int i = 0; i < Neurons.get(Neurons.size()-1).size(); i++) {
            cost += Math.pow(Neurons.get(Neurons.size()-1).get(i)-perfectOutput.get(i), 2);
        }
        return cost;    //0.0 = Perfect     10.0 = Wrong
    }

    static void backpropagation() {
        for (int i = 0; i < Neurons.get(Neurons.size() - 1).size(); i++) {
            NeuronNudges.get(Neurons.size() - 1).get(i).set(0, Neurons.get(Neurons.size() - 1).get(i) - perfectOutput.get(i));
        }


        for (int layer = Neurons.size() - 1; layer > 0; layer--) {    //Going from the last layer to second
            for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {     //Calculating Synapse derivatives for each layer
                for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                    double previous_neuron = Neurons.get(layer - 1).get(prNeuron);
                    double z = zs.get(layer).get(neuron);
                    double synapse_derivative;
                    if (layer < Neurons.size()-1)
                        synapse_derivative = previous_neuron * ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0);
                    else
                        synapse_derivative = previous_neuron * sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0);
                    if (neuron == 0 && prNeuron == 0) {
                        if (layer < Neurons.size()-1)
                            System.out.println("Synapse composition (layer="+layer+"): -1 * " + previous_neuron + " * " + ReLU_derivative(z) + " * 2 * " + NeuronNudges.get(layer).get(neuron).get(0) + " = " + -synapse_derivative);
                        else
                            System.out.println("Synapse composition (layer="+layer+"): -1 * " + previous_neuron + " * " + sigmoid_derivative(z) + " * 2 * " + NeuronNudges.get(layer).get(neuron).get(0) + " = " + -synapse_derivative);
                    }
                    SynapseNudges.get(layer - 1).get(prNeuron).get(neuron).set(0, -synapse_derivative);    //Operation -a needed to be applied
                }
            }
            for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {    //Calculating Bias derivatives for each layer
                double z = zs.get(layer).get(neuron);
                double bias_derivative;
                if (layer < Neurons.size()-1)
                    bias_derivative = ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0);
                else
                    bias_derivative = sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0);
                if (neuron == 0) {
                    if (layer < Neurons.size()-1)
                        System.out.println("Bias composition (layer="+layer+"): -1 * " + ReLU_derivative(z) + " ("+z+") * 2 * " + NeuronNudges.get(layer).get(neuron).get(0) + " = " + -bias_derivative);
                    else
                        System.out.println("Bias composition (layer="+layer+"): -1 * " + sigmoid_derivative(z) + " ("+z+") * 2 * " + NeuronNudges.get(layer).get(neuron).get(0) + " = " + -bias_derivative);
                }
                BiasNudges.get(layer).get(neuron).set(0, -bias_derivative);         //Operation -a needed to be applied
            }
            if (layer > 1) {    //Calculating Neuron value derivatives for iteration to the previous layer
                for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                    double neuron_derivative = 0.0;
                    for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                        double z = zs.get(layer).get(neuron);
                        if (layer < Neurons.size()-1)
                            neuron_derivative += ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                        else
                            neuron_derivative += sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(0) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                    }
                    NeuronNudges.get(layer - 1).get(prNeuron).set(0, -neuron_derivative);
                    if (prNeuron == 0) {
                        System.out.println("Neuron gradient: " + -neuron_derivative);
                    }
                }
            }
        }
        System.out.println("Synapse gradients:\t" + SynapseNudges.get(SynapseNudges.size()-1).get(0).get(0).get(0) + "\t" + SynapseNudges.get(SynapseNudges.size()-2).get(0).get(0).get(0) + "\t" + SynapseNudges.get(SynapseNudges.size()-3).get(0).get(0).get(0));
        System.out.println("Bias gradients:\t\t" + BiasNudges.get(BiasNudges.size()-1).get(0).get(0) + "\t" + BiasNudges.get(BiasNudges.size()-2).get(0).get(0) + "\t" + BiasNudges.get(BiasNudges.size()-3).get(0).get(0));

        for (int i = 0; i < SynapseNudges.size(); i++) {      //Applying Synapse nudges
            for (int i1 = 0; i1 < SynapseNudges.get(i).size(); i1++) {
                for (int i2 = 0; i2 < SynapseNudges.get(i).get(i1).size(); i2++) {
                    double synapse_nudge = SynapseNudges.get(i).get(i1).get(i2).get(0);
                    Synapses.get(i).get(i1).set(i2, Synapses.get(i).get(i1).get(i2) + synapse_nudge);
                }
            }
        }
        for (int i = 1; i < BiasNudges.size(); i++) {   //Applying Bias nudges
            for (int i1 = 0; i1 < BiasNudges.get(i).size(); i1++) {
                double bias_nudge = BiasNudges.get(i).get(i1).get(0);
                Biases.get(i).set(i1, Biases.get(i).get(i1) + bias_nudge);
            }
        }

        neuralNetwork();
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
                double neuron_derivative = Neurons.get(Neurons.size() - 1).get(i) - perfectOutput.get(i);
                NeuronNudges.get(Neurons.size() - 1).get(i).set(image, neuron_derivative);
            }


            for (int layer = Neurons.size() - 1; layer > 0; layer--) {    //Going from the last layer to second
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {     //Calculating Synapse derivatives for each layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double previous_neuron = Neurons.get(layer - 1).get(prNeuron);
                        double z = zs.get(layer).get(neuron);
                        double synapse_derivative;
                        if (layer < Neurons.size()-1)
                            synapse_derivative = previous_neuron * ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image);
                        else
                            synapse_derivative = previous_neuron * sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image);
                        SynapseNudges.get(layer - 1).get(prNeuron).get(neuron).set(image, -synapse_derivative);    //Operation -a needed to be applied
                    }
                }
                for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {    //Calculating Bias derivatives for each layer
                    double z = zs.get(layer).get(neuron);
                    double bias_derivative;
                    if (layer < Neurons.size()-1)
                        bias_derivative = ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image);
                    else
                        bias_derivative = sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image);
                    BiasNudges.get(layer).get(neuron).set(image, -bias_derivative);    //Operation -a needed to be applied
                }
                if (layer > 1) {    //Calculating Neuron value derivatives for iteration to the previous layer
                    for (int prNeuron = 0; prNeuron < Neurons.get(layer - 1).size(); prNeuron++) {
                        double neuron_derivative = 0.0;
                        for (int neuron = 0; neuron < Neurons.get(layer).size(); neuron++) {
                            double z = zs.get(layer).get(neuron);
                            if (layer < Neurons.size()-1)
                                neuron_derivative += ReLU_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                            else
                                neuron_derivative += sigmoid_derivative(z) * 2 * NeuronNudges.get(layer).get(neuron).get(image) * Synapses.get(layer - 1).get(prNeuron).get(neuron);
                        }
                        NeuronNudges.get(layer - 1).get(prNeuron).set(image, -neuron_derivative);
                    }
                }
            }


            neuralNetwork();
        }


        for (int i = 0; i < SynapseNudges.size(); i++) {      //Applying Synapse nudges
            for (int i1 = 0; i1 < SynapseNudges.get(i).size(); i1++) {
                for (int i2 = 0; i2 < SynapseNudges.get(i).get(i1).size(); i2++) {
                    double average_nudge = 0.0;
                    for (int image = 0; image < SynapseNudges.get(i).get(i1).get(i2).size(); image++) {
                        average_nudge += SynapseNudges.get(i).get(i1).get(i2).get(image);
                    }
                    average_nudge /= butch;
                    Synapses.get(i).get(i1).set(i2, Synapses.get(i).get(i1).get(i2) + average_nudge);
                }
            }
        }
        for (int i = 1; i < BiasNudges.size(); i++) {   //Applying Bias nudges
            for (int i1 = 0; i1 < BiasNudges.get(i).size(); i1++) {
                double average_nudge = 0.0;
                for (int image = 0; image < BiasNudges.get(i).get(i1).size(); image++) {
                    average_nudge += BiasNudges.get(i).get(i1).get(image);
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

    static void testNeuralNetwork() {
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
        System.out.println(": Time testing: "+convertTime((new Date().getTime()-before)/1000));
        System.out.println(": Neural network total accuracy - "+result+"%");
    }

    static String convertTime(long time) {
        String returnString = "";
        if (time % 60 != 0) {
            returnString += (time%60)+" s";
        }
        if (time/60 > 0 && time/60 % 60 != 0) {
            returnString = (time/60%60)+" m " + returnString;
        }
        if (time/60/60 > 0) {
            returnString = (time/60/60)+" h " + returnString;
        }
        return returnString;
    }
}
