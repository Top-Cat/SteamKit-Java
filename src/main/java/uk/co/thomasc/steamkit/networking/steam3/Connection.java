package uk.co.thomasc.steamkit.networking.steam3;

import java.io.IOException;
import java.net.InetAddress;


import uk.co.thomasc.steamkit.base.IClientMsg;
import uk.co.thomasc.steamkit.util.cSharp.events.Event;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.events.GenericEvent;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;

public abstract class Connection {
	/**
	 * The net filter.
	 */
	public NetFilterEncryption NetFilter;


	/**
	 * Occurs when a net message is recieved over the network.
	 */
	public Event<NetMsgEventArgs> NetMsgReceived = new Event<NetMsgEventArgs>();
	
	/**
	 * Raises the {@link #NetMsgReceived} event.
	 * @param e	The {@link NetMsgEventArgs} instance containing the event data.
	 */
	protected void onNetMsgReceived(NetMsgEventArgs e) {
		if (NetMsgReceived != null) {
			NetMsgReceived.handleEvent(this, e);
		}
	}

	/**
	 * Occurs when the physical connection is established.
	 */
	public GenericEvent Connected = new GenericEvent();
	protected void onConnected(EventArgs e) {
		if (Connected != null) {
			Connected.handleEvent(this, e);
		}
	}

	/**
	 * Occurs when the physical connection is broken.
	 */
	public GenericEvent Disconnected = new GenericEvent();
	protected void onDisconnected(EventArgs e) {
		if (Disconnected != null) {
			Disconnected.handleEvent(this, e);
		}
	}

	/**
	 * Connects to the specified end point.
	 * @param endPoint	The end point.
	 */
	public abstract void connect(IPEndPoint endPoint);
	/**
	 * Disconnects this instance.
	 */
	public abstract void disconnect();

	/**
	 * Sends the specified client net message.
	 * @param clientMsg	The client net message.
	 * @throws IOException 
	 */
	public abstract void send(IClientMsg clientMsg) throws IOException;

	/**
	 * Gets the local IP.
	 * @return The local IP
	 */
	public abstract InetAddress getLocalIP();
}
