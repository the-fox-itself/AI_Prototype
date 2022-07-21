package Mechanic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import static Mechanic.Variables.*;
import static Mechanic.Mechanics.neuralNetwork;
import static Mechanic.Mechanics.*;

public class Threads {
    public static class GameLoop extends Thread {
        @Override
        public void run() {
            super.run();

            while (true) {
                if (gameLoopOn) {
                    double loopStartTime = new Date().getTime();

                    handleInput();

                    frame.repaint();
                    nnvFrame.repaint();

                    double loopSlot = 10;
                    double endTime = loopStartTime + loopSlot;
                    while (new Date().getTime() < endTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
        public void handleInput() {
            if (pressedLMB && mouseX <= 28*25 && mouseX >= 0 && mouseY <= 28*25 && mouseY >= 0) {
                if (Neurons.get(0).get(mouseX/25 + mouseY/25*28)+0.01 <= 1.0) {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28) + 0.1);
                    neuralNetwork();
//                    if ((mouseX / 25 + mouseY / 25 * 28) % 28 != 0) {         //left
//                        if (Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 - 1)+0.01 <= 1.0)
//                            Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28 - 1, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 - 1) + 0.01);
//                    }
//                    if (mouseX / 25 + mouseY / 25 * 28 > 27) {               //up
//                        if (Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 - 28)+0.01 <= 1.0)
//                            Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28 - 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 - 28) + 0.01);
//                    }
//                    if ((mouseX / 25 + mouseY / 25 * 28 + 1) % 28 != 0) {     //right
//                        if (Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 + 1)+0.01 <= 1.0)
//                            Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28 + 1, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 + 1) + 0.01);
//                    }
//                    if (mouseX / 25 + mouseY / 25 * 28 < 756) {             //down
//                        if (Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 + 28)+0.01 <= 1.0)
//                            Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28 + 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28 + 28) + 0.01);
//                    }
                } else {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, 1.0);
                    neuralNetwork();
                }
            } else if (pressedRMB && mouseX <= 28*25 && mouseX >= 0 && mouseY <= 28*25 && mouseY >= 0) {
                if (Neurons.get(0).get(mouseX/25 + mouseY/25*28)-0.01 >= 0.0) {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28) - 0.1);
                    neuralNetwork();
                } else {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, 0.0);
                    neuralNetwork();
                }
            }
        }
    }

    public static class ModelGameLoop extends Thread {
        @Override
        public void run() {
            super.run();

            while (true) {
                if (modelGameLoopOn) {
                    double loopStartTime = new Date().getTime();

                    handleInput();

                    modelFrame.repaint();

                    double loopSlot = 10;
                    double endTime = loopStartTime + loopSlot;
                    while (new Date().getTime() < endTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
        public void handleInput() {

        }
    }

    public static class CloneThread extends Thread {
        CloneThread(int clone) {
            this.firstClone = clone;
            this.lastClone = clone;
        }
        CloneThread(int firstClone, int lastClone) {
            this.firstClone = firstClone;
            this.lastClone = lastClone;
        }

        Vector<Vector<Double>> TestingNeurons = new Vector<>();
        Vector<Vector<Integer>> TestingNeuronFunctions = new Vector<>();
        Vector<Vector<Vector<Vector<Double>>>> TestingSynapsesE = new Vector<>();
        Vector<Vector<Double>> TestingZs = new Vector<>();
        Vector<Double> TestingPerfectOutput = new Vector<>();
        int testingRightAnswer;
        int testingNeuralNetworkAnswer = -1;

        int firstClone;
        int lastClone;

        @Override
        public void run() {
            super.run();

            long clock = new Date().getTime();

            for (int clone = firstClone; clone < lastClone + 1; clone++) {
                int mutation_number;
                if (clone == 0)
                    mutation_number = 0;
                else if (clone == clone_number - 1)
                    mutation_number = 50;
                else if (clone > clone_number / 7 * 6)
                    mutation_number = 3;
                else if (clone > clone_number / 7 * 4)
                    mutation_number = 2;
                else
                    mutation_number = 1;
                for (int mutation = 0; mutation < mutation_number; mutation++) {       //~ 200-300 mutations / s
                    Vector<Vector<Vector<Vector<Double>>>> CloneSynapses = EvolutionSynapses.get(clone);        //Translates values to EvolutionSynapses and EvolutionNeurons automatically
                    Vector<Vector<Integer>> CloneNeurons = EvolutionNeurons.get(clone);

                    //Figuring out what kinds of mutations are possible
                    ArrayList<int[]> missingSynapses = new ArrayList<>();
                    ArrayList<int[]> existingSynapses = new ArrayList<>();
                    ArrayList<int[]> existingNeurons = new ArrayList<>();
                    int next = 0;
                    int add = -1;
                    int change = -1;
                    int disable = -1;
                    int neuron = -1;
                    int function = -1;
                    int remove = -1;
                    for (int j = 0; j < CloneSynapses.size(); j++) {
                        for (int j1 = 0; j1 < CloneSynapses.get(j).size(); j1++) {
                            for (int j2 = j + 1; j2 < CloneSynapses.get(j).get(j1).size(); j2++) {
                                for (int j3 = 0; j3 < CloneSynapses.get(j).get(j1).get(j2).size(); j3++) {
                                    double weight = CloneSynapses.get(j).get(j1).get(j2).get(j3);
                                    if (Double.isNaN(weight)) {
                                        if (add == -1) {
                                            add = next;
                                            next++;
                                        }
                                        missingSynapses.add(new int[]{j, j1, j2, j3});
                                    } else {
                                        if (change == -1) {
                                            change = next;
                                            next++;
                                        }
                                        if (disable == -1) {
                                            disable = next;
                                            next++;
                                        }
                                        if (neuron == -1) {
                                            neuron = next;
                                            next++;
                                        }
                                        existingSynapses.add(new int[]{j, j1, j2, j3});
                                    }
                                }
                            }
                        }
                    }
                    for (int layer = 1; layer < CloneNeurons.size(); layer++) {
                        for (int neuron1 = 0; neuron1 < CloneNeurons.get(layer).size(); neuron1++) {
                            if (function == -1) {
                                function = next;
                                next++;
                            }
                            if (remove == -1) {
                                remove = next;
                                next++;
                            }
                            existingNeurons.add(new int[]{layer, neuron1});
                        }
                    }

                    //Applying a random mutation
                    int action = (int) (Math.random() * next);
                    if (action == add) {                //Add synapse                           (if there is a place to add one)    DONE
                        int randomMissingSynapse = (int) (Math.random() * missingSynapses.size());
                        double randomWeight = Math.random() * 2 - 1;
                        int[] missingSynapse = missingSynapses.get(randomMissingSynapse);
                        if (Double.isNaN(CloneSynapses.get(missingSynapse[0]).get(missingSynapse[1]).get(missingSynapse[2]).get(missingSynapse[3]))) {
                            CloneSynapses.get(missingSynapse[0]).get(missingSynapse[1]).get(missingSynapse[2]).set(missingSynapse[3], randomWeight);
                            addActions++;
                        } else
                            System.out.println("! Algorithm error: line 335");
                    } else if (action == change) {      //Change synapse's weight               (if there is a synapse) + should be privileged  DONE
                        int randomSynapse = (int) (Math.random() * existingSynapses.size());
                        double randomWeightChange = Math.random() * 1 - 0.5;
                        int[] synapse = existingSynapses.get(randomSynapse);
                        double weightValue = CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).get(synapse[3]);
                        if (!Double.isNaN(weightValue)) {
                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).set(synapse[3], weightValue + randomWeightChange);
                            changeActions++;
                        } else
                            System.out.println("! Algorithm error: line 346");
                    } else if (action == disable) {     //Disable synapse                       (if there is a synapse)     DONE
                        int randomSynapse = (int) (Math.random() * existingSynapses.size());
                        int[] synapse = existingSynapses.get(randomSynapse);
                        double weightValue = CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).get(synapse[3]);
                        if (!Double.isNaN(weightValue)) {
                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).set(synapse[3], Double.NaN);
                            disableActions++;
                        } else
                            System.out.println("! Algorithm error: line 356");
                    } else if (action == neuron) {      //Add a hidden neuron on a synapse      (if there is a synapse)     DONE
                        int randomSynapse = (int) (Math.random() * existingSynapses.size());
                        int[] synapse = existingSynapses.get(randomSynapse);
                        int randomFunction = (int) (Math.random() * 6);
                        double weight = CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).get(synapse[3]);
                        if (synapse[2] - synapse[0] > 1) {
                            int length = CloneNeurons.get(synapse[2] - 1).size();
                            CloneNeurons.get(synapse[2] - 1).add(randomFunction);
                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).set(synapse[3], Double.NaN);

                            for (int startLayer = 0; startLayer < synapse[2] - 1; startLayer++) {                     //Resizing the CloneSynapses array to create a new ending neuron
                                for (int startNeuron = 0; startNeuron < CloneSynapses.get(startLayer).size(); startNeuron++) {
                                    CloneSynapses.get(startLayer).get(startNeuron).get(synapse[2] - 1).setSize(length + 1);
                                    CloneSynapses.get(startLayer).get(startNeuron).get(synapse[2] - 1).set(length, Double.NaN);
                                }
                            }
                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2] - 1).set(length, weight);

                            CloneSynapses.get(synapse[2] - 1).setSize(length + 1);                                    //Resizing the CloneSynapses array to create a new starting neuron
                            CloneSynapses.get(synapse[2] - 1).set(length, new Vector<>());
                            CloneSynapses.get(synapse[2] - 1).get(length).setSize(CloneSynapses.size() + 1);
                            for (int endLayer = synapse[2]; endLayer < CloneSynapses.get(synapse[2] - 1).get(length).size(); endLayer++) {
                                CloneSynapses.get(synapse[2] - 1).get(length).set(endLayer, new Vector<>());
                                CloneSynapses.get(synapse[2] - 1).get(length).get(endLayer).setSize(CloneSynapses.get(0).get(0).get(endLayer).size());  //bug
                                for (int endNeuron = 0; endNeuron < CloneSynapses.get(synapse[2] - 1).get(length).get(endLayer).size(); endNeuron++) {
                                    CloneSynapses.get(synapse[2] - 1).get(length).get(endLayer).set(endNeuron, Double.NaN);
                                }
                            }
                            CloneSynapses.get(synapse[2] - 1).get(length).get(synapse[2]).set(synapse[3], 1.0);

                            neuronActions++;
                        } else if (synapse[2] - synapse[0] == 1) {
                            CloneNeurons.setSize(CloneNeurons.size() + 1);                                            //Resizing the CloneNeurons array to create a new layer
                            CloneNeurons.set(CloneNeurons.size() - 1, new Vector<>());
                            for (int layer = CloneNeurons.size() - 1; layer > synapse[2]; layer--) {                //Hidden neuron movement
                                for (int hiddenNeuron = 0; hiddenNeuron < CloneNeurons.get(layer - 1).size(); hiddenNeuron++) {
                                    int value = CloneNeurons.get(layer - 1).get(hiddenNeuron);
                                    CloneNeurons.get(layer - 1).remove(hiddenNeuron);
                                    hiddenNeuron -= 1;
                                    CloneNeurons.get(layer).add(value);
                                }
                            }
                            CloneNeurons.get(synapse[2]).add(randomFunction);

                            CloneSynapses.setSize(CloneSynapses.size() + 1);
                            CloneSynapses.set(CloneSynapses.size() - 1, new Vector<>());
                            //Synapse movement
                            for (int startLayer = 0; startLayer < synapse[2]; startLayer++) {                       //Layers before the endLayer of the modified synapse
                                for (int startNeuron = 0; startNeuron < CloneSynapses.get(startLayer).size(); startNeuron++) {
                                    CloneSynapses.get(startLayer).get(startNeuron).setSize(CloneSynapses.get(startLayer).get(startNeuron).size() + 1);
                                    CloneSynapses.get(startLayer).get(startNeuron).set(CloneSynapses.get(startLayer).get(startNeuron).size() - 1, new Vector<>());

                                    for (int endLayer = CloneSynapses.get(startLayer).get(startNeuron).size() - 1; endLayer > synapse[2]; endLayer--) {
                                        CloneSynapses.get(startLayer).get(startNeuron).get(endLayer).setSize(CloneSynapses.get(startLayer).get(startNeuron).get(endLayer - 1).size());
                                        for (int endNeuron = 0; endNeuron < CloneSynapses.get(startLayer).get(startNeuron).get(endLayer - 1).size(); endNeuron++) {
                                            CloneSynapses.get(startLayer).get(startNeuron).get(endLayer).set(endNeuron, CloneSynapses.get(startLayer).get(startNeuron).get(endLayer - 1).get(endNeuron));
                                            CloneSynapses.get(startLayer).get(startNeuron).get(endLayer - 1).set(endNeuron, Double.NaN);
                                        }
                                    }
                                    CloneSynapses.get(startLayer).get(startNeuron).get(synapse[2]).setSize(1);
                                }
                            }
                            for (int startLayer = CloneSynapses.size() - 1; startLayer > synapse[2]; startLayer--) {
                                CloneSynapses.get(startLayer).setSize(CloneSynapses.get(startLayer - 1).size());
                                for (int startNeuron = 0; startNeuron < CloneSynapses.get(startLayer - 1).size(); startNeuron++) {
                                    CloneSynapses.get(startLayer).set(startNeuron, new Vector<>());
                                    CloneSynapses.get(startLayer).get(startNeuron).setSize(CloneSynapses.size() + 1);
                                    for (int endLayer = CloneSynapses.get(startLayer).get(startNeuron).size() - 1; endLayer > startLayer; endLayer--) {
                                        CloneSynapses.get(startLayer).get(startNeuron).set(endLayer, new Vector<>());
                                        CloneSynapses.get(startLayer).get(startNeuron).get(endLayer).setSize(CloneSynapses.get(startLayer - 1).get(startNeuron).get(endLayer - 1).size());
                                        for (int endNeuron = 0; endNeuron < CloneSynapses.get(startLayer).get(startNeuron).get(endLayer).size(); endNeuron++) {
                                            CloneSynapses.get(startLayer).get(startNeuron).get(endLayer).set(endNeuron, CloneSynapses.get(startLayer - 1).get(startNeuron).get(endLayer - 1).get(endNeuron));
                                            CloneSynapses.get(startLayer - 1).get(startNeuron).get(endLayer - 1).set(endNeuron, Double.NaN);
                                        }
                                    }
                                }
                            }
                            CloneSynapses.get(synapse[2]).setSize(1);
                            CloneSynapses.get(synapse[2]).set(0, new Vector<>());
                            CloneSynapses.get(synapse[2]).get(0).setSize(CloneSynapses.size() + 1);
                            for (int endLayer = synapse[2] + 1; endLayer < CloneSynapses.get(synapse[2]).get(0).size(); endLayer++) {
                                CloneSynapses.get(synapse[2]).get(0).set(endLayer, new Vector<>());
                                CloneSynapses.get(synapse[2]).get(0).get(endLayer).setSize(CloneSynapses.get(synapse[2] - 1).get(0).get(endLayer).size());
                                for (int endNeuron = 0; endNeuron < CloneSynapses.get(synapse[2]).get(0).get(endLayer).size(); endNeuron++) {
                                    CloneSynapses.get(synapse[2]).get(0).get(endLayer).set(endNeuron, Double.NaN);
                                }
                            }

                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2] + 1).set(synapse[3], Double.NaN);
                            CloneSynapses.get(synapse[0]).get(synapse[1]).get(synapse[2]).set(0, weight);
                            CloneSynapses.get(synapse[2]).get(0).get(synapse[2] + 1).set(synapse[3], 1.0);
                            neuronActions++;
                        } else {
                            System.out.println("! Algorithm error: line 402");
                        }
                    } else if (action == function) {    //Change neuron's activation function   (if there is a neuron)  DONE
                        int randomNeuron = (int) (Math.random() * existingNeurons.size());
                        int[] hiddenNeuron = existingNeurons.get(randomNeuron);
                        int functionIndex = CloneNeurons.get(hiddenNeuron[0]).get(hiddenNeuron[1]);
                        int randomFunction = (int) (Math.random() * 5);
                        if (randomFunction >= functionIndex)
                            randomFunction++;
                        CloneNeurons.get(hiddenNeuron[0]).set(hiddenNeuron[1], randomFunction);
                        functionActions++;
                    } else if (action == remove) {      //Remove a hidden neuron                (if there is a neuron)  DONE
                        int randomNeuron = (int) (Math.random() * existingNeurons.size());
                        int[] hiddenNeuron = existingNeurons.get(randomNeuron);
                        for (int startLayer = 0; startLayer < hiddenNeuron[0]; startLayer++) {
                            for (int startNeuron = 0; startNeuron < CloneSynapses.get(startLayer).size(); startNeuron++) {
                                for (int endNeuron = hiddenNeuron[1]; endNeuron < CloneSynapses.get(startLayer).get(startNeuron).get(hiddenNeuron[0]).size() - 1; endNeuron++) {
                                    CloneSynapses.get(startLayer).get(startNeuron).get(hiddenNeuron[0]).set(endNeuron, CloneSynapses.get(startLayer).get(startNeuron).get(hiddenNeuron[0]).get(endNeuron + 1));
                                }
                                CloneSynapses.get(startLayer).get(startNeuron).get(hiddenNeuron[0]).setSize(CloneSynapses.get(startLayer).get(startNeuron).get(hiddenNeuron[0]).size() - 1);
                            }
                        }
                        for (int startNeuron = hiddenNeuron[1]; startNeuron < CloneSynapses.get(hiddenNeuron[0]).size() - 1; startNeuron++) {
                            for (int endLayer = hiddenNeuron[0] + 1; endLayer < CloneSynapses.get(hiddenNeuron[0]).get(startNeuron).size(); endLayer++) {
                                for (int endNeuron = 0; endNeuron < CloneSynapses.get(hiddenNeuron[0]).get(startNeuron).get(endLayer).size(); endNeuron++) {
                                    CloneSynapses.get(hiddenNeuron[0]).get(startNeuron).get(endLayer).set(endNeuron, CloneSynapses.get(hiddenNeuron[0]).get(startNeuron + 1).get(endLayer).get(endNeuron));
                                }
                            }
                        }
                        CloneSynapses.get(hiddenNeuron[0]).setSize(CloneSynapses.get(hiddenNeuron[0]).size() - 1);
                        CloneNeurons.get(hiddenNeuron[0]).remove(hiddenNeuron[1]);
                        removeActions++;
                    } else {
                        System.out.println("! Algorithm error: line 430");
                    }
                }


                double costSum = 0;
                TestingNeurons = new Vector<>();
                TestingNeurons.setSize(EvolutionNeurons.get(clone).size() + 1);
                TestingNeurons.replaceAll(ignored -> new Vector<>());
                TestingNeurons.get(0).setSize(initial_layers[0]);
                TestingNeurons.lastElement().setSize(initial_layers[initial_layers.length - 1]);
                for (int layer = 1; layer < TestingNeurons.size() - 1; layer++) {
                    TestingNeurons.get(layer).setSize(EvolutionNeurons.get(clone).get(layer).size());
                }

                TestingNeuronFunctions = new Vector<>();
                TestingNeuronFunctions.setSize(EvolutionNeurons.get(clone).size() + 1);
                TestingNeuronFunctions.set(TestingNeuronFunctions.size() - 1, new Vector<>());
                TestingNeuronFunctions.lastElement().setSize(initial_layers[initial_layers.length - 1]);
                Collections.fill(TestingNeuronFunctions.lastElement(), FUNCTION_SIG);
                for (int layer = 1; layer < TestingNeuronFunctions.size() - 1; layer++) {
                    TestingNeuronFunctions.set(layer, new Vector<>());
                    TestingNeuronFunctions.get(layer).setSize(EvolutionNeurons.get(clone).get(layer).size());
                    for (int neuron = 0; neuron < TestingNeuronFunctions.get(layer).size(); neuron++) {
                        TestingNeuronFunctions.get(layer).set(neuron, EvolutionNeurons.get(clone).get(layer).get(neuron));
                    }
                }

                TestingSynapsesE = EvolutionSynapses.get(clone);

                TestingZs.setSize(TestingNeurons.size());
                for (int layer = 1; layer < TestingZs.size(); layer++) {
                    TestingZs.set(layer, new Vector<>());
                    TestingZs.get(layer).setSize(TestingNeurons.get(layer).size());
                }
                TestingPerfectOutput.setSize(initial_layers[initial_layers.length - 1]);
                evolutionTraining = true;

                for (int image : trainingImages) {        //~ 1000 images / s
                    testingReadImage(image);
                    testingReadAnswer(image);
                    testingNeuralNetwork();
                    costSum += testingCostFunction();
                }

                double totalCost = costSum / trainingImages.size();
                Costs.set(clone, totalCost);
