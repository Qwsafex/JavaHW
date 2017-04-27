package utils;


import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SmallWritableMessage extends WritableMessage {
    public SmallWritableMessage(@NotNull SocketChannel channel, @NotNull byte[] data) {
        super(channel, new ByteArrayInputStream(addHeader(data)));
        System.out.println("SmallMessage of " + data.length + " bytes");
    }

    private static byte[] addHeader(byte[] data) {
        long size = Long.BYTES + data.length;
        return ArrayUtils.addAll(ByteBuffer.allocate(Long.BYTES).putLong(size).array(), data);
    }
}
