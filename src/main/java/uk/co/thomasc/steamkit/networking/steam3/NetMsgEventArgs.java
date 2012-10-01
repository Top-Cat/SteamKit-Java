package uk.co.thomasc.steamkit.networking.steam3;

import lombok.Getter;

import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;

/**
 * Represents data that has been received over the network.
 */
public class NetMsgEventArgs extends EventArgs {
	@Getter private final byte[] data;
	@Getter private final IPEndPoint endPoint;

	public NetMsgEventArgs(byte[] data, IPEndPoint endPoint) {
		this.data = data;
		this.endPoint = endPoint;
	}
}
