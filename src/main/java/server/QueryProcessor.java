package server;

import client.Client.Query;
import org.jetbrains.annotations.NotNull;
import utils.GetResponse;
import utils.ListResponse;
import utils.Response;
import utils.SimpleFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

class QueryProcessor {
    Response process(@NotNull byte[] data) throws IOException {
        Query queryType = Query.values()[data[0]];
        String path = new String(data, 1, data.length - 1, StandardCharsets.UTF_8);

        switch (queryType) {
            case GET: {
                return new GetResponse(Paths.get(path));
            }
            case LIST: {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                ArrayList<SimpleFile> files = new ArrayList<>(Files.list(Paths.get(path))
                        .map(p -> new SimpleFile(p.toString(), Files.isDirectory(p))).collect(Collectors.toList()));
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
