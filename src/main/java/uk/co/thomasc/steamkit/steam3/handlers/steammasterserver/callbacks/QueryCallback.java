package uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGMSClientServerQueryResponse;
import uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.SteamMasterServer;
import uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.types.QueryDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.types.Server;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamMasterServer#serverQuery(QueryDetails)}.
 */
public final class QueryCallback extends CallbackMsg {
	/**
	 * Gets the list of servers.
	 */
	@Getter private final List<Server> servers = new ArrayList<Server>();

	public QueryCallback(CMsgGMSClientServerQueryResponse msg) {
		for (final CMsgGMSClientServerQueryResponse.Server s : msg.getServersList()) {
			servers.add(new Server(s));
		}
	}
}