//                System.out.println(Costs);
            }
//            System.out.println((double) (new Date().getTime()-clock)/1000 + " s - clone " + clone + " done");
        }

        void testingReadImage(int num) {
            short index = 0;
            for (int i = 16 + 784 * num; i < 16 + 784 * (num + 1); i++) {
                double color = imageBytes[i];
                if (imageBytes[i] < 0) {
                    color += 256;
                }
                TestingNeurons.get(0).set(index, color / 255);
                index++;
            }
        }
        void testingReadAnswer(int num) {
            testingRightAnswer = labelBytes[8 + num];
            testingSetPerfectOutput(testingRightAnswer);
        }
        void testingSetPerfectOutput(int correctResult) {
            Collections.fill(TestingPerfectOutput, 0.0);
            TestingPerfectOutput.set(correctResult, 1.0);
        }
        void testingNeuralNetwork() {
            for (int layer = 1; layer < TestingNeurons.size(); layer++) {
                for (int neuron = 0; neuron < TestingNeurons.get(layer).size(); neuron++) {
                    testingNeuronActivationCalculation(layer, neuron);
                }
            }
            double highestValue = 0;
            int result = 0;
            for (int i = 0; i < TestingNeurons.lastElement().size(); i++) {
                double neuron = TestingNeurons.lastElement().get(i);
                if (neuron > highestValue) {
                    highestValue = neuron;
                    result = i;
                }
            }
            testingNeuralNetworkAnswer = result;
        }
        void testingNeuronActivationCalculation(int layer, int index) {
            if (layer != 0) {
                double z = 0;
                for (int l = 0; l < layer; l++) {
                    for (int neuron = 0; neuron < TestingNeurons.get(l).size(); neuron++) {
                        if (!Double.isNaN(TestingSynapsesE.get(l).get(neuron).get(layer).get(index))) {
                            z += TestingNeurons.get(l).get(neuron) * TestingSynapsesE.get(l).get(neuron).get(layer).get(index);
                        }
                    }
                }
                TestingZs.get(layer).set(index, z);
                switch (TestingNeuronFunctions.get(layer).get(index)) {
                    case FUNCTION_SIG -> TestingNeurons.get(layer).set(index, SIG(z));
                    case FUNCTION_LIN -> TestingNeurons.get(layer).set(index, LIN(z));
                    case FUNCTION_SQR -> TestingNeurons.get(layer).set(index, SQR(z));
                    case FUNCTION_SIN -> TestingNeurons.get(layer).set(index, SIN(z));
                    case FUNCTION_ABS -> TestingNeurons.get(layer).set(index, ABS(z));
                    case FUNCTION_REL -> TestingNeurons.get(layer).set(index, REL(z));
//                    case FUNCTION_GAU -> TestingNeurons.get(layer).set(index, GAU(z));
//                    case FUNCTION_LAT -> TestingNeurons.get(layer).set(index, LAT(z));
                }
            }
        }
        double testingCostFunction() {
            double cost = 0;
            for (int i = 0; i < TestingNeurons.lastElement().size(); i++) {
                cost += Math.pow(TestingNeurons.lastElement().get(i)-TestingPerfectOutput.get(i), 2);
            }
            return cost;
        }
    }

    public static class TestingThread extends Thread {
        TestingThread(int image) {
            this.firstImage = image;
            this.lastImage = image;
        }
        TestingThread(int firstImage, int lastImage) {
            this.firstImage = firstImage;
            this.lastImage = lastImage;
        }

        int firstImage;
        int lastImage;

        Vector<Vector<Double>> TestingNeurons = new Vector<>();
        Vector<Vector<Integer>> TestingNeuronFunctions = new Vector<>();
        Vector<Vector<Vector<Vector<Double>>>> TestingSynapsesE = new Vector<>();
        Vector<Vector<Double>> TestingZs = new Vector<>();
        Vector<Double> TestingPerfectOutput = new Vector<>();
        int testingRightAnswer;
        int testingNeuralNetworkAnswer = -1;

        @Override
        public void run() {
            super.run();

            TestingNeurons = new Vector<>();
            TestingNeurons.setSize(BestCloneNeurons.size() + 1);
            TestingNeurons.replaceAll(ignored -> new Vector<>());
            TestingNeurons.get(0).setSize(initial_layers[0]);
            TestingNeurons.lastElement().setSize(initial_layers[initial_layers.length - 1]);
            for (int layer = 1; layer < TestingNeurons.size() - 1; layer++) {
                TestingNeurons.get(layer).setSize(BestCloneNeurons.get(layer).size());
            }

            TestingNeuronFunctions = new Vector<>();
            TestingNeuronFunctions.setSize(BestCloneNeurons.size() + 1);
            TestingNeuronFunctions.set(TestingNeuronFunctions.size() - 1, new Vector<>());
            TestingNeuronFunctions.lastElement().setSize(initial_layers[initial_layers.length - 1]);
            Collections.fill(TestingNeuronFunctions.lastElement(), FUNCTION_SIG);
            for (int layer = 1; layer < TestingNeuronFunctions.size() - 1; layer++) {
                TestingNeuronFunctions.set(layer, new Vector<>());
                TestingNeuronFunctions.get(layer).setSize(BestCloneNeurons.get(layer).size());
                for (int neuron = 0; neuron < TestingNeuronFunctions.get(layer).size(); neuron++) {
                    TestingNeuronFunctions.get(layer).set(neuron, BestCloneNeurons.get(layer).get(neuron));
                }
            }

            TestingSynapsesE = BestCloneSynapses;

            TestingZs.setSize(TestingNeurons.size());
            for (int layer = 1; layer < TestingZs.size(); layer++) {
                TestingZs.set(layer, new Vector<>());
                TestingZs.get(layer).setSize(TestingNeurons.get(layer).size());
            }
            TestingPerfectOutput.setSize(initial_layers[initial_layers.length - 1]);
//            evolutionTraining = true;

//            TestingNeurons = (Vector<Vector<Double>>) Neurons.clone();
//            TestingNeuronFunctions = (Vector<Vector<Integer>>) NeuronFunctions.clone();
//            TestingSynapsesE = (Vector<Vector<Vector<Vector<Double>>>>) SynapsesE.clone();
//            TestingZs = (Vector<Vector<Double>>) zs.clone();
//            TestingPerfectOutput = (Vector<Double>) perfectOutput.clone();

            for (int image = firstImage; image < lastImage + 1; image++) {
                testingReadAnswer(image);
                testingReadImage(image);
                testingNeuralNetwork();
                if (neuralNetworkAnswer == rightAnswer) {
                    TestingThreadCorrects++;
                }
                TestingThreadCompletes++;
            }
            System.out.println("TestingThread finished == firstImage:"+firstImage+" ; lastImage:"+lastImage);
        }

        void testingReadImage(int num) {
            short index = 0;
            for (int i = 16 + 784 * num; i < 16 + 784 * (num + 1); i++) {
                double color = imageBytes[i];
                if (imageBytes[i] < 0) {
                    color += 256;
                }
                TestingNeurons.get(0).set(index, color / 255);
                index++;
            }
        }
        void testingReadAnswer(int num) {
            testingRightAnswer = labelBytes[8 + num];
            testingSetPerfectOutput(testingRightAnswer);
        }
        void testingSetPerfectOutput(int correctResult) {
            Collections.fill(TestingPerfectOutput, 0.0);
            TestingPerfectOutput.set(correctResult, 1.0);
        }
        void testingNeuralNetwork() {
            for (int i = 1; i < TestingNeurons.size(); i++) {
                for (int i1 = 0; i1 < TestingNeurons.get(i).size(); i1++) {
                    testingNeuronActivationCalculation(i, i1);
                }
            }
            double highestValue = 0;
            int result = 0;
            for (int i = 0; i < TestingNeurons.lastElement().size(); i++) {
                double neuron = TestingNeurons.lastElement().get(i);
                if (neuron > highestValue) {
                    highestValue = neuron;
                    result = i;
                }
            }
            testingNeuralNetworkAnswer = result;
        }
        void testingNeuronActivationCalculation(int layer, int index) {
            if (layer != 0) {
                double z = 0;
                for (int l = 0; l < layer; l++) {
                    for (int neuron = 0; neuron < TestingNeurons.get(l).size(); neuron++) {
                        if (!Double.isNaN(TestingSynapsesE.get(l).get(neuron).get(layer).get(index))) {
                            z += TestingNeurons.get(l).get(neuron) * TestingSynapsesE.get(l).get(neuron).get(layer).get(index);
                        }
                    }
                }
                TestingZs.get(layer).set(index, z);
                switch (TestingNeuronFunctions.get(layer).get(index)) {
                    case FUNCTION_SIG -> TestingNeurons.get(layer).set(index, SIG(z));
                    case FUNCTION_LIN -> TestingNeurons.get(layer).set(index, LIN(z));
                    case FUNCTION_SQR -> TestingNeurons.get(layer).set(index, SQR(z));
                    case FUNCTION_SIN -> TestingNeurons.get(layer).set(index, SIN(z));
                    case FUNCTION_ABS -> TestingNeurons.get(layer).set(index, ABS(z));
                    case FUNCTION_REL -> TestingNeurons.get(layer).set(index, REL(z));
//                    case FUNCTION_GAU -> TestingNeurons.get(layer).set(index, GAU(z));
//                    case FUNCTION_LAT -> TestingNeurons.get(layer).set(index, LAT(z));
                }
            }
        }
    }
}
