package Mechanic;

import java.util.Date;

import static Mechanic.MainVariables.*;

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
            if (pressed) {
                if (Neurons.get(0).get(mouseX/25 + mouseY/25*28)+0.05 <= 1.0)
                    Neurons.get(0).set(mouseX/25 + mouseY/25*28, Neurons.get(0).get(mouseX/25 + mouseY/25*28)+0.05);
            }
        }
        public void updateGameStats() {

        }
    }
}
