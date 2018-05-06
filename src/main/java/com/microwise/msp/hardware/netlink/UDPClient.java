package com.microwise.msp.hardware.netlink;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

/**
 * UDPClient
 *
 * @author he.ming
 * @since Feb 5, 2013
 */
public class UDPClient {
    private static final Logger log = LoggerFactory.getLogger(UDPClient.class);

    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            log.error("", e);
        }
    }

    /**
     * UDP发送数据包(外网通信需要UDP打洞？)
     *
     * @param datagram 数据包
     * @param rAddress 远程地址
     * @param rPort    远程端口
     * @author he.ming
     * @since Feb 5, 2013
     */
    public static void sendDirectly(List<Byte> datagram, String rAddress, int rPort) {
        if (datagram == null || datagram.isEmpty()) {
            return;
        }

        sendDirectly(Bytes.toArray(datagram), new InetSocketAddress(rAddress, rPort));
    }

    public static void sendDirectly(byte[] datagram, String remoteHost, int port) {
        sendDirectly(datagram, new InetSocketAddress(remoteHost, port));
    }

    public static void sendDirectly(byte[] datagram, InetSocketAddress remoteAddress) {
        if (datagram == null || datagram.length == 0) {
            return;
        }

        try {
            DatagramPacket datagramPacket = new DatagramPacket(datagram, datagram.length, remoteAddress);
            socket.send(datagramPacket);
        } catch (SocketException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 优先使用原通道发送数据包, 如果原通道不存在，使用未知端口发送数据
     *
     * @param datagram 待发送的数据包
     * @author he.ming
     * @since Mar 12, 2013
     */
    public static void send(byte[] datagram, String rAddress, int rPort) {
        if (datagram == null || datagram.length == 0) {
            return;
        }

        // 优先使用已存在的链接, 否则另外发
        if (!UDPServer.sendByChannel(rAddress, rPort, datagram)) {
            sendDirectly(datagram, rAddress, rPort);
        }
    }
}
