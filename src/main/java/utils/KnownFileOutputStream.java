package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class KnownFileOutputStream extends FileOutputStream {
    private final File file;

    public KnownFileOutputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }
    public File getFile() {
        return file;
    }
}
