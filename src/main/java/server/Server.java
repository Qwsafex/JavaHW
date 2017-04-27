package server;

import utils.SmallReadableMessage;
import utils.SmallWritableMessage;
import utils.WritableMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    public void run(String hostname, int port) throws IOException {
        System.out.println("run");
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("binded to " + hostname + ":" + port);
        serverSocketChannel.bind(new InetSocketAddress(hostname, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        QueryProcessor queryProcessor = new QueryProcessor();
        while (true) {
            //System.out.println("hey");
            selector.selectNow();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey newSelectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    newSelectionKey.attach(new SmallReadableMessage(socketChannel));
                }
                if (selectionKey.isReadable()) {
                    SmallReadableMessage message = (SmallReadableMessage) selectionKey.attachment();
                    if (message.read()) {
                        SelectionKey newSelectionKey = selectionKey.channel().register(selector, SelectionKey.OP_WRITE);
                        newSelectionKey.attach(queryProcessor.process(message.getData()).generateMessage(message.getChannel()));
                    }
                }
                if (selectionKey.isWritable()) {
                    WritableMessage message = (WritableMessage) selectionKey.attachment();
                    if (message.write()) {
                        selectionKey.channel().close();
                        selectionKey.cancel();
                    }
                }
                keyIterator.remove();
            }
        }
    }
}
