package org.spout.steamkit.util.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import uk.co.thomasc.steamkit.util.util.NetHelpers;
import static org.junit.Assert.assertEquals;

public class NetHelpersTest {
	
	@Test
	public void testIPConversion() {
		InetAddress ip;
		long ipV;
		try {
			ip = InetAddress.getByName("97.226.158.107");
			ipV = 1642241643;
			
			assertEquals(NetHelpers.getIPAddress(ip), ipV);
			assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			ip = InetAddress.getByName("28.106.15.222");
			ipV = 476712926;
			
			assertEquals(NetHelpers.getIPAddress(ip), ipV);
			assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			ip = InetAddress.getByName("252.106.110.134");
			ipV = 4234833542L;
			
			assertEquals(NetHelpers.getIPAddress(ip), ipV);
			assertEquals(NetHelpers.getIPAddress(ipV), ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}