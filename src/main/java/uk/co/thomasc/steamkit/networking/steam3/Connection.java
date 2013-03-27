package uk.co.thomasc.steamkit.networking.steam3;

import java.io.IOException;
import java.net.InetAddress;

import uk.co.thomasc.steamkit.base.IClientMsg;
import uk.co.thomasc.steamkit.util.cSharp.events.Event;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgsGeneric;
import uk.co.thomasc.steamkit.util.cSharp.events.GenericEvent;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;

public abstract class Connection {
	/**
	 * The net filter.
	 */
	public NetFilterEncryption netFilter;

	/**
	 * Occurs when a net message is recieved over the network.
	 */
	public Event<NetMsgEventArgs> netMsgReceived = new Event<NetMsgEventArgs>();

	/**
	 * Raises the {@link #netMsgReceived} event.
	 * @param e	The {@link NetMsgEventArgs} instance containing the event data.
	 */
	protected void onNetMsgReceived(NetMsgEventArgs e) {
		if (netMsgReceived != null) {
			netMsgReceived.handleEvent(this, e);
		}
	}

	/**
	 * Occurs when the physical connection is established.
	 */
	public GenericEvent connected = new GenericEvent();

	protected void onConnected(EventArgs e) {
		if (connected != null) {
			connected.handleEvent(this, e);
		}
	}

	/**
	 * Occurs when the physical connection is broken.
	 */
	public Event<EventArgsGeneric<Boolean>> disconnected = new Event<EventArgsGeneric<Boolean>>();

	protected void onDisconnected(boolean e) {
		if (disconnected != null) {
			disconnected.handleEvent(this, new EventArgsGeneric<Boolean>(e));
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
