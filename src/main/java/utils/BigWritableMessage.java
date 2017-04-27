package utils;


import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class BigWritableMessage extends WritableMessage {
    public BigWritableMessage(@NotNull SocketChannel channel, @NotNull Path source) throws FileNotFoundException {
        super(channel, new FileInputStream(source.toFile()));
    }
}
