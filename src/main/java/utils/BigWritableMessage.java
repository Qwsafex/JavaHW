package utils;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.SocketChannel;

class BigWritableMessage extends WritableMessage {
    BigWritableMessage(@NotNull SocketChannel channel, @NotNull InputStream source) throws IOException {
        super(channel, createMessageInputStream(source), getSize(source));
    }

    @NotNull
    private static InputStream createMessageInputStream(@NotNull InputStream source) throws IOException {
        InputStream stream = new ByteArrayInputStream(ByteUtils.longToBytes(getSize(source)));
        return new SequenceInputStream(stream, source);
    }

    private static long getSize(@NotNull InputStream source) throws IOException {
        return Long.BYTES + source.available();
    }
}
