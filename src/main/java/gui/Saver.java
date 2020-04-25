package gui;

import java.awt.*;
import java.io.*;

public class Saver implements Serializable{
    private String name;
    private int x;
    private int y;
    private static final long serialVersionUID = 1113799434508676095L;

    public Saver() {}

    public Saver(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getTitle() {
        return name;
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("Name='%s', x=%d, y=%d", name, x, y);
    }

    void save(String name) {
        String filePath = getCurrentPath(name);
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(this);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void restore(String name) {
        String filePath = getCurrentPath(name);
        try {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        Saver obj = (Saver) in.readObject();
        this.name = obj.name;
        this.x = obj.x;
        this.y = obj.y;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentPath(String name) {
        String filePath = new File("").getAbsolutePath();
        return filePath.substring(0, filePath.length() - 6) + "src/main/resources/" + name;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Saver save = new Saver("Window", 0, 0);
        Saver save2 = new Saver("SmallWin", 150, 150);
        System.out.println("Before: \n" + save + "\n" + save2 + "\n\n");

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("windows.out"));
        out.writeObject(save);
        out.writeObject(save2);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("window.out"));
        Saver newSave = (Saver) in.readObject();
        Saver newSave2 = (Saver) in.readObject();

        System.out.println("After: \n" + newSave + "\n" + newSave2);


    }

}
