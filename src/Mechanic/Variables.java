package Mechanic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import static Libraries.Methods.*;
import static Mechanic.Mechanics.*;
import static Mechanic.Panels.*;

public abstract class Variables {
    public static JFrame frame = getFrame("AI Prototype", null, 900, 900, null, null, false);
    public static JFrame nnvFrame = getFrame("Neural Network Visualization", null, 1000, 1000, null, null, false);
    public static JFrame modelFrame = getFrame("Neural Network Model", null, 1000, 1000, null, null, false);
    public static DrawPanel drawPanel = new DrawPanel();
    public static NNVPanel nnvPanel = new NNVPanel();
    public static ModelPanel modelPanel = new ModelPanel();

    public static final int MODE_OPTIMIZATION = 0;
    public static final int MODE_VISUALIZATION = 1;
    public static int mode;

    public static Threads.GameLoop gameLoop = new Threads.GameLoop();
    public static boolean gameLoopOn;
    public static Threads.ModelGameLoop modelGameLoop = new Threads.ModelGameLoop();
    public static boolean modelGameLoopOn;

    public static Vector<Vector<Double>> Neurons = new Vector<>();
    public static Vector<Vector<Double>> Biases = new Vector<>();
    public static Vector<Vector<Vector<Double>>> Synapses = new Vector<>();

    public static Vector<Vector<Double>> zs = new Vector<>();

    public static Vector<Vector<Vector<Double>>> BiasNudges = new Vector<>();
    public static Vector<Vector<Vector<Vector<Double>>>> SynapseNudges = new Vector<>();
    public static Vector<Vector<Vector<Double>>> NeuronNudges = new Vector<>();

    public static int[] layers = {784, 16, 16, 10};

    public static int neuralNetworkAnswer = -1;
    public static Vector<Double> perfectOutput = new Vector<>();
    public static int rightAnswer;

    public static int[] initial_layers = new int[]{784, 10};

    public static int addActions;
    public static int changeActions;
    public static int disableActions;
    public static int neuronActions;
    public static int functionActions;
    public static int removeActions;

    public static Vector<Integer> trainingImages;

    public static Vector<Vector<Integer>> NeuronFunctions = new Vector<>();
    public static Vector<Vector<Vector<Vector<Double>>>> SynapsesE = new Vector<>();

    public static boolean evolutionTraining;
    public static int generation_number;
    public static int clone_number;
    public static int trainingImagesNumber;

    public static Vector<Vector<Vector<Integer>>> EvolutionNeurons = new Vector<>();
    public static Vector<Vector<Vector<Vector<Vector<Double>>>>> EvolutionSynapses = new Vector<>();

    public static Vector<Vector<Integer>> BestCloneNeurons;
    public static Vector<Vector<Vector<Vector<Double>>>> BestCloneSynapses;

    public static final int FUNCTION_SIG = 0;
    public static final int FUNCTION_LIN = 1;
    public static final int FUNCTION_SQR = 2;
    public static final int FUNCTION_SIN = 3;
    public static final int FUNCTION_ABS = 4;
    public static final int FUNCTION_REL = 5;
    public static final int FUNCTION_GAU = 6;
    public static final int FUNCTION_LAT = 7;

    public static Vector<Integer> imagesUsed = new Vector<>();
    public static Vector<Double> costsBefore = new Vector<>();

    public static Vector<Double> Costs = new Vector<>();
    public static int TestingThreadCompletes;
    public static int TestingThreadCorrects;

    public static int butch = 12;   //High = effective + long learning    Low = bad + fast learning

    public static boolean neuralNetworkSetUp;

    public static boolean pressedLMB;
    public static boolean pressedRMB;
    public static int mouseX;
    public static int mouseY;

    public static Point nnvMouseLocation;
    public static int nnvXDisplacement;
    public static int nnvYDisplacement;

    final public static File images = new File("resources"+SEPARATOR+"training data"+SEPARATOR+"train-images.idx3-ubyte");
    public static int imageNumber;
    final public static File labels = new File("resources"+SEPARATOR+"training data"+SEPARATOR+"train-labels.idx1-ubyte");

    public static byte[] imageBytes;
    public static byte[] labelBytes;

    final public static File FONT_SPEAK_HEAVY_TEXT = new File("resources"+SEPARATOR+"fonts"+SEPARATOR+"SpeakHeavy.ttf");
    final public static String FONT_USED = "Speak-Heavy";

    final public static File neuralNetworkSave = new File("resources"+SEPARATOR+"neural networks"+SEPARATOR+"neural_network.txt");
    final public static File evolutionaryNeuralNetworkSave = new File("resources"+SEPARATOR+"neural networks"+SEPARATOR+"evolutionary_neural_network.txt");

    final public static File soundFile = new File("resources"+SEPARATOR+"sounds"+SEPARATOR+"Volume Alpha 08. Minecraft.mp3");


    public static class DrawKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case '0':
                    setPerfectOutput(0);
                    break;
                case '1':
                    setPerfectOutput(1);
                    break;
                case '2':
                    setPerfectOutput(2);
                    break;
                case '3':
                    setPerfectOutput(3);
                    break;
                case '4':
                    setPerfectOutput(4);
                    break;
                case '5':
                    setPerfectOutput(5);
                    break;
                case '6':
                    setPerfectOutput(6);
                    break;
                case '7':
                    setPerfectOutput(7);
                    break;
                case '8':
                    setPerfectOutput(8);
                    break;
                case '9':
                    setPerfectOutput(9);
                    break;
                case ' ':
                    neuralNetwork();
                    System.out.println(neuralNetworkAnswer);
                    break;
                case 'r':
                    for (int i = 0; i < Neurons.get(0).size(); i++) {
                        double rand = Math.random();
                        Neurons.get(0).set(i, rand);
                    }
                    break;
                case 'i':
                    imageNumber = (int) (Math.random()*60000);
                    readImage(imageNumber);
                    readAnswer(imageNumber);
                    neuralNetwork();
                    break;
                case 'n':
                    imageNumber++;
                    readImage(imageNumber);
                    readAnswer(imageNumber);
                    neuralNetwork();
                case 'b':
                    backpropagation();
                    break;
                default:
                    System.out.println(e.getKeyChar());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public static class DrawMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    pressedLMB = true;
                    break;
                case MouseEvent.BUTTON3:
                    pressedRMB = true;
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    pressedLMB = false;
                    break;
                case MouseEvent.BUTTON3:
                    pressedRMB = false;
                    break;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static class DrawMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }


    public static class NNVMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            nnvMouseLocation = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static class NNVMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - nnvMouseLocation.x;
            int dy = e.getY() - nnvMouseLocation.y;
            nnvMouseLocation = e.getPoint();

            nnvXDisplacement += dx;
            nnvYDisplacement += dy;
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public static class NNVMouseWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }
    }


    public static class ModelMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static class ModelMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public static class ModelMouseWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }
    }
}
