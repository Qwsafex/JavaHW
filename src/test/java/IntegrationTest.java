
import client.Client;
import org.junit.Assert;
import org.junit.Test;
import server.FTPServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IntegrationTest {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final TestFile A = new TestFile("A.txt", "A text");
    private static final TestFile B = new TestFile("B.txt", "B text");
    private static final TestFile A_B = new TestFile("A/B.txt", "A/B.txt text");
    private static final TestFile A_B_C = new TestFile("A/B/C.txt", "A/B/C.txt text");

    private Client client;
    private Thread serverThread;

    @Test
    public void processGet() throws Exception {
        prepare();

        A.create();
        String path = A.getPath().toString();

        Path downloadedPath = client.executeGet(A.getPath().toString());

        Assert.assertArrayEquals(Files.readAllBytes(downloadedPath), A.getContent());
        Files.delete(downloadedPath);

        finish();
    }



    private void finish() throws IOException {
        client.disconnect();
        serverThread.interrupt();
    }

    private void prepare() throws IOException {
        Path serverRoot = TestFile.getTestDir();
        FTPServer server = FTPServer.getNonBlocking(serverRoot);
        client = Client.getNonBlocking();
        serverThread = new Thread(() -> {
            try {
                server.run(HOSTNAME, PORT);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        serverThread.start();

        //noinspection StatementWithEmptyBody
        while(!server.isReady());
        client.connect(HOSTNAME, PORT);
    }
}

class TestFile {
    private static final Path TEST_DIR;

    static Path getTestDir() {
        return TEST_DIR;
    }

    static {
        try {
            TEST_DIR = Files.createTempDirectory("FTP_root");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test directory");
        }
    }

    private String content;
    private Path path;

    TestFile(String path, String content){
        this.content = content;
        this.path = TEST_DIR.resolve(path);
    }
    void create() throws IOException {
        Path parent = path.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        Files.write(path, content.getBytes());
    }

    Path getPath() {
        return path;
    }

    byte[] getContent() {
        return content.getBytes();
    }
}