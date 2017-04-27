import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class SmallMessage extends ReadableMessage {
    private SocketChannel channel;
    ArrayList<Byte> data;

    SmallMessage(@NotNull SocketChannel channel) {
        super(channel);
    }


    public byte[] getData() {
        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    @Override
    protected void process(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            data.add(buffer.get());
        }
    }
}
