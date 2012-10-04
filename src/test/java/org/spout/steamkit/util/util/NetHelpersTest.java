package org.spout.steamkit.util.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

import uk.co.thomasc.steamkit.util.util.NetHelpers;

public class NetHelpersTest {

	@Test
	public void testIPConversion() {
		InetAddress ip;
		long ipV;
		try {
			ip = InetAddress.getByName("97.226.158.107");
			ipV = 1642241643;

			Assert.assertEquals(NetHelpers.getIPAddress(ip), ipV);
			Assert.assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			ip = InetAddress.getByName("28.106.15.222");
			ipV = 476712926;

			Assert.assertEquals(NetHelpers.getIPAddress(ip), ipV);
			Assert.assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			ip = InetAddress.getByName("252.106.110.134");
			ipV = 4234833542L;

			Assert.assertEquals(NetHelpers.getIPAddress(ip), ipV);
			Assert.assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
