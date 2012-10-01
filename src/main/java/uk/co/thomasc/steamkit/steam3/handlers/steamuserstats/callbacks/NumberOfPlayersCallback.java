package uk.co.thomasc.steamkit.steam3.handlers.steamuserstats.callbacks;

import lombok.Getter;


import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientGetNumberOfCurrentPlayersResponse;
import uk.co.thomasc.steamkit.steam3.handlers.steamuserstats.SteamUserStats;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.gameid.GameID;

/**
 * This callback is fired in response to {@link SteamUserStats#getNumberOfCurrentPlayers(GameID)}.
 */
public class NumberOfPlayersCallback extends CallbackMsg {
	/**
	 * Gets the result of the request.
	 */
	@Getter private EResult result;

	/**
	 * Gets the current number of players according to Steam.
	 */
	@Getter private int numPlayers;

	public NumberOfPlayersCallback(MsgClientGetNumberOfCurrentPlayersResponse resp) {
		this.result = resp.result;
		this.numPlayers = resp.numPlayers;
	}
}