package gui;

import java.awt.*;
import javax.swing.*;

public class GameWindow extends JInternalFrame {

    public GameWindow(int width, int height) {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer(width, height);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(width, height);
    }
}
