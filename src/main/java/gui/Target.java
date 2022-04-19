package gui;

import java.awt.*;

public class Target extends GameObject {
    private final int m_targetPositionX;
    private final int m_targetPositionY;
    private final int size = 20;
    private Color targetColor = Color.BLUE;

    public Target() {
        this.m_targetPositionX = 0;
        this.m_targetPositionY = 0;
    }

    public Target(int width, int height) {
        Point coordinates = calculateTargetCoordinates(width, height);
        this.m_targetPositionX = coordinates.x;
        this.m_targetPositionY = coordinates.y;
    }

    protected void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(targetColor);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size + 1, size + 1);

//        g.setColor(Color.PINK);
//        drawBounds(g);
    }

    public Point getTargetPosition() {
        return new Point(this.m_targetPositionX, this.m_targetPositionY);
    }

    public void setTargetColor(Color color) {
        this.targetColor = color;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.m_targetPositionX, this.m_targetPositionY, size + 2, size + 2);
    }

    private void drawBounds(Graphics2D g) {
        Rectangle bounds = getBounds();
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
