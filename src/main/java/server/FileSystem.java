package server;

import org.jetbrains.annotations.NotNull;
import utils.SimpleFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class that provides means to interact with file system.
 */
public class FileSystem {
    @NotNull
    private final Path root;

    /**
     * Creates file system manager with specified root.
     * @param root path to desired root directory
     */
    @SuppressWarnings("WeakerAccess")
    public FileSystem(@NotNull Path root) {
        this.root = root;
    }

    /**
     * Returns list of entries in specified directory.
     * @param path path to directory
     * @return list of entries in specified directory
     * @throws IOException If an I/O error occurs
     */
    @SuppressWarnings("WeakerAccess")
    @NotNull
    public ArrayList<SimpleFile> list(@NotNull Path path) throws IOException {
        assertChild(path, root);
        return new ArrayList<>(Files.list(path).map(p -> new SimpleFile(p.toString(), Files.isDirectory(p))).collect(Collectors.toList()));
    }

    /**
     * Returns {@link InputStream} to read from the file.
     * @param path path to the file
     * @return a new input stream
     * @throws IOException If an I/O error occurs
     */
    @SuppressWarnings("WeakerAccess")
    @NotNull
    public InputStream getOutputStream(@NotNull Path path) throws IOException {
        assertChild(path, root);
        return Files.newInputStream(path);
    }

    private static void assertChild(@NotNull Path path, @NotNull Path root) {
        if (!isChild(path, root)) {
            throw new SecurityException("Trying to go out of set root");
        }
    }


    private static boolean isChild(@NotNull Path path, @NotNull Path root) {
        return path.toAbsolutePath().startsWith(root.toAbsolutePath());
    }

}