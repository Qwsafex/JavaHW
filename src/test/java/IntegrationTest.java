
import client.Client;
import org.junit.Test;
import server.FileSystem;
import server.Server;

import java.nio.file.Paths;

public class IntegrationTest {
    @Test
    public void processGet() throws Exception {
        Server server = new Server(Paths.get("."));
        FileSystem fileSystem = new FileSystem(Paths.get("."));
        Client client = Client.getNonBlocking();
        //ArrayUtils.addAll(ByteUtils.byteToBytes((byte) NonBlockingClient.Query.GET.ordinal()),
    }

}