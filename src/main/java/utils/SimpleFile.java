package utils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Object containing pair (name, isDirectory) for file.
 */
public class SimpleFile implements Serializable{
    /**
     * Filename
     */
    @NotNull
    public final String name;
    /**
     * Whether this file is a directory
     */
    public final boolean isDirectory;

    /**
     * Creates new file info with given data.
     * @param name filename
     * @param isDirectory whether given file is a directory
     */
    public SimpleFile(@NotNull String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

}
