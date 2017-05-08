package utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of {@link ReadableMessage} suitable for reading big messages.
 */
public class BigReadableMessage extends ReadableMessage {
    @NotNull
    private String path;

    /**
     * Creates new message that reads its content from provided channel.
     * @param channel channel to read data from
     * @throws IOException If an I/O error occurs
     */
    public BigReadableMessage(@NotNull SocketChannel channel, @Nullable Path destination) throws IOException {
            super(channel, (destination != null ?
                    new KnownFileOutputStream(Files.createTempFile(destination, "dwnl", "").toFile()) :
                    new KnownFileOutputStream(Files.createTempFile("dwnl", "").toFile())));
        path = ((KnownFileOutputStream) getDestination()).getFile().getAbsolutePath();
    }

    /**
     * Returns path to downloaded file.
     * @return path to downloaded file
     */
    @NotNull
    public Path getPath() {
        return Paths.get(path);
    }
}
