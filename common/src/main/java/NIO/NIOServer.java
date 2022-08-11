package NIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOServer implements Runnable {

    ServerSocketChannel srv;
    Selector selector;

    @Override
    public void run() {
        try {
            srv = ServerSocketChannel.open();
            srv.bind(new InetSocketAddress(8189));
            System.out.println("server started!");
            srv.configureBlocking(false);
            selector = Selector.open();
            srv.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(); // block
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isAcceptable()) {
                        SocketChannel channel = ((ServerSocketChannel) key.channel())
                                .accept();
                        System.out.println("Client accepted");
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        SocketChannel channel = (SocketChannel) key.channel();
                        int cnt = channel.read(buffer);
                        if (cnt == -1) {
                            System.out.println("client leave chat!");
                            channel.close();
                        }
                        buffer.flip();
                        StringBuilder msg = new StringBuilder();
                        while (buffer.hasRemaining()) {
                            msg.append((char) buffer.get());
                        }
                        System.out.println(msg);
                        for (SelectionKey out : selector.keys()) {
                            if (out.isReadable() && out.channel() instanceof SocketChannel) {
                                ((SocketChannel) out.channel()).write(ByteBuffer.wrap
                                        (msg.toString().getBytes()));
                            }
                        }
                        //channel.close();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        new Thread(new NIOServer()).start();
    }
}