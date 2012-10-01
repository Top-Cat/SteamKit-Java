package uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_InitiateTradeRequest;

import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

/**
 * This callback is fired when this client receives a trade proposal.
 */
public final class TradeProposedCallback extends CallbackMsg {
	/**
	 * Gets the Trade ID of his proposal, used for replying.
	 */
	@Getter private int tradeID;

	/**
	 * Gets the SteamID of the client that sent the proposal.
	 */
	@Getter private SteamID otherClient;

	/**
	 * Gets the persona name of the client that sent the proposal.
	 */
	@Getter private String otherName;


	public TradeProposedCallback(CMsgTrading_InitiateTradeRequest msg) {
		this.tradeID = msg.getTradeRequestId();

		this.otherClient = new SteamID(msg.getOtherSteamid());

		this.otherName = msg.getOtherName();
	}
}