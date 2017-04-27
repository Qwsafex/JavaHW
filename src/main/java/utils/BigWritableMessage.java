package utils;


import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class BigWritableMessage extends WritableMessage {
    public BigWritableMessage(@NotNull SocketChannel channel, @NotNull Path source) throws IOException {
        super(channel, createMessageInputStream(source), getSize(source));
    }

    private static InputStream createMessageInputStream(Path source) throws IOException {
        InputStream stream1 = new ByteArrayInputStream(ByteUtils.longToBytes(getSize(source)));
        InputStream stream2 = new FileInputStream(source.toFile());
        return new SequenceInputStream(stream1, stream2);
    }

    private static long getSize(Path source) throws IOException {
        return Long.BYTES + Files.size(source);
    }
}
