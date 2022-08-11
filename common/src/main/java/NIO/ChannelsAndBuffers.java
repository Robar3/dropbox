package NIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class ChannelsAndBuffers {
    /*
    is + os
    channel <- buffer
    channel -> buffer
    ByteBuffer buf
    _ _ _ _ _ 5b
    lim = 0
    pos = 0
    cap = 5
    buf.put(3b)
    lim = 1
    pos = 1
    cap = 5
    buf.get() -> Exception lim = 1 pos = 2 lim = 1
    buf.flip()
    pos = 0
    lim = 1
    cap = 5
    buf.get() -> 3
    buf.get() ->
    */

    public static void main(String[] args) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(5);
        buf.put((byte) 65);
        buf.put((byte) 66);
        buf.flip();
        while (buf.hasRemaining()) {
            System.out.println(buf.get());
        }
        buf.rewind();
        buf.put((byte) 67);
        buf.put((byte) 68);
        buf.flip();
        while (buf.hasRemaining()) {
            System.out.println(buf.get());
        }
        System.out.println(Arrays.toString(buf.array()));
        // buf.get();
        buf.flip();
        RandomAccessFile raf =
                new RandomAccessFile("./common/src/main/resources/1.txt",
                        "rw");
        FileChannel channel = raf.getChannel();
        channel.position(channel.size());
        channel.write(buf);
    }

}