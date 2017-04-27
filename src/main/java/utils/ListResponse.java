package utils;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.channels.SocketChannel;

/**
 * Class encapsulating response to Query.LIST query.
 */
public class ListResponse implements Response {
    @NotNull private byte[] data;

    /**
     * Creates new response encapsulating given data.
     * @param data response data
     */
    public ListResponse(@NotNull byte[] data) {
        this.data = data;
    }

    @Override
    public WritableMessage generateMessage(SocketChannel channel) {
        return new SmallWritableMessage(channel, data);
    }
}
