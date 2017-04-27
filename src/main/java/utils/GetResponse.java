package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class GetResponse implements Response {
    @NotNull private Path path;

    public GetResponse(@NotNull Path path) {
        this.path = path;
    }

    @Override
    public WritableMessage generateMessage(@NotNull SocketChannel channel) throws IOException {
        return new BigWritableMessage(channel, path);
    }
}
