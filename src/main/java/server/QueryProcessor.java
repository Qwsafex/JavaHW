package server;

import client.Client;
import client.Client.Query;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.jetbrains.annotations.NotNull;
import utils.SimpleFile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static client.Client.Query;

class QueryProcessor {
    byte[] process(@NotNull byte[] data) throws IOException {
        Query queryType = Query.values()[data[0]];
        String path = new String(data, 1, data.length - 1, StandardCharsets.UTF_8);

        switch (queryType) {
            case GET: {
                return ("Hello, " + path).getBytes();
            }
            case LIST: {
                ByteOutputStream byteStream = new ByteOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                ArrayList<SimpleFile> files = new ArrayList<>();
                files.add(new SimpleFile("heh", false));
                files.add(new SimpleFile("meh", true));
                objectStream.writeObject(files);
                objectStream.flush();
                return byteStream.getBytes();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
}
