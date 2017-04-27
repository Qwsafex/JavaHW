
import client.Client;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import server.FileSystem;
import server.Server;
import utils.ByteUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class IntegrationTest {
    @Test
    public void processGet() throws Exception {
        Server server = new Server(Paths.get("."));
        FileSystem fileSystem = new FileSystem(Paths.get("."));
        //ArrayUtils.addAll(ByteUtils.byteToBytes((byte) Client.Query.GET.ordinal()),
    }

}