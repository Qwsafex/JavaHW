package utils;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WritableMessage {
    private static final int BUFFER_SIZE = 4096;
    private final ByteBuffer buffer;
    private int position;
    private SocketChannel channel;
    private InputStream source;
    public WritableMessage(@NotNull SocketChannel channel, @NotNull InputStream source) {
        this.channel = channel;
        this.source = source;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.clear();
    }

    /**
     * Calls write() on underlying channel with remaining data. 
     * @return whether it has completed message writing
     */
    public boolean write() throws IOException {
        if (buffer.hasRemaining()) {
            channel.write(buffer);
            return false;
        }
        else {
            buffer.clear();
        }
        if (source.available() == 0) {
            return true;
        }

        //noinspection ResultOfMethodCallIgnored
        source.read(buffer.array());
        buffer.flip();
        return false;
    }
}
