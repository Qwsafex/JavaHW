package utils;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.channels.SocketChannel;

public class ListResponse implements Response {
    @NotNull private byte[] data;

    public ListResponse(@NotNull byte[] data) {
        this.data = data;
    }

    @Override
    public WritableMessage generateMessage(SocketChannel channel) throws FileNotFoundException {
        return new SmallWritableMessage(channel, data);
    }
}
