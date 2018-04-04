package utils;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Object that can write message data to specified channel.
 */
public class WritableMessage {
    private static final int BUFFER_SIZE = 4096;
    @NotNull
    private final ByteBuffer buffer;
    private long leftSource;
    @NotNull
    private SocketChannel channel;
    @NotNull
    private InputStream source;
    WritableMessage(@NotNull SocketChannel channel, @NotNull InputStream source, long size) {
        this.leftSource = size;
        this.channel = channel;
        // TODO: add buffered
        this.source = source;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.limit(0);
    }

    /**
     * Calls write() on associated channel with remaining data.
     * @return whether it has completed message writing
     */
    public boolean write() throws IOException {
        if (buffer.hasRemaining()) {
            channel.write(buffer);
            return false;
        }
        else if (leftSource > 0){
            buffer.clear();
            int read = source.read(buffer.array());
            buffer.limit(read);
            leftSource -= read;
        }
        return leftSource == 0 && !buffer.hasRemaining();
    }
}
