import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class WritableMessage {
    private final ByteBuffer buffer;
    private int position;
    private SocketChannel socket;
    private byte[] data;
    /**
     *  @param socket socket to write data to
     * @param data data to write to socket
     */
    WritableMessage(@NotNull SocketChannel socket, @NotNull byte[] data) {
        this.socket = socket;
        this.buffer = ByteBuffer.allocate(data.length);
        this.buffer.clear();
    }

    /**
     * Calls write() on underlying channel with remaining data. 
     * @return whether it has completed message writing
     */
    boolean write() {

        throw new UnsupportedOperationException();
    }
}
