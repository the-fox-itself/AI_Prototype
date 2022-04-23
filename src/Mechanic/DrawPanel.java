package Mechanic;

import javax.swing.*;
import java.awt.*;

import static Mechanic.MainVariables.*;

public class DrawPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(0));
        g.drawRect(0, 0, 28*25, 28*25);

        for (int i = 0; i < Neurons.get(0).size(); i++) {
            int place = i+1;
            int row = 1;
            while (place > 28) {
                place -= 28;
                row++;
            }
            g.setColor(new Color(255-(int) (Neurons.get(0).get(i)*255), 255-(int) (Neurons.get(0).get(i)*255), 255-(int) (Neurons.get(0).get(i)*255)));
            g.fillRect((place-1)*25, (row-1)*25, 25, 25);
        }
    }
}
