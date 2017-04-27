package utils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SocketChannel;

public class SmallReadableMessage extends ReadableMessage {
    public SmallReadableMessage(@NotNull SocketChannel channel) {
        super(channel, new ByteArrayOutputStream());
    }

    @NotNull
    public byte[] getData() {
        return ((ByteArrayOutputStream) getDestination()).toByteArray();
    }

}
