import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BigMessage extends ReadableMessage {
    private String filename;

    BigMessage(@NotNull SocketChannel channel) {
        super(channel);

    }


    @Override
    protected void process(ByteBuffer buffer) {
        throw new UnsupportedOperationException();
    }


    public String getFilename() {
        return filename;
    }
}
