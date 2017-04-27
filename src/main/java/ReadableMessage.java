import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

abstract class ReadableMessage {
    private static final int BUFFER_SIZE = 4096;
    private SocketChannel channel;
    private long size = 0;
    private int read = 0;
    private ByteBuffer buffer;

    ReadableMessage(@NotNull SocketChannel channel) {
        this.channel = channel;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public SocketChannel getChannel() {
        return channel;
    }

    boolean read() throws IOException {
        if (read < 4) {
            read += channel.read(buffer);
            if (read >= 4) {
                size = buffer.getLong();
            }
            else {
                return false;
            }
        }
        channel.read(buffer);
        process(buffer);
        return read == size;
    }

    protected abstract void process(ByteBuffer buffer);
}
