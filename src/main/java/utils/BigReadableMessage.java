package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;

public class BigReadableMessage extends ReadableMessage {
    @NotNull
    private String filename;

    public BigReadableMessage(@NotNull SocketChannel channel) throws IOException {
        super(channel, new KnownFileOutputStream(Files.createTempFile("dwnl", "").toFile()));
        filename = ((KnownFileOutputStream) getDestination()).getFile().getAbsolutePath();
    }


    @NotNull
    public String getFilename() {
        return filename;
    }
}
