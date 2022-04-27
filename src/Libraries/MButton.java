package Libraries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MButton extends JComponent implements MouseListener {
    public int style;
    public static final int STYLE_BUTTON = 0;
    public static final int STYLE_TASK = 1;

    public String text;
    public int fontSize;

    public Dimension size;

    public Point position;

    public Container container;

    private boolean entered;
    private boolean pressed;

    public MButton(Container container, int style, Point position, Dimension size, String text, int fontSize) {
        super();

        this.container = container;
        this.style = style;
        this.position = position;
        this.size = size;
        container.add(this);
        this.setBounds(position.x, position.y, size.width, size.height);
        System.out.println(this.position.x + " " + this.position.y + " " + this.size.width + " " + this.size.height);

        this.text = text;
        this.fontSize = fontSize;

        enableInputMethods(true);
        addMouseListener(this);

        setSize(size.width, size.height);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

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
        entered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        entered = false;
    }
}
