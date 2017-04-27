package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;

public class GetResponse implements Response {
    private final InputStream source;

    public GetResponse(@NotNull InputStream source) {
        this.source = source;
    }

    @Override
    public WritableMessage generateMessage(@NotNull SocketChannel channel) throws IOException {
        return new BigWritableMessage(channel, source);
    }
}
