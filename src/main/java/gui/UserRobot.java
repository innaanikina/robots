package gui;

import java.awt.*;

public class UserRobot extends Robot {

    public UserRobot(int x, int y) {
        super(x, y);
        this.robotColor = Color.ORANGE;
        this.velocity = 0.1;
    }
}
