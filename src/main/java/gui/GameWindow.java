package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();


        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                int result = JOptionPane.showConfirmDialog(GameWindow.this,
                        "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    GameWindow.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    e.getInternalFrame().dispose();
                }
                else if (result == JOptionPane.NO_OPTION) {
                    GameWindow.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
}
