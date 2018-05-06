package com.microwise.msp.hardware.handler;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 测试发送
 *
 * @author gaohui
 * @date 13-12-11 10:13
 */
public class DatagramSocketTest {

    @Test
    public void testSend() throws IOException {
        String remoteIp = "127.0.0.1";
        int remotePort = 1234;
        DatagramSocket datagramSocket = new DatagramSocket();
        InetSocketAddress address = new InetSocketAddress(remoteIp, remotePort);
        byte[] bytes = new String("hello").getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address);
        datagramSocket.send(datagramPacket);
        datagramSocket.close();
    }
}
