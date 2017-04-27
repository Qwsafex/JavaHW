package server;

import client.Client;
import org.jetbrains.annotations.NotNull;
import utils.GetResponse;
import utils.ListResponse;
import utils.Response;
import utils.SimpleFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;

class QueryProcessor {
    Response process(@NotNull byte[] data, @NotNull FileSystem fileSystem) throws IOException {
        Client.Query queryType = Client.Query.values()[data[0]];
        String path = new String(data, 1, data.length - 1, StandardCharsets.UTF_8);

        switch (queryType) {
            case GET: {
                return new GetResponse(fileSystem.getOutputStream(Paths.get(path)));
            }
            case LIST: {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                ArrayList<SimpleFile> files = fileSystem.list(Paths.get(path));
                objectStream.writeObject(files);
                objectStream.flush();
                return new ListResponse(byteStream.toByteArray());
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
}
