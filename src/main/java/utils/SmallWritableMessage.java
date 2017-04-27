package utils;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.nio.channels.SocketChannel;

public class SmallWritableMessage extends WritableMessage {
    public SmallWritableMessage(@NotNull SocketChannel channel, @NotNull byte[] data) {
        super(channel, new ByteArrayInputStream(data));
    }
}
