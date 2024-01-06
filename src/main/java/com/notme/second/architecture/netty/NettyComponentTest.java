package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.protocol.Message;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author monstaxl
 **/
public class NettyComponentTest {

    public static void main(String[] args) throws Exception {
        System.out.println("启动Server");
        NettyServer server = new NettyServer();
        new Thread(() -> {
            server.start();
        }).start();
        NetworkAddress address = server.address();
        System.out.println("NettyServer address: " + address);

        TimeUnit.SECONDS.sleep(3);

        ByteBuffer buf = ByteBuffer.allocate(1024);


        System.out.println("启动Client");
        NettyClient client = new NettyClient(address);
        new Thread(() -> {
            client.start();
        }).start();

        TimeUnit.SECONDS.sleep(3);

        for (int i = 0; i < 3 + ThreadLocalRandom.current().nextInt(3); i++) {
            client.send(nextMessage());
        }
    }

    static int counter = 1;

    static Message nextMessage() {
        byte[] content = ("message from client, it's counter: " + counter++).getBytes(StandardCharsets.UTF_8);
        return new Message.Builder()
                .bizCode(1)
                .body(content)
                .bodyLength(content.length)
                .build();

    }

}
