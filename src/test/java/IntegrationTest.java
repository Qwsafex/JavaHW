
import client.Client;
import org.junit.Test;
import server.FTPServer;
import server.FileSystem;

import java.nio.file.Paths;

public class IntegrationTest {
    @Test
    public void processGet() throws Exception {
        FTPServer server = FTPServer.getNonBlocking(Paths.get("."));
        FileSystem fileSystem = new FileSystem(Paths.get("."));
        Client client = Client.getNonBlocking();
        //ArrayUtils.addAll(ByteUtils.byteToBytes((byte) NonBlockingClient.Query.GET.ordinal()),
    }

}