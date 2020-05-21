package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Robot {
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 150;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.002;

    private int count;

    public String name;

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
        if (count < 3) {
            System.out.println("height is " + height + ", width is " + width);
            System.out.println("name is " + name);
            count++;
        }

        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);

        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
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
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private static int applyLimits(int value, int min, int max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    protected void onModelUpdateEvent(Dimension window) {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
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
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
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
        Point newTargetCoordinates = calculateTargetCoordinates(width, height);
        System.out.println("I AM HERE");
        System.out.println("Target position: " + m_targetPositionX + " " + m_targetPositionY);
        System.out.println("Robot position: " + m_robotPositionX + " " + m_robotPositionY);
        if (m_targetPositionX == 150 && m_targetPositionY == 150) {
            System.out.println("New target calcuations 1");
            m_targetPositionX = newTargetCoordinates.x;
            m_targetPositionY = newTargetCoordinates.y;
        }
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        System.out.println("Distance is " + distance);
        if (distance < 0.5) {
            System.out.println("New target calcuations 2");
            Random rand = new Random();
            int x = applyLimits(rand.nextInt(width - 25), 0, width - 25);
            int y = applyLimits(rand.nextInt(height - 25), 0, width - 25);

            int counter = 0;
            while (x == m_targetPositionX || y == m_targetPositionY) {
                System.out.println("couter is " + counter);
                counter++;
                x = applyLimits(rand.nextInt(width - 15), 25, width - 25);
                y = applyLimits(rand.nextInt(height - 15), 25, width - 25);
            }

            Point coords = calculateTargetCoordinates(width, height);
            System.out.println("Calculated coordinates: " + x + " " + y);
            m_targetPositionX = x;
            m_targetPositionY = y;
        }
    }

    private Point calculateTargetCoordinates(int width, int height) {
        Random rand = new Random();
        int x = applyLimits(rand.nextInt(width), 25, width - 25);
        int y = applyLimits(rand.nextInt(height), 25, width - 25);
        return new Point(x, y);
    }
}
