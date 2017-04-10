package vcs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VCSFiles {
    private static final Path VCS_DIR = Paths.get("vcs");
    static void delete(Path path) throws IOException {
        Files.delete(VCS_DIR.resolve(path));
    }

    public static void create(Path path) throws IOException {
        Files.createFile(path);
    }

    public static void write(Path path, String s) throws IOException {
        Files.write(VCS_DIR.resolve(path), s.getBytes());
    }

    public static boolean exists(Path path) {
        return Files.exists(VCS_DIR.resolve(path));
    }

    public static void writeObject(Path path, Serializable object) throws IOException {
        FileOutputStream outStream = new FileOutputStream(path.toFile());
        ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
        objOutStream.writeObject(object);
        objOutStream.close();
        outStream.close();
    }

    public static Object readObject(Path path) throws IOException, ClassNotFoundException {
        FileInputStream inStream = new FileInputStream(path.toFile());
        ObjectInputStream objInStream = new ObjectInputStream(inStream);
        Object object = objInStream.readObject();
        objInStream.close();
        inStream.close();
        return object;
    }
}
