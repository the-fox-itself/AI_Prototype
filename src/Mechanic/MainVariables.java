package Mechanic;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import static Libraries.Methods.*;

public class MainVariables {
    public static JFrame frame = getFrame("AI Prototype", null, 900, 900, null, null, false);
    public static DrawPanel drawPanel = new DrawPanel();

    public static GameThreads.GameLoop gameLoop = new GameThreads.GameLoop();
    public static double millisecondsPerUpdate = 1000d / 60;
    public static boolean gameLoopOn;

    public static Vector<Vector<Double>> Neurons = new Vector<>();
    public static Vector<Vector<Double>> Biases = new Vector<>();
    public static Vector<Vector<Vector<Double>>> Synapses = new Vector<>();

    public static Vector<Vector<Double>> Image = new Vector<>();

    public static boolean pressed;
    public static int mouseX;
    public static int mouseY;


    public static class DrawMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
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
}
