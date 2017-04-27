import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Client {
    private enum Command {LIST, GET}

    private final SocketChannel channel;

    public Client() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
    }

    public void connnect(String hostname, int port) throws IOException {
        if (channel.isConnected()) return;
        channel.connect(new InetSocketAddress(hostname, port));

        //noinspection StatementWithEmptyBody
        while(channel.isConnectionPending());
    }
    public void disconnect() throws IOException {
        channel.close();
    }

    public List<SimpleFile> executeList(String path) throws IOException {
        sendRequest(createSentData((byte) Command.LIST.ordinal(), path.getBytes()));
        byte[] response = getSmallResponse();
        ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(response));
        try {
            //noinspection unchecked
            return (List<SimpleFile>) objectStream.readObject();
        } catch (ClassNotFoundException e) {
            // TODO: probably change to custom exception
            throw new RuntimeException("Server fucked up!");
        }
    }


    public String executeGet(String path) throws IOException {
        sendRequest(createSentData((byte) Command.GET.ordinal(), path.getBytes()));
        return getBigResponse().getFilename();
    }
    private byte[] getSmallResponse() throws IOException {
        SmallMessage message = new SmallMessage(channel);
        getResponse(message);
        return message.getData();
    }

    private BigMessage getBigResponse() throws IOException {
        BigMessage message = new BigMessage(channel);
        getResponse(message);
        return message;
    }

    private void getResponse(ReadableMessage message) throws IOException {
        //noinspection StatementWithEmptyBody
        while (!message.read());
    }

    private void sendRequest(byte[] data) {
        //noinspection StatementWithEmptyBody
        if (!channel.isConnected()) {
            throw new NotYetConnectedException();
        }
        WritableMessage message = new WritableMessage(channel, data);
        //noinspection StatementWithEmptyBody
        while (!message.write());
    }

    private byte[] createSentData(byte first, byte[] rest) {
        long size = Long.BYTES + rest.length;
        return ArrayUtils.addAll(ByteBuffer.allocate(4).putLong(size).put(first).array(), rest);
    }

}
