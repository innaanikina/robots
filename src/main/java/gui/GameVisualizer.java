package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class  GameVisualizer extends JPanel {

    private Robot[] robots;
    private Robot robot;
    private Timer m_timer;

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
                Dimension window = GameVisualizer.this.getSize();
                robots[0].onModelUpdateEvent(window);
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension window = GameVisualizer.this.getSize();
                robots[1].onModelUpdateEvent(window);
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension window = GameVisualizer.this.getSize();
                robots[2].onModelUpdateEvent(window);
            }
        }, 0, 10);
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
            robot.drawRobot(g2d, robotPosition.x, robotPosition.y, robot.getRobotDirection());
            Point targetPosition = robot.getTargetPosition();
            robot.drawTarget(g2d, targetPosition.x, targetPosition.y);
        }
    }

    private void initRobots(int count) {
        robots = new Robot[count];
        int width = 400 / (count + 1);
        System.out.println(width);
        Robot robot1 = new Robot(width, 0);
        Robot robot2 = new Robot(width * 2, 0);
        Robot robot3 = new Robot(width * 3, 0);
        robot2.setTargetPosition(new Point(100, 200));
        robot3.setTargetPosition(new Point(200, 100));


        robots[0] = robot1;
        robots[1] = robot2;
        robots[2] = robot3;
    }
}
