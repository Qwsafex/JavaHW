package vcs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class VCSFiles {
    private static final Path VCS_DIR = Paths.get(".vcs");
    static void delete(Path path) throws IOException {
        Files.delete(VCS_DIR.resolve(path));
    }


    static void init() throws IOException {
        if (!Files.exists(VCS_DIR) || !Files.isDirectory(VCS_DIR)) {
            Files.createDirectories(VCS_DIR);
        }
    }
    static void create(Path path) throws IOException {
        path = VCS_DIR.resolve(path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.createFile(path);
    }

    public static void write(Path path, String s) throws IOException {
        write(path, s.getBytes());
    }
    public static void write(Path path, byte[] bytes) throws IOException {
        path = VCS_DIR.resolve(path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, bytes);
    }

    static boolean exists(Path path) {
        path = VCS_DIR.resolve(path);
        return Files.exists(path);
    }

    static void writeObject(Path path, Serializable object) throws IOException {
        path = VCS_DIR.resolve(path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        FileOutputStream outStream = new FileOutputStream(path.toFile());
        ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
        objOutStream.writeObject(object);
        objOutStream.close();
        outStream.close();
    }

    static Object readObject(Path path) throws IOException, ClassNotFoundException {
        path = VCS_DIR.resolve(path);
        FileInputStream inStream = new FileInputStream(path.toFile());
        ObjectInputStream objInStream = new ObjectInputStream(inStream);
        Object object = objInStream.readObject();
        objInStream.close();
        inStream.close();
        return object;
    }

}
