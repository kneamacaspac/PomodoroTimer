import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Buttons extends JButton {
    public Buttons(String label) {
        super(label);
        setContentAreaFilled(false); // Don't fill the default button background
        setFocusPainted(false);      // Remove the focus rectangle
        setBorderPainted(false);     // Optional: remove border painting
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color
        if (getModel().isArmed()) {
            g2.setColor(new Color(143, 95, 0));
        } else {
            g2.setColor(getBackground());
        }

        // Draw rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // last 2 values = arc width and height

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: draw border (or leave blank to remove)
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
        return shape.contains(x, y);
    }
}
