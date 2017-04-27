package utils;


import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.nio.channels.SocketChannel;

/**
 * Implementation of {@link WritableMessage} suitable for writing small messages.
 */
public class SmallWritableMessage extends WritableMessage {
    /**
     * Creates new message that writes its content to provided channel.
     * @param channel channel to write data to
     */
    public SmallWritableMessage(@NotNull SocketChannel channel, @NotNull byte[] data) {
        super(channel, new ByteArrayInputStream(addHeader(data)), getSize(data));
        System.out.println("SmallMessage of " + data.length + " bytes");
    }

    private static long getSize(byte[] data) {
        return Long.BYTES + data.length;
    }

    private static byte[] addHeader(byte[] data) {
        return ArrayUtils.addAll(ByteUtils.longToBytes(getSize(data)), data);
    }
}
