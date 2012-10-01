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
	@Getter private int tradeID;

	/**
	 * Gets the response of the trade proposal.
	 */
	@Getter private EEconTradeResponse response;

	/**
	 * Gets the SteamID of the client that responded to the proposal.
	 */
	@Getter private SteamID otherClient;

	public TradeResultCallback( CMsgTrading_InitiateTradeResponse msg ) {
		this.tradeID = msg.getTradeRequestId();

		this.response = EEconTradeResponse.f(msg.getResponse());

		this.otherClient = new SteamID(msg.getOtherSteamid());
	}
}