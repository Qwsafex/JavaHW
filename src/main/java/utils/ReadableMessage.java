package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Object that can read message data from specified channel.
 */
public class ReadableMessage {
    private static final int BUFFER_SIZE = 4096;
    @NotNull
    private final OutputStream destination;
    @NotNull
    private SocketChannel channel;
    private long size = 0;
    private int read = 0;
    @NotNull
    private ByteBuffer buffer;

    ReadableMessage(@NotNull SocketChannel channel, @NotNull OutputStream destination) {
        this.channel = channel;
        // TODO: add buffered
        this.destination = destination;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    /**
     * Returns associated channel.
     * @return channel from which data is being read
     */
    @NotNull
    public SocketChannel getChannel() {
        return channel;
    }

    /**
     * Calls read() on associated channel
     * @return whether there is data left to read
     * @throws IOException If an I/O error occurs.
     */
    public boolean read() throws IOException {
        if (read < 4) {
            read += channel.read(buffer);
            if (read >= 4) {
                buffer.flip();
                size = buffer.getLong();
            }
            else {
                return false;
            }
        }
        while (buffer.hasRemaining()) {
            destination.write(buffer.get());
        }
        buffer.clear();
        if (read < size) {
            read += channel.read(buffer);
        }
        buffer.flip();
        return read == size && !buffer.hasRemaining();
    }

    @NotNull
    OutputStream getDestination() {
        return destination;
    }
}
