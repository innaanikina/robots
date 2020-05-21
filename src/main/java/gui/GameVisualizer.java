package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class  GameVisualizer extends JPanel {

    private Robot[] robots;
    private Robot userRobot;
    private Timer m_timer;

    private int count;

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer() {
        initRobots(3);

        m_timer = initTimer();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension window = getSize();
                robots[0].onModelUpdateEvent(window);
            }
        }, 100, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension window = getSize();
                robots[1].onModelUpdateEvent(window);
                robots[1].autoMove(window.width, window.height);
            }
        }, 100, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension window = getSize();
                robots[2].onModelUpdateEvent(window);
            }
        }, 100, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robots[0].setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Robot robot : robots) {
            Point robotPosition = robot.getRobotPosition();
            if (count < 3) {
                System.out.println(robotPosition + robot.name);
                count++;
            }
            robot.drawRobot(g2d, robotPosition.x, robotPosition.y, robot.getRobotDirection());
            Point targetPosition = robot.getTargetPosition();
            robot.drawTarget(g2d, targetPosition.x, targetPosition.y);
        }
    }

    private void initRobots(int count) {
        robots = new Robot[count];

        int width = 400 / (count + 1); //как посчитать имеющуюся ширину??
        for (int i = 0; i < count; i++) {
            Robot robot = new Robot((i + 1) * width, 15);
            robots[i] = robot;
        }

        robots[1].setTargetPosition(new Point(150, 150));
        robots[2].setTargetPosition(new Point(200, 100));

        robots[0].name = "1";
        robots[1].name = "2";
        robots[2].name = "3";
    }
}
