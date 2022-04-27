package Mechanic;

import java.util.Date;

import static Mechanic.MainVariables.*;
import static Mechanic.Mechanic.neuralNetwork;

public class GameThreads {
    public static class GameLoop extends Thread {
        public void run() {
            double previous = new Date().getTime();
            double steps = 0;
            while (true) {
                if (gameLoopOn) {
                    double loopStartTime = new Date().getTime();
                    double elapsed = loopStartTime - previous;
                    previous = new Date().getTime();
                    steps += elapsed;

                    handleInput();

                    while (steps >= millisecondsPerUpdate) {
                        updateGameStats();
                        steps -= millisecondsPerUpdate;
                    }

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
                } else {
                    previous = new Date().getTime();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        public void handleInput() {
            if (pressedLMB && mouseX <= 28*25 && mouseX >= 0 && mouseY <= 28*25 && mouseY >= 0) {
                if (Neurons.get(0).get(mouseX/25 + mouseY/25*28)+0.01 <= 1.0) {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28) + 0.01);
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
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, Neurons.get(0).get(mouseX / 25 + mouseY / 25 * 28) - 0.01);
                    neuralNetwork();
                } else {
                    Neurons.get(0).set(mouseX / 25 + mouseY / 25 * 28, 0.0);
                    neuralNetwork();
                }
            }
        }
        public void updateGameStats() {

        }
    }
}
