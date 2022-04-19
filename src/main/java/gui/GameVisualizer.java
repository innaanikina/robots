package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class  GameVisualizer extends JPanel {

    private Robot[] robots;
    private CopyOnWriteArrayList<Target> targets;
    private final Timer m_timer;

    private final int m_gameWidth;
    private final int m_gameHeight;

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public GameVisualizer(int width, int height) {
        m_gameWidth = width;
        m_gameHeight = height;

        initRobots();
        initTargets();

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
                robots[2].autoMove(window.width, window.height);
            }
        }, 100, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (targets.isEmpty()) {
                    gameOver();
                }

                checkCollision();
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

    private void gameOver() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("GAME OVER");
                for (Robot robot : robots) {
                    robot.printScore();
                }

                m_timer.cancel();
                m_timer.purge();
            }
        }, 1000);
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

        for (Target target : targets) {
            Point targetPosition = target.getTargetPosition();
            target.drawTarget(g2d, targetPosition.x, targetPosition.y);
        }
    }

    private void initRobots() {
        int robotsCount = 2;
        robots = new Robot[robotsCount + 1];

        int width = 400 / (robotsCount + 2);
        UserRobot userRobot = new UserRobot(width, 15);
        robots[0] = userRobot;
        this.add(userRobot);

        for (int i = 1; i <= robotsCount; i++) {
            Robot robot = new Robot((i + 1) * width, 15);
            robots[i] = robot;
            this.add(robots[i]);
        }

        robots[0].setTargetPosition(new Point(150, 150));
        robots[1].setTargetPosition(new Point(200, 100));

        robots[0].name = "1: USER robot";
        robots[1].name = "2: auto robot";
        robots[2].name = "3: auto robot";
    }

    private void initTargets() {
        targets = new CopyOnWriteArrayList<>();

        int targetsCount = 10;
        for (int i = 0; i < targetsCount; i++) {
            Target target = new Target(m_gameWidth, m_gameHeight);
            target.name = "target " + i;
            targets.add(target);
            this.add(target);
        }
    }

    public void checkCollision() {
        for (Robot robot : robots) {
            for (Target target : targets) {
                if (robot.intersects(target)) {
                    System.out.println("robot " + robot.name + " intersected target " + target.name);
                    target.setTargetColor(Color.RED);

                    robot.addPoints(10);
                    targets.remove(target);
                    this.remove(target);
                    robot.printScore();
                }
            }
        }
    }
}
