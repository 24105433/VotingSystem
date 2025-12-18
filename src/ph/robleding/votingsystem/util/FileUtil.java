package ph.robleding.votingsystem.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // Save a list of serializable objects to file
    public static <T> void saveToFile(String filename, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("❌ Failed to save list to " + filename + ": " + e.getMessage());
        }
    }

    // Load a list of serializable objects from file
    public static <T> List<T> loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // First time file might not exist, return empty list
            return new ArrayList<>();
        }
    }

    // Save a single serializable object (like ElectionState)
    public static <T> void saveSingle(String filename, T obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.out.println("❌ Failed to save object to " + filename + ": " + e.getMessage());
        }
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.delete();
    }

    // Load a single object from file
    public static <T> T loadSingle(String filename, Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
