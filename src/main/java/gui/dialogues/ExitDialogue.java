package gui.dialogues;

import javax.swing.*;

public class ExitDialogue extends JOptionPane {
    public static void closeWindowDialogue(JFrame obj) {
        int result = showConfirmDialog(obj,
                "Вы действительно хотите выйти?",
                "Выход", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void closeJIF(JInternalFrame obj) {
        int result = showConfirmDialog(obj, "Закрыть окно?",
                "Закрыть", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION)
            obj.dispose();
    }
}
