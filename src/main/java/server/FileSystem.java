package server;

import utils.SimpleFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class FileSystem {
    private final Path root;

    @SuppressWarnings("WeakerAccess")
    public FileSystem(Path root) {
        this.root = root;
    }

    @SuppressWarnings("WeakerAccess")
    public ArrayList<SimpleFile> list(Path path) throws IOException {
        assertChild(path, root);
        return new ArrayList<>(Files.list(path).map(p -> new SimpleFile(p.toString(), Files.isDirectory(p))).collect(Collectors.toList()));
    }

    private static void assertChild(Path path, Path root) {
        if (!isChild(path, root)) {
            throw new SecurityException("Trying to go out of set root");
        }
    }


    private static boolean isChild(Path path, Path root) {
        return path.toAbsolutePath().startsWith(root.toAbsolutePath());
    }

    @SuppressWarnings("WeakerAccess")
    public InputStream getOutputStream(Path path) throws IOException {
        assertChild(path, root);
        return Files.newInputStream(path);
    }
}