package uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_InitiateTradeResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EEconTradeResponse;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired when this client receives the response from a trade proposal.
 */
public final class TradeResultCallback extends CallbackMsg {
	/**
	 * Gets the Trade ID that this result is for.
	 */
	@Getter private final int tradeID;

	/**
	 * Gets the response of the trade proposal.
	 */
	@Getter private final EEconTradeResponse response;

	/**
	 * Gets the SteamID of the client that responded to the proposal.
	 */
	@Getter private final SteamID otherClient;

	public TradeResultCallback(CMsgTrading_InitiateTradeResponse msg) {
		tradeID = msg.getTradeRequestId();

		response = EEconTradeResponse.f(msg.getResponse());

		otherClient = new SteamID(msg.getOtherSteamid());
	}
}
