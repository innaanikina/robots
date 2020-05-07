package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.Logger;
import gui.dialogues.ExitDialogue;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private HashMap<String, JInternalFrame> allFrames = new HashMap<>();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        setVisible(true);

        LogWindow logWindow = createLogWindow();
        allFrames.put(logWindow.getTitle(), logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        allFrames.put(gameWindow.getTitle(), gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        restoreWindows();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveWindows();
                ExitDialogue.closeWindowDialogue(MainApplicationFrame.this);
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ExitDialogue.closeJIF(frame);
            }
    });
    }

    private JMenuItem createMenuItem(String txt, ActionListener e, int eventNum){
        JMenuItem item = new JMenuItem(txt, eventNum);
        item.addActionListener(e);
        return item;
    }

    private JMenu addReturnTab(String name, int key, String description) {
        JMenu tab = new JMenu(name);
        tab.setMnemonic(key);
        tab.getAccessibleContext().setAccessibleDescription(description);
        return tab;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = addReturnTab("Режим отображения",
                KeyEvent.VK_V, "Управление режимом отображения");

        lookAndFeelMenu.add(createMenuItem("Cистемная схема", (event) ->
        {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }, KeyEvent.VK_S));

        lookAndFeelMenu.add(createMenuItem("Универсальная схема", (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }, KeyEvent.VK_S));

        JMenu testMenu = addReturnTab("Тесты", KeyEvent.VK_T,
                "Тестовые команды.");
        testMenu.add(createMenuItem("Сообщение в лог", (event) -> {
            Logger.debug("Новая строка");
        }, KeyEvent.VK_S));

        JMenu mainMenu = addReturnTab("Программа", KeyEvent.VK_P, "Программное меню");
        mainMenu.add(createMenuItem("Выход", (event) -> {
            saveWindows();
            ExitDialogue.closeWindowDialogue(MainApplicationFrame.this);
        }, KeyEvent.VK_ESCAPE));


        menuBar.add(mainMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }
    
    public void saveWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        Saver[] saves = new Saver[frames.length];

        for (int i = 0; i < frames.length; i++) {
            String title = frames[i].getTitle();
            Saver frm = new Saver(title, frames[i].getLocation(),
                    frames[i].isIcon(), frames[i].getSize());
            saves[i] = frm;
        }

        Saver.saveAll(saves);
    }

    private void restoreWindows() {
        HashMap<String, Saver> restored = Saver.restore();
        for (String frmName : allFrames.keySet()) {
            Saver restoredFrame = restored.get(frmName);
            JInternalFrame frm = allFrames.get(frmName);
            addWindow(frm);
            try {
                frm.setLocation(restoredFrame.getLocation());
                frm.setIcon(restoredFrame.getIsIcon());
                frm.setSize(restoredFrame.getSize());
            } catch (PropertyVetoException e) {
                System.out.println(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println(frmName + " is restored with default settings");
            }
        }
    }
    
}
