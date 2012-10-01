package uk.co.thomasc.steamkit.util.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class NetHelpers {
	public static InetAddress getIPAddress(long ipAddr) {
		final ByteBuffer buff = ByteBuffer.allocate(4);
		buff.putInt((int) ipAddr);

		try {
			return InetAddress.getByAddress(buff.array());
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long getIPAddress(InetAddress ipAddr) {
		final ByteBuffer buff = ByteBuffer.wrap(ipAddr.getAddress());
		return buff.getInt() & 0xFFFFFFFFL;
	}
}
