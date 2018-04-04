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
    }

    private static long getSize(@NotNull byte[] data) {
        return Long.BYTES + data.length;
    }

    @NotNull
    private static byte[] addHeader(@NotNull byte[] data) {
        return ArrayUtils.addAll(ByteUtils.longToBytes(getSize(data)), data);
    }
}
