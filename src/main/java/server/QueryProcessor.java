package server;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

class QueryProcessor {
    byte[] process(@NotNull byte[] data) throws IOException {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));
        int queryType = inputStream.readInt();
        String path = inputStream.readUTF();

        switch (queryType) {
            case 0: {
                return ("Hello, " + path).getBytes();
            }
            case 1: {
                return ("Goodbye, " + path).getBytes();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
}
