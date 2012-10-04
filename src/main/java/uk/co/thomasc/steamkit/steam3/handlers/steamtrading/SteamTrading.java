package uk.co.thomasc.steamkit.steam3.handlers.steamtrading;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_CancelTradeRequest;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_InitiateTradeRequest;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_InitiateTradeResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgTrading_StartSession;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks.SessionStartCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks.TradeProposedCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamtrading.callbacks.TradeResultCallback;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This handler is used for initializing Steam trades with other clients.
 */
public final class SteamTrading extends ClientMsgHandler {

	/**
	 * Proposes a trade to another client.
	 * @param user	The client to trade.
	 */
	public void trade(SteamID user) {
		final ClientMsgProtobuf<CMsgTrading_InitiateTradeRequest.Builder> tradeReq = new ClientMsgProtobuf<CMsgTrading_InitiateTradeRequest.Builder>(CMsgTrading_InitiateTradeRequest.class, EMsg.EconTrading_InitiateTradeRequest);

		tradeReq.getBody().setOtherSteamid(user.convertToLong());

		getClient().send(tradeReq);
	}

	/**
	 * Responds to a trade proposal.
	 * @param tradeId		The trade id of the received proposal.
	 * @param acceptTrade	if set to true, the trade will be accepted.
	 */
	public void respondToTrade(int tradeId, boolean acceptTrade) {
		final ClientMsgProtobuf<CMsgTrading_InitiateTradeResponse.Builder> tradeResp = new ClientMsgProtobuf<CMsgTrading_InitiateTradeResponse.Builder>(CMsgTrading_InitiateTradeResponse.class, EMsg.EconTrading_InitiateTradeResponse);

		tradeResp.getBody().setTradeRequestId(tradeId);
		tradeResp.getBody().setResponse(acceptTrade ? 0 : 1);

		getClient().send(tradeResp);
	}

	/**
	 * Cancels an already sent trade proposal.
	 * @param user	The user.
	 */
	public void cancelTrade(SteamID user) {
		final ClientMsgProtobuf<CMsgTrading_CancelTradeRequest.Builder> cancelTrade = new ClientMsgProtobuf<CMsgTrading_CancelTradeRequest.Builder>(CMsgTrading_CancelTradeRequest.class, EMsg.EconTrading_CancelTradeRequest);

		cancelTrade.getBody().setOtherSteamid(user.convertToLong());

		getClient().send(cancelTrade);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case EconTrading_InitiateTradeProposed:
				handleTradeProposed(packetMsg);
				break;
			case EconTrading_InitiateTradeResult:
				handleTradeResult(packetMsg);
				break;
			case EconTrading_StartSession:
				handleStartSession(packetMsg);
				break;
		}
	}

	void handleTradeProposed(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgTrading_InitiateTradeRequest.Builder> tradeProp = new ClientMsgProtobuf<CMsgTrading_InitiateTradeRequest.Builder>(CMsgTrading_InitiateTradeRequest.class, packetMsg);

		final TradeProposedCallback callback = new TradeProposedCallback(tradeProp.getBody().build());
		getClient().postCallback(callback);
	}

	void handleTradeResult(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgTrading_InitiateTradeResponse.Builder> tradeResult = new ClientMsgProtobuf<CMsgTrading_InitiateTradeResponse.Builder>(CMsgTrading_InitiateTradeResponse.class, packetMsg);

		final TradeResultCallback callback = new TradeResultCallback(tradeResult.getBody().build());
		getClient().postCallback(callback);
	}

	void handleStartSession(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgTrading_StartSession.Builder> startSess = new ClientMsgProtobuf<CMsgTrading_StartSession.Builder>(CMsgTrading_StartSession.class, packetMsg);

		final SessionStartCallback callback = new SessionStartCallback(startSess.getBody().build());
		getClient().postCallback(callback);
	}
}
