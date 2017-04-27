package utils;

import java.io.Serializable;

public class SimpleFile implements Serializable{
    public final String name;
    public final boolean isDirectory;

    public SimpleFile(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

}
