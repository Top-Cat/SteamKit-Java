package uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.types;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGMSClientServerQueryResponse;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;
import uk.co.thomasc.steamkit.util.util.NetHelpers;

/**
 * Represents a single server.
 */
public final class Server {
	/**
	 * Gets the IP endpoint of the server.
	 */
	@Getter private final IPEndPoint endPoint;

	/**
	 * Gets the number of Steam authenticated players on this server.
	 */
	@Getter private final int authedPlayers;

	public Server(CMsgGMSClientServerQueryResponse.Server server) {
		endPoint = new IPEndPoint(NetHelpers.getIPAddress(server.getServerIp()), (short) server.getServerPort());

		authedPlayers = server.getAuthPlayers();
	}
}
