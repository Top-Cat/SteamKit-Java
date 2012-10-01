package uk.co.thomasc.steamkit.util.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class NetHelpers {
	/*public static InetAddress getLocalIP(Socket activeSocket) {
		IPEndPoint ipEndPoint = activeSocket.LocalEndPoint as IPEndPoint;

		if ( ipEndPoint == null || ipEndPoint.Address == IPAddress.Any )
			throw new Exception( "Socket not connected" );

		return ipEndPoint.Address;
	}*/

	public static InetAddress getIPAddress(long ipAddr) {
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.putInt((int) ipAddr);

		try {
			return InetAddress.getByAddress(buff.array());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long getIPAddress(InetAddress ipAddr) {
		ByteBuffer buff = ByteBuffer.wrap(ipAddr.getAddress());
		return buff.getInt() & 0xFFFFFFFFL;
	}

	/*
	public static uint EndianSwap( uint input )
	{
		return ( uint )IPAddress.NetworkToHostOrder( ( int )input );
	}
	public static ulong EndianSwap( ulong input )
	{
		return ( ulong )IPAddress.NetworkToHostOrder( ( long )input );
	}
	public static ushort EndianSwap( ushort input )
	{
		return ( ushort )IPAddress.NetworkToHostOrder( ( short )input );
	}*/
}