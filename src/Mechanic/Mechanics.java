package Mechanic;                                                                                                       //8.88%   20.09%   19.51%   20.56%   18.76%

import java.awt.*;
import java.io.*;
import java.util.*;

import static Libraries.Methods.*;

public class Mechanics extends Variables {                                                                           // 12.08%
    public static class TestThread extends Thread {
        @Override
        public void run() {
            super.run();
            double variable = 1;
            while (true) {
                variable++;
//                System.out.println(variable);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    static void preparation() {
//        for (int thread = 0; thread < 500; thread++) {
//            TestThread testThread = new TestThread();
//            testThread.start();
//        }

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

                    Synapses.setSize(Neurons.size()-1);                                                               //Full EvolutionSynapses list setup
                    for (int i = 0; i < Neurons.size()-1; i++) {
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
                    for (int i = 1; i < zs.size(); i++) {
                        zs.set(i, new Vector<>());
                        zs.get(i).setSize(Neurons.get(i).size());
                    }

                    Collections.fill(Neurons.get(0), 0.0);

                    perfectOutput.setSize(Neurons.lastElement().size());

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
                        for (int i = 1; i < zs.size(); i++) {
                            zs.set(i, new Vector<>());
                            zs.get(i).setSize(Neurons.get(i).size());
                        }

                        Collections.fill(Neurons.get(0), 0.0);

                        perfectOutput.setSize(Neurons.lastElement().size());

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
//                                while (Neurons.lastElement().get(rightAnswer) < 0.9)
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
                case "train":
                    //Set up empty clone storage system
                    String answer = readLine("- Number of generations: ");
                    if (!answer.equals("")) {
                        generation_number = Integer.parseInt(answer);
                        clone_number = Integer.parseInt(readLine("- Number of clones per generation: "));
                        trainingImagesNumber = Integer.parseInt(readLine("- Number of testing images every generation: "));
                    } else {
                        generation_number = 1000000;
                        clone_number = 100;
                        trainingImagesNumber = 50;
                        System.out.println(": Default Evolutionary Neural Network setup was used");
                    }

                    long clock = new Date().getTime();
                    long clockSaving = new Date().getTime();

                    EvolutionNeurons = new Vector<>();
                    EvolutionNeurons.setSize(clone_number);
                    for (int i = 0; i < EvolutionNeurons.size(); i++) {
                        EvolutionNeurons.set(i, new Vector<>());
                        EvolutionNeurons.get(i).setSize(1);
                    }
                    EvolutionSynapses = new Vector<>();
                    EvolutionSynapses.setSize(clone_number);
                    for (int i = 0; i < EvolutionSynapses.size(); i++) {
                        EvolutionSynapses.set(i, new Vector<>());
                        EvolutionSynapses.get(i).setSize(initial_layers.length - 1);
                        for (int i1 = 0; i1 < EvolutionSynapses.get(i).size(); i1++) {
                            EvolutionSynapses.get(i).set(i1, new Vector<>());
                            EvolutionSynapses.get(i).get(i1).setSize(initial_layers[i1]);
                            for (int i2 = 0; i2 < EvolutionSynapses.get(i).get(i1).size(); i2++) {
                                EvolutionSynapses.get(i).get(i1).set(i2, new Vector<>());
                                EvolutionSynapses.get(i).get(i1).get(i2).setSize(initial_layers.length);
                                for (int i3 = i1+1; i3 < EvolutionSynapses.get(i).get(i1).get(i2).size(); i3++) {
                                    EvolutionSynapses.get(i).get(i1).get(i2).set(i3, new Vector<>());
                                    EvolutionSynapses.get(i).get(i1).get(i2).get(i3).setSize(initial_layers[i3]);
                                    for (int i4 = 0; i4 < EvolutionSynapses.get(i).get(i1).get(i2).get(i3).size(); i4++) {
                                        EvolutionSynapses.get(i).get(i1).get(i2).get(i3).set(i4, Double.NaN);
                                    }
                                }
                            }
                        }
                    }

                    System.out.println((double) (new Date().getTime()-clock)/1000 + " s - lists initialization");
                    clock = new Date().getTime();
                    //An attempt to load a saved clone
                    if (loadEvolutionaryNeuralNetwork()) {
                        System.out.println((double) (new Date().getTime()-clock)/1000 + " s - Neural Network loading");
                        clock = new Date().getTime();
                        multiplyClones();
                        System.out.println((double) (new Date().getTime()-clock)/1000 + " s - clone multiplication");
                    }

                    trainingImages = new Vector<>();
                    if (trainingImagesNumber < 10) {
                        System.out.println("! "+trainingImages+" training images per generation is not enough for successful training");
                    }
                    int amountOfEachExample = trainingImagesNumber/10;
                    for (int testingAnswer = 0; testingAnswer < 10; testingAnswer++) {
                        int examples = 0;
                        for (int image = 0; image < 60000; image++) {
                            rightAnswer = labelBytes[8 + image];
                            if (rightAnswer == testingAnswer) {
                                trainingImages.add(image);
                                examples++;
                                if (examples == amountOfEachExample) {
                                    break;
                                } else if (examples > amountOfEachExample) {
                                    System.out.println("! Algorithm error: examples > amountOfEachExample");
                                }
                            }
                        }
                        if (examples < amountOfEachExample) {
                            System.out.println("! There is not enough training data");
                        }
                    }
//                    for (int index = 0; index < trainingImagesNumber; index++) {
//                        while (true) {
//                            int image = (int) (Math.random() * 60000);
//                            if (!trainingImages.contains(image)) {
//                                trainingImages.add(image);
//                                break;
//                            }
//                        }
//                    }

                    for (int generation = 0; generation < generation_number; generation++) {
                        //Create mutated clones
                        clock = new Date().getTime();

                        addActions      = 0;
                        changeActions   = 0;
                        disableActions  = 0;
                        neuronActions   = 0;
                        functionActions = 0;
                        removeActions   = 0;

//                        Vector<Vector<Vector<Vector<Double>>>> CloneSynapses = EvolutionSynapses.get(0);
//                        Vector<Vector<Integer>> CloneNeurons = EvolutionNeurons.get(0);
//                        ArrayList<int[]> missingSynapses = new ArrayList<>();
//                        ArrayList<int[]> existingSynapses = new ArrayList<>();
//                        ArrayList<int[]> existingNeurons = new ArrayList<>();
//                        for (int j = 0; j < CloneSynapses.size(); j++) {
//                            for (int j1 = 0; j1 < CloneSynapses.get(j).size(); j1++) {
//                                for (int j2 = j + 1; j2 < CloneSynapses.get(j).get(j1).size(); j2++) {
//                                    for (int j3 = 0; j3 < CloneSynapses.get(j).get(j1).get(j2).size(); j3++) {
//                                        double weight = CloneSynapses.get(j).get(j1).get(j2).get(j3);
//                                        if (Double.isNaN(weight)) {
//                                            if (add == -1) {
//                                                add = next;
//                                                next++;
//                                            }
//                                            missingSynapses.add(new int[]{j, j1, j2, j3});
//                                        } else {
//                                            if (change == -1) {
//                                                change = next;
//                                                next++;
//                                            }
//                                            if (disable == -1) {
//                                                disable = next;
//                                                next++;
//                                            }
//                                            if (neuron == -1) {
//                                                neuron = next;
//                                                next++;
//                                            }
//                                            existingSynapses.add(new int[]{j, j1, j2, j3});
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        for (int layer = 1; layer < CloneNeurons.size(); layer++) {
//                            for (int neuron1 = 0; neuron1 < CloneNeurons.get(layer).size(); neuron1++) {
//                                if (function == -1) {
//                                    function = next;
//                                    next++;
//                                }
//                                if (remove == -1) {
//                                    remove = next;
//                                    next++;
//                                }
//                                existingNeurons.add(new int[]{layer, neuron1});
//                            }
//                        }

                        Vector<Integer[]> removedSynapses = new Vector<>();
                        Vector<Integer[]> addedNeurons = new Vector<>();
                        Vector<Integer[]> changedFunctions = new Vector<>();
                        Vector<Integer[]> removedNeurons = new Vector<>();

                        Costs = new Vector<>();
                        Costs.setSize(clone_number);
                        Collections.fill(Costs, -1.0);

                        for (int clone = 0; clone < clone_number; clone++) {
                            Threads.CloneThread cloneThread = new Threads.CloneThread(clone);
                            cloneThread.start();
                        }
                        System.out.println((double) (new Date().getTime()-clock)/1000 + " s - all clone threads initialized of generation " + generation);
                        boolean loop = true;
                        while (loop) {
                            loop = false;
                            for (double cost : Costs) {
                                if (cost == -1.0) {
                                    loop = true;
                                }
                            }
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println(": All TestingLoops complete");
                        System.out.println((double) (new Date().getTime()-clock)/1000 + " s - all mutations + testing of generation " + generation);

                        double lowestCost = 10;
                        int lowestCostCloneIndex = 0;
                        double highestCost = -1;
                        int highestCostCloneIndex = 0;

                        for (int clone = 0; clone < clone_number; clone++) {
                            double cost = Costs.get(clone);
                            if (cost == -1.0) {
                                System.out.println(": Thread ERROR");
                            }
                            if (cost < lowestCost) {
                                lowestCost = cost;
                                lowestCostCloneIndex = clone;
                            }
                            if (cost > highestCost) {
                                highestCost = cost;
                                highestCostCloneIndex = clone;
                            }
                        }
                        for (int clone = 0; clone < clone_number; clone++) {
                            double cost = testEvolutionaryClone(EvolutionNeurons.get(clone), EvolutionSynapses.get(clone), trainingImages);
                            if (cost != Costs.get(clone)) {
                                System.out.println("! COST CALCULATION ERROR");
                                System.out.println(clone + "\t" + cost + "\t" + Costs.get(clone) + "\t" + (cost == Costs.get(clone)));
                            }
                        }

                        System.out.println("Generation " + generation + " complete");
                        System.out.println(": " + clone_number + " clones mutated");
                        System.out.println(addActions + " addActions\n" + changeActions + " changeActions\n" + disableActions + " disableActions\n" + neuronActions + " neuronActions\n" + functionActions + " functionActions\n" + removeActions + " removeActions");
                        System.out.println("= " + (addActions+changeActions+disableActions+neuronActions+functionActions+removeActions) + " total mutations");
                        System.out.println(": Testing images - " + trainingImages.size() + "");
                        System.out.println(": Best clone - #" + (lowestCostCloneIndex+1) + " with Lowest cost - " + lowestCost + " :: " + Costs.get(0));
                        System.out.println(": Worst clone - #" + (highestCostCloneIndex+1) + " with Highest cost - " + highestCost);

//                        System.out.println(": Highest correct guess % - #" + (highestCorrectCloneIndex+1) + " with " + highestCorrect + "% correct");
//                        System.out.println(": Lowest correct guess % - #" + (lowestCorrectCloneIndex+1) + " with " + lowestCorrect + "% correct");

                        //Choose the best clone and pass its Neural Network to the next ones
                        BestCloneNeurons = EvolutionNeurons.get(lowestCostCloneIndex);
                        BestCloneSynapses = EvolutionSynapses.get(lowestCostCloneIndex);
                        multiplyClones();

                        int neurons = 0;
                        for (int layer = 1; layer < BestCloneNeurons.size(); layer++) {
                            neurons += BestCloneNeurons.get(layer).size();
                        }
                        System.out.println("Number of neurons in the leading clone: " + neurons);
                        System.out.println();

                        //Saving the best clone as a file every 30 seconds
                        if ((double)(new Date().getTime()-clockSaving)/1000 > 30) {
                            clock = new Date().getTime();
                            saveEvolutionaryNeuralNetwork();
                            clockSaving = new Date().getTime();
                            System.out.println((double) (new Date().getTime()-clock)/1000 + " s - Neural Network saving");
                        }
                    }
                    System.out.println(": Evolutionary Neural Network training done successfully");
                    break;
                case "test":
                    if (neuralNetworkSetUp) {
                        System.out.println(": Testing... (usually takes a couple of minutes)");
                        testNeuralNetwork();
                    } else
                        System.out.println("! There is no created or loaded Neural network to test");
                    break;
                case "test e":      //14.91%        --> 20 generations -->      14.953%         14.1616%                46.79833333333333%
                    if (loadEvolutionaryNeuralNetwork()) {        //600 generations  15.383%      -->     2843 generations    101 neurons     1.4546      11.208%
                        System.out.println(": Testing... (usually takes a couple of minutes)");
                        long clock1 = new Date().getTime();

                        int correct = 0;
                        Neurons = new Vector<>();
                        Neurons.setSize(BestCloneNeurons.size() + 1);
                        Neurons.replaceAll(ignored -> new Vector<>());
                        Neurons.get(0).setSize(initial_layers[0]);
                        Neurons.lastElement().setSize(initial_layers[initial_layers.length - 1]);
                        for (int layer = 1; layer < Neurons.size() - 1; layer++) {
                            Neurons.get(layer).setSize(BestCloneNeurons.get(layer).size());
                        }

                        NeuronFunctions = new Vector<>();
                        NeuronFunctions.setSize(BestCloneNeurons.size() + 1);
                        NeuronFunctions.set(NeuronFunctions.size() - 1, new Vector<>());
                        NeuronFunctions.lastElement().setSize(initial_layers[initial_layers.length - 1]);
                        Collections.fill(NeuronFunctions.lastElement(), FUNCTION_SIG);
                        for (int layer = 1; layer < NeuronFunctions.size() - 1; layer++) {
                            NeuronFunctions.set(layer, new Vector<>());
                            NeuronFunctions.get(layer).setSize(BestCloneNeurons.get(layer).size());
                            for (int neuron = 0; neuron < NeuronFunctions.get(layer).size(); neuron++) {
                                NeuronFunctions.get(layer).set(neuron, BestCloneNeurons.get(layer).get(neuron));
                            }
                        }

                        SynapsesE = BestCloneSynapses;

                        zs.setSize(Neurons.size());
                        for (int layer = 1; layer < zs.size(); layer++) {
                            zs.set(layer, new Vector<>());
                            zs.get(layer).setSize(Neurons.get(layer).size());
                        }
                        perfectOutput.setSize(initial_layers[initial_layers.length - 1]);
                        evolutionTraining = true;

                        for (int image = 0; image < 60000; image++) {
                            readImage(image);
                            readAnswer(image);
                            neuralNetwork();
                            if (neuralNetworkAnswer == rightAnswer) {
                                correct++;
                            }
                            if (image % 100 == 0)
                                System.out.println(image);
                        }

//                        TestingThreadCompletes = 0;
//                        TestingThreadCorrects = 0;
//                        for (int image = 0; image < 60000; image+=1000) {
//                            Threads.TestingThread testingThread = new Threads.TestingThread(image, image+999);
//                            testingThread.start();
//                        }
//                        while (true) {
//                            if (TestingThreadCompletes == 60000) {
//                                break;
//                            } else {
////                                System.out.println(TestingThreadCompletes);
//                            }
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }

//                        System.out.println("Test result: " + ((double) (TestingThreadCorrects) / TestingThreadCompletes * 100) + "%");
                        System.out.println((double) (new Date().getTime()-clock1)/1000 + " s - Evolutionary Neural network full testing");
                        System.out.println("Test result: " + ((double) (correct) / 60000 * 100) + "%");
                    } else {
                        System.out.println("! There is no saved Evolutionary Neural network to test");
                    }
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
                            gameLoop = new Threads.GameLoop();
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

                    Variables.Synapses.setSize(Neurons.size()-1);                                                               //Full EvolutionSynapses list setup
                    for (int i = 0; i < Neurons.size()-1; i++) {
                        Variables.Synapses.set(i, new Vector<>());
                        Variables.Synapses.get(i).setSize(Neurons.get(i).size());
                        for (int i1 = 0; i1 < Neurons.get(i).size(); i1++) {
                            Variables.Synapses.get(i).set(i1, new Vector<>());
                            Variables.Synapses.get(i).get(i1).setSize(Neurons.get(i + 1).size());
                        }
                    }

                    Biases.setSize(Neurons.size());                                                                     //Full Biases list setup
                    for (int i = 1; i < Neurons.size(); i++) {
                        Biases.set(i, new Vector<>());
                        Biases.get(i).setSize(Neurons.get(i).size());
                    }

                    randomInitialize();

                    zs.setSize(Neurons.size());
                    for (int i = 1; i < zs.size(); i++) {
                        zs.set(i, new Vector<>());
                        zs.get(i).setSize(Neurons.get(i).size());
                    }

                    Collections.fill(Neurons.get(0), 0.0);

                    perfectOutput.setSize(Neurons.lastElement().size());

                    changeButch(1);

                    visTrue(modelFrame);

                    modelGameLoopOn = true;
                    modelGameLoop.start();
                    System.out.println(": The program has successfully launched a Neural Network model");
                    break;
            }
        }
    }

    static double testEvolutionaryClone(Vector<Vector<Integer>> CloneNeurons, Vector<Vector<Vector<Vector<Double>>>> CloneSynapses, Vector<Integer> TrainingImages) {
        double costSum = 0;
        Neurons = new Vector<>();
        Neurons.setSize(CloneNeurons.size() + 1);
        Neurons.replaceAll(ignored -> new Vector<>());
        Neurons.get(0).setSize(initial_layers[0]);
        Neurons.lastElement().setSize(initial_layers[initial_layers.length - 1]);
        for (int layer = 1; layer < Neurons.size() - 1; layer++) {
            Neurons.get(layer).setSize(CloneNeurons.get(layer).size());
        }

        NeuronFunctions = new Vector<>();
        NeuronFunctions.setSize(CloneNeurons.size() + 1);
        NeuronFunctions.set(NeuronFunctions.size() - 1, new Vector<>());
        NeuronFunctions.lastElement().setSize(initial_layers[initial_layers.length - 1]);
        Collections.fill(NeuronFunctions.lastElement(), FUNCTION_SIG);
        for (int layer = 1; layer < NeuronFunctions.size() - 1; layer++) {
            NeuronFunctions.set(layer, new Vector<>());
            NeuronFunctions.get(layer).setSize(CloneNeurons.get(layer).size());
            for (int neuron = 0; neuron < NeuronFunctions.get(layer).size(); neuron++) {
                NeuronFunctions.get(layer).set(neuron, CloneNeurons.get(layer).get(neuron));
            }
        }

        SynapsesE = CloneSynapses;

        zs.setSize(Neurons.size());
        for (int layer = 1; layer < zs.size(); layer++) {
            zs.set(layer, new Vector<>());
            zs.get(layer).setSize(Neurons.get(layer).size());
        }
        perfectOutput.setSize(initial_layers[initial_layers.length - 1]);
        evolutionTraining = true;

        for (int image : TrainingImages) {
            readImage(image);
            readAnswer(image);
            neuralNetwork();
            costSum += costFunction();
        }
        return costSum / trainingImages.size();
    }
    static boolean saveEvolutionaryNeuralNetwork() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(evolutionaryNeuralNetworkSave);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(BestCloneNeurons);
            objectOutputStream.writeObject(BestCloneSynapses);
            objectOutputStream.close();
            System.out.println(": Evolutionary Neural network ["+evolutionaryNeuralNetworkSave+"] has been successfully saved");
            return true;
        } catch (IOException e) {
            System.out.println("! An unexpected error occurred while trying to save the Evolutionary Neural network ["+evolutionaryNeuralNetworkSave+"]");
            return false;
        }
    }
    static boolean loadEvolutionaryNeuralNetwork() {
        try {
            FileInputStream fileInputStream = new FileInputStream(evolutionaryNeuralNetworkSave);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            BestCloneNeurons = (Vector<Vector<Integer>>) objectInputStream.readObject();
            BestCloneSynapses = (Vector<Vector<Vector<Vector<Double>>>>) objectInputStream.readObject();
            System.out.println(": Evolutionary Neural network ["+evolutionaryNeuralNetworkSave+"] has been successfully loaded");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("! Evolutionary Neural network file ["+evolutionaryNeuralNetworkSave+"] has not been found");
            return false;
        } catch (IOException e) {
            System.out.println("! An unexpected error has occurred while trying to load Evolutionary Neural network file ["+evolutionaryNeuralNetworkSave+"]");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("! Evolutionary Neural network file ["+evolutionaryNeuralNetworkSave+"] is corrupted and cannot be read");
            return false;
        }
    }
    static void multiplyClones() {
        EvolutionSynapses = new Vector<>();
        EvolutionSynapses.setSize(clone_number);
        EvolutionNeurons = new Vector<>();
        EvolutionNeurons.setSize(clone_number);
        for (int clone = 0; clone < clone_number; clone++) {
            EvolutionSynapses.set(clone, new Vector<>());
            EvolutionSynapses.get(clone).setSize(BestCloneSynapses.size());
            for (int i = 0; i < BestCloneSynapses.size(); i++) {
                EvolutionSynapses.get(clone).set(i, new Vector<>());
                EvolutionSynapses.get(clone).get(i).setSize(BestCloneSynapses.get(i).size());
                for (int i1 = 0; i1 < BestCloneSynapses.get(i).size(); i1++) {
                    EvolutionSynapses.get(clone).get(i).set(i1, new Vector<>());
                    EvolutionSynapses.get(clone).get(i).get(i1).setSize(BestCloneSynapses.get(i).get(i1).size());
                    for (int i2 = i+1; i2 < BestCloneSynapses.get(i).get(i1).size(); i2++) {
                        EvolutionSynapses.get(clone).get(i).get(i1).set(i2, new Vector<>());
                        EvolutionSynapses.get(clone).get(i).get(i1).get(i2).setSize(BestCloneSynapses.get(i).get(i1).get(i2).size());
                        for (int i3 = 0; i3 < BestCloneSynapses.get(i).get(i1).get(i2).size(); i3++) {
                            EvolutionSynapses.get(clone).get(i).get(i1).get(i2).set(i3, BestCloneSynapses.get(i).get(i1).get(i2).get(i3));
                        }
                    }
                }
            }
            EvolutionNeurons.set(clone, new Vector<>());
            EvolutionNeurons.get(clone).setSize(BestCloneNeurons.size());
            for (int i = 0; i < BestCloneNeurons.size(); i++) {
                EvolutionNeurons.get(clone).set(i, new Vector<>());
                if (BestCloneNeurons.get(i) != null) {
                    EvolutionNeurons.get(clone).get(i).setSize(BestCloneNeurons.get(i).size());
                    for (int i1 = 0; i1 < BestCloneNeurons.get(i).size(); i1++) {
                        EvolutionNeurons.get(clone).get(i).set(i1, BestCloneNeurons.get(i).get(i1));
                    }
                }
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
        SynapseNudges.setSize(Neurons.size()-1);
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
    static double SIG(double x) {                                                                                       // 0
        return sigmoid(x);
    }
    static double LIN(double x) {                                                                                       // 1
        return LIN(x, 1);
    }
    static double LIN(double x, double multiplier) {
        return x*multiplier;
    }
    static double SQR(double x) {                                                                                       // 2
        return x*x;
    }
    static double SIN(double x) {                                                                                       // 3
        return Math.sin(x);
    }
    static double ABS(double x) {                                                                                       // 4
        return Math.abs(x);
    }
    static double REL(double x) {                                                                                       // 5
        return ReLU(x);
    }
    static double GAU(double x, double height, double center, double standard_deviation) {                              // 6
        return height * Math.pow(Math.E, -Math.pow(x-center, 2)/(2*Math.pow(standard_deviation, 2)));
    }
    static double LAT(double x) {                                                                                       // 7
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
        for (int i = 0; i < Neurons.lastElement().size(); i++) {
            double neuron = Neurons.lastElement().get(i);
            if (neuron > highestValue) {
                highestValue = neuron;
                result = i;
            }
        }
        neuralNetworkAnswer = result;
    }
    static void neuronActivationCalculation(int layer, int index) {
        if (layer != 0) {
            double z;
            if (evolutionTraining) {
                z = 0;      //change
                for (int l = 0; l < layer; l++) {
                    for (int neuron = 0; neuron < Neurons.get(l).size(); neuron++) {
                        if (!Double.isNaN(SynapsesE.get(l).get(neuron).get(layer).get(index))) {
                            z += Neurons.get(l).get(neuron) * SynapsesE.get(l).get(neuron).get(layer).get(index);
                        }
                    }
                }
                zs.get(layer).set(index, z);
                switch (NeuronFunctions.get(layer).get(index)) {
                    case FUNCTION_SIG -> Neurons.get(layer).set(index, SIG(z));
                    case FUNCTION_LIN -> Neurons.get(layer).set(index, LIN(z));
                    case FUNCTION_SQR -> Neurons.get(layer).set(index, SQR(z));
                    case FUNCTION_SIN -> Neurons.get(layer).set(index, SIN(z));
                    case FUNCTION_ABS -> Neurons.get(layer).set(index, ABS(z));
                    case FUNCTION_REL -> Neurons.get(layer).set(index, REL(z));
//                    case FUNCTION_GAU -> Neurons.get(layer).set(index, GAU(z));
//                    case FUNCTION_LAT -> Neurons.get(layer).set(index, LAT(z));
                }
            } else {
                z = -Biases.get(layer).get(index);
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
        Collections.fill(perfectOutput, 0.0);
        perfectOutput.set(correctResult, 1.0);
    }

    static double costFunction() {
        double cost = 0;
        for (int i = 0; i < Neurons.lastElement().size(); i++) {
            cost += Math.pow(Neurons.lastElement().get(i)-perfectOutput.get(i), 2);
        }
        return cost;    //0.0 = Perfect     10.0 = Wrong
    }

    static void backpropagation() {
        for (int i = 0; i < Neurons.lastElement().size(); i++) {
            NeuronNudges.get(Neurons.size()-1).get(i).set(0, Neurons.lastElement().get(i) - perfectOutput.get(i));
        }


        for (int layer = Neurons.size()-1; layer > 0; layer--) {    //Going from the last layer to second
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
        System.out.println("Synapse gradients:\t" + SynapseNudges.lastElement().get(0).get(0).get(0) + "\t" + SynapseNudges.get(SynapseNudges.size()-2).get(0).get(0).get(0) + "\t" + SynapseNudges.get(SynapseNudges.size()-3).get(0).get(0).get(0));
        System.out.println("Bias gradients:\t\t" + BiasNudges.lastElement().get(0).get(0) + "\t" + BiasNudges.get(BiasNudges.size()-2).get(0).get(0) + "\t" + BiasNudges.get(BiasNudges.size()-3).get(0).get(0));

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

            for (int i = 0; i < Neurons.lastElement().size(); i++) {
                double neuron_derivative = Neurons.lastElement().get(i) - perfectOutput.get(i);
                NeuronNudges.get(Neurons.size()-1).get(i).set(image, neuron_derivative);
            }


            for (int layer = Neurons.size()-1; layer > 0; layer--) {    //Going from the last layer to second
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
        costsBefore = new Vector<>();

        double sum = 0;
        for (int i : imagesUsed) {
            readImage(i);
            readAnswer(i);
            neuralNetwork();
            sum += costFunction();
        }
        double new_cost = sum/imagesUsed.size();
        imagesUsed = new Vector<>();

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
            System.out.println("! An unexpected error occurred while trying to save the Neural network ["+save+"]");
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
                successes++;
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
