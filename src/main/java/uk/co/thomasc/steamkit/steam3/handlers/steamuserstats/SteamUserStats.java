package uk.co.thomasc.steamkit.steam3.handlers.steamuserstats;

import uk.co.thomasc.steamkit.base.ClientMsg;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientGetNumberOfCurrentPlayers;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientGetNumberOfCurrentPlayersResponse;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamuserstats.callbacks.NumberOfPlayersCallback;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.gameid.GameID;

/**
 * This handler handles Steam user statistic related actions.
 */
public final class SteamUserStats extends ClientMsgHandler {
	/**
	 * Retrieves the number of current players or a given {@link GameID}.
	 * Results are returned in a {@link NumberOfPlayersCallback} from a {@link JobCallback}.
	 * @param gameId	The GameID to request the number of players for.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getNumberOfCurrentPlayers(GameID gameId) {
		final ClientMsg<MsgClientGetNumberOfCurrentPlayers> msg = new ClientMsg<MsgClientGetNumberOfCurrentPlayers>(MsgClientGetNumberOfCurrentPlayers.class);
		msg.setSourceJobID(getClient().getNextJobID());

		msg.getBody().setGameId(gameId);

		getClient().send(msg);

		return msg.getSourceJobID();
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case ClientGetNumberOfCurrentPlayersResponse:
				handleNumberOfPlayersResponse(packetMsg);
				break;
		}
	}

	void handleNumberOfPlayersResponse(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientGetNumberOfCurrentPlayersResponse> msg = new ClientMsg<MsgClientGetNumberOfCurrentPlayersResponse>(packetMsg, MsgClientGetNumberOfCurrentPlayersResponse.class);

		final NumberOfPlayersCallback innerCallback = new NumberOfPlayersCallback(msg.getBody());
		final JobCallback<?> callback = new JobCallback<NumberOfPlayersCallback>(new JobID(msg.getHeader().targetJobID), innerCallback);
		getClient().postCallback(callback);
	}
}
