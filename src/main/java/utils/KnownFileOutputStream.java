package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

class KnownFileOutputStream extends FileOutputStream {
    private final File file;

    KnownFileOutputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }
    File getFile() {
        return file;
    }
}
