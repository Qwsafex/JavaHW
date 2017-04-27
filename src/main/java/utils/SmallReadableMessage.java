package utils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SocketChannel;

/**
 * Implementation of {@link ReadableMessage} suitable for reading small messages.
 */
public class SmallReadableMessage extends ReadableMessage {
    /**
     * Creates new message that reads its content from provided channel.
     * @param channel channel to read data from
     */
    public SmallReadableMessage(@NotNull SocketChannel channel) {
        super(channel, new ByteArrayOutputStream());
    }

    /**
     * Return read data.
     * @return data byte array
     */
    @NotNull
    public byte[] getData() {
        return ((ByteArrayOutputStream) getDestination()).toByteArray();
    }

}
