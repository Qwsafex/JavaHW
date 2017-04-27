package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Response {
    WritableMessage generateMessage(SocketChannel channel) throws IOException;
}
