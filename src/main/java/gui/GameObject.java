package gui;

import java.awt.*;
import java.util.Random;

public class GameObject extends Component {
    public String name;

    protected static int applyLimits(int value, int min, int max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    protected Point calculateTargetCoordinates(int width, int height) {
        Random rand = new Random();
        int x = applyLimits(rand.nextInt(width), 25, width - 25);
        int y = applyLimits(rand.nextInt(height), 25, width - 25);
        return new Point(x, y);
    }
}
