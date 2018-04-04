package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Response to client query based on which message could be generated.
 */
public interface Response {
    /**
     * Returns {@link WritableMessage} based on this response
     * @param channel channel to associate message with
     * @return message with this response
     */
    WritableMessage generateMessage(@NotNull SocketChannel channel) throws IOException;
}
