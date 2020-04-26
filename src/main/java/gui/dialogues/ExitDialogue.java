package gui.dialogues;

import javax.swing.*;
import java.awt.Component;

public class ExitDialogue extends JOptionPane {

    public static void closeWindowDialogue(JFrame obj) {
        int res = getAnswer(obj);
        if (res == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void closeJIF(JInternalFrame obj) {
        int result = getAnswer(obj);
        if (result == JOptionPane.YES_OPTION)
            obj.dispose();
    }

    private static int getAnswer(Component obj) {
        return JOptionPane.showConfirmDialog(obj, "Закрыть окно?",
                "Закрыть", JOptionPane.YES_NO_OPTION);
    }

}
