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
    void run(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        QueryProcessor queryProcessor = new QueryProcessor();
        while (!Thread.interrupted()) {
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
                        selectionKey.cancel();
                        SelectionKey newSelectionKey = selectionKey.channel().register(selector, SelectionKey.OP_WRITE);
                        newSelectionKey.attach(new SmallWritableMessage(message.getChannel(), queryProcessor.process(message.getData())));
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
