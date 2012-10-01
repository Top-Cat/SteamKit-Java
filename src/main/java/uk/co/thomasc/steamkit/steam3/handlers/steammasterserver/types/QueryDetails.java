package uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.types;

import java.net.InetAddress;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.ERegionCode;

/**
 * Details used when performing a server list query.
 */
public final class QueryDetails {
	/**
	 * Gets or sets the AppID used when querying servers.
	 */
	public int appId;

	/**
	 * Gets or sets the filter used for querying the master server.
	 * Check https://developer.valvesoftware.com/wiki/Master_Server_Query_Protocol for details on how the filter is structured.
	 */
	public String filter;

	/**
	 * Gets or sets the region that servers will be returned from.
	 */
	public ERegionCode region;

	/**
	 * Gets or sets the IP address that will be GeoIP located.
	 * This is done to return servers closer to this location.
	 */
	public InetAddress geoLocatedIP;

	/**
	 * Gets or sets the maximum number of servers to return.
	 */
	public int maxServers;
}
