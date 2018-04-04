package utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

class KnownFileOutputStream extends FileOutputStream {
    @NotNull
    private final File file;

    KnownFileOutputStream(@NotNull File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }
    @NotNull
    File getFile() {
        return file;
    }
}
