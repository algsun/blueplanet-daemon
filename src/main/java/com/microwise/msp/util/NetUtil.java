/**
 *  Mar 7, 2013 
 */
package com.microwise.msp.util;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * @author he.ming
 * @since Mar 7, 2013
 */
public class NetUtil {

	public static void main(String[] args) {
		NetUtil.ping("192.168.0.166");
	}

	public static boolean ping(String host) {
		try {
			InetAddress address = null;
			if (host != null && host.trim().length() > 0) {
				address = InetAddress.getByName(host);
			}
			if (address != null && address.isReachable(5000)) {
				return true;
			} else {
				System.out.println(host.concat(" is unrecongized "));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 测试主机是否可达
	 * 
	 * @param ip
	 * @return
	 * @author he.ming
	 * @since Mar 7, 2013
	 */
	public static boolean isAddressAvailable(String ip) {

		try {
			InetAddress address = InetAddress.getByName(ip); // ping this ip

			if (address instanceof Inet4Address) {
				System.out.println(ip.concat(" is ipv4 address"));
			} else if (address instanceof Inet6Address) {
				System.out.println(ip.concat(" is ipv6 address"));
			} else {
				System.out.println(ip.concat(" is unrecongized"));
			}

			if (address.isReachable(5000)) {
				System.out.println("SUCCESS - ping ".concat(ip).concat(
						" with no interface specified "));
				return true;
			} else {
				System.out.println("FAILURE - ping ".concat(ip).concat(
						" with no interface specified "));
			}

			System.out
					.println("\n-------------Trying different interfaces---------------\n");

			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				System.out.println("Checking interface,DisplayName:".concat(
						ni.getDisplayName()).concat(",Name:").concat(
						ni.getName()));
				if (address.isReachable(ni, 0, 5000)) {
					System.out.println("SUCCESS - ping ".concat(ip));
				} else {
					System.out.println("FAILURE - ping ".concat(ip));
				}

				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					System.out.println("IP:".concat(ips.nextElement()
							.getHostAddress()));
				}
				System.out.println("---------------------------------------");
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
