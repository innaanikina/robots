package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Robot extends GameObject {
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 150;

    private static final double maxAngularVelocity = 0.002;

    protected Color robotColor = Color.MAGENTA;
    protected double velocity = 0.05;

    private int score = 0;

    public Robot() {
        this.m_robotPositionX = 0;
        this.m_robotPositionY = 0;
        this.m_robotDirection = 0;
    }

    public Robot(int x, int y) {
        this.m_robotPositionX = x;
        this.m_robotPositionY = y;
        this.m_robotDirection = 0;
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected Point getRobotPosition() {
        return new Point(round(this.m_robotPositionX), round(this.m_robotPositionY));
    }

    protected double getRobotDirection() {
        return this.m_robotDirection;
    }

    protected Point getTargetPosition() {
        return new Point(this.m_targetPositionX, this.m_targetPositionY);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration, Dimension win) {
        int height = win.height;
        int width = win.width;

        velocity = applyLimits(velocity, 0, this.velocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);

        double newX = m_robotPositionX + velocity / angularVelocity * (Math.sin(m_robotDirection + angularVelocity * duration) - Math.sin(m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * (Math.cos(m_robotDirection + angularVelocity * duration) - Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        newX = applyLimits(newX, 15, width - 15);
        newY = applyLimits(newY, 15, height - 15);
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = newDirection;
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    protected void onModelUpdateEvent(Dimension window) {
        double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX, m_robotPositionY);
        if (distance < 1) {
            return;
        }
        double velocity = this.velocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10, window);
    }

    protected void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = round(x);
        int robotCenterY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(robotColor);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);

//        g.setColor(Color.PINK);
//        drawBounds(g);
    }

    protected void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    protected void autoMove(int width, int height) {
        double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX, m_robotPositionY);
        if (distance < 1) {
            Point newCoordinates = calculateTargetCoordinates(width, height);
            System.out.println("Calculated coordinates: " + newCoordinates.x + " " + newCoordinates.y);
            m_targetPositionX = newCoordinates.x;
            m_targetPositionY = newCoordinates.y;
        }
    }

    public boolean intersects(Target trg) {
        Rectangle targetBounds = trg.getBounds();

        Rectangle result = SwingUtilities.computeIntersection(round(this.m_robotPositionX) - 15, round(this.m_robotPositionY) - 15, 30, 30, targetBounds);

        return (result.getWidth() > 0 && result.getHeight() > 0);
    }

    public Rectangle getBounds() {
        return new Rectangle(round(this.m_robotPositionX) - 15, round(this.m_robotPositionY) - 7, 30, 14);
    }

    private void drawBounds(Graphics2D g) {
        Rectangle bounds = getBounds();
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void addPoints(int points) {
        this.score += points;
    }

    public void printScore() {
        String text = String.format("Name: %s, score: %d", this.name, this.score);
        System.out.println(text);
    }
}
