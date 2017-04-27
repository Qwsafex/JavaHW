package server;

import client.Client.Query;
import org.jetbrains.annotations.NotNull;
import utils.SimpleFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class QueryProcessor {
    byte[] process(@NotNull byte[] data) throws IOException {
        Query queryType = Query.values()[data[0]];
        String path = new String(data, 1, data.length - 1, StandardCharsets.UTF_8);

        switch (queryType) {
            case GET: {
                return ("Hello, " + path).getBytes();
            }
            case LIST: {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                ArrayList<SimpleFile> files = new ArrayList<>();
                files.add(new SimpleFile("heh", false));
                files.add(new SimpleFile("meh", true));
                objectStream.writeObject(files);
                objectStream.flush();
                return byteStream.toByteArray();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
}
