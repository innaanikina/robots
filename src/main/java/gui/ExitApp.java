package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitApp implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
