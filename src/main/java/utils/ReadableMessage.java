package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ReadableMessage {
    private static final int BUFFER_SIZE = 4096;
    private final OutputStream destination;
    private SocketChannel channel;
    private long size = 0;
    private int read = 0;
    private ByteBuffer buffer;

    ReadableMessage(@NotNull SocketChannel channel, OutputStream destination) {
        this.channel = channel;
        this.destination = destination;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public SocketChannel getChannel() {
        return channel;
    }

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

    protected OutputStream getDestination() {
        return destination;
    }
}
