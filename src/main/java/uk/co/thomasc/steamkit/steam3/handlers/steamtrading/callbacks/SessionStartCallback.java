package uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_StartSession;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired when a trading session has started.
 */
public final class SessionStartCallback extends CallbackMsg {
	/**
	 * Gets the SteamID of the client that this the trading session has started with.
	 */
	@Getter private final SteamID otherClient;

	public SessionStartCallback(CMsgTrading_StartSession msg) {
		otherClient = new SteamID(msg.getOtherSteamid());
	}
}
