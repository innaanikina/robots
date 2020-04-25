package gui;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class Saver implements Serializable{
    private String name;
    private int x;
    private int y;
    private boolean isIcon;
    private static final long serialVersionUID = 1113799434508676095L;

    public Saver() {}

    //TODO изменить на Point
    public Saver(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Name='%s', x=%d, y=%d", name, x, y);
    }

    private static String getCurrentPath() {
        String filePath = new File("").getAbsolutePath();
        return filePath.substring(0, filePath.length() - 6) + "src/main/resources/windows.bin";
    }

    public static HashMap<String, Point> restoreAll(int count) {
        HashMap<String, Point> restored = new HashMap<>();

        String filePath = getCurrentPath();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            for (int i = 0; i < count; i++) {
                Saver obj = (Saver) in.readObject();
                restored.put(obj.name, new Point(obj.x, obj.y));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return restored;
    }

    //TODO попробовать сериализовать полностью JInternalFrames

    public static void saveAll(Saver[] frames) {
        String filePath = getCurrentPath();
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            for (Saver frame : frames) {
                out.writeObject(frame);
            }
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
