package Mechanic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import static Libraries.Methods.*;
import static Mechanic.Mechanic.*;

public class MainVariables {
    public static JFrame frame = getFrame("AI Prototype", null, 900, 900, null, null, false);
    public static JFrame nnvFrame = getFrame("Neural Network Visualization", null, 1000, 1000, null, null, false);
    public static DrawPanel drawPanel = new DrawPanel();
    public static NNVPanel nnvPanel = new NNVPanel();

    public static GameThreads.GameLoop gameLoop = new GameThreads.GameLoop();
    public static double millisecondsPerUpdate = 1000d / 60;
    public static boolean gameLoopOn;

    public static Vector<Vector<Double>> Neurons = new Vector<>();
    public static Vector<Vector<Double>> Biases = new Vector<>();
    public static Vector<Vector<Vector<Double>>> Synapses = new Vector<>();
    public static Vector<Vector<Vector<Double>>> SynapseDerivatives = new Vector<>();

    public static int inputs = 2; //784
    public static int outputs = 3; //10
    public static int[] layers = {inputs, 2, outputs}; //16, 16

    public static Vector<Double> perfectOutput = new Vector<>();

    public static Vector<Vector<Double>> Image = new Vector<>();

    public static boolean pressedLMB;
    public static boolean pressedRMB;
    public static int mouseX;
    public static int mouseY;

    public static Point nnvMouseLocation;
    public static int nnvXDisplacement;
    public static int nnvYDisplacement;

    final public static File FONT_ALUNDRA_TEXT = new File("resources"+SEPARATOR+"fonts"+SEPARATOR+"AlundraText.ttf");
    final public static File FONT_SPEAK_HEAVY_TEXT = new File("resources"+SEPARATOR+"fonts"+SEPARATOR+"SpeakHeavy.ttf");
    public static final String FONT_USED = "Speak-Heavy";


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
                    break;
                case 'r':
                    for (int i = 0; i < Neurons.get(0).size(); i++) {
                        double rand = Math.random();
                        Neurons.get(0).set(i, rand);
                    }
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
}
