package gui;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class Saver implements Serializable{
    private String name;
    private Point location;
    private boolean isIcon;
    private static final long serialVersionUID = 1113799434508676095L;
    private Dimension size;

    public Saver() {}

    public Saver(String name, Point p, boolean isIcon, Dimension size) {
        this.name = name;
        this.location = p;
        this.isIcon = isIcon;
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("Name='%s', x=%d, y=%d", name, location.x, location.y);
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public boolean getIsIcon() {
        return isIcon;
    }

    public Dimension getSize() {
        return size;
    }

    private static String getCurrentPath() {
        String filePath = new File("").getAbsolutePath();
        return filePath.substring(0, filePath.length() - 6) + "robots/src/main/resources/windows.bin";
    }


    public static HashMap<String, Saver> restore() {
        HashMap<String, Saver> restored = new HashMap<>();
        String filePath = getCurrentPath();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                Saver obj = (Saver) in.readObject();
                restored.put(obj.getName(), obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return restored;
    }

    public static void saveAll(Saver[] frames) {
        String filePath = getCurrentPath();
        int count = frames.length;
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeInt(count);
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
