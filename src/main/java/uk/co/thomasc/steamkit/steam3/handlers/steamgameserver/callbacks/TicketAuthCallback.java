package uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientTicketAuthComplete;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAuthSessionResponse;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.gameid.GameID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired when ticket authentication has completed.
 */
public final class TicketAuthCallback extends CallbackMsg {
	/**
	 * Gets the SteamID the ticket auth completed for.
	 */
	@Getter private final SteamID steamId;

	/**
	 * Gets the GameID the ticket was for.
	 */
	@Getter private final GameID gameId;

	/**
	 * Gets the authentication state.
	 */
	@Getter private final int state;

	/**
	 * Gets the auth session response.
	 */
	@Getter private final EAuthSessionResponse authSessionResponse;

	/**
	 * Gets the ticket CRC.
	 */
	@Getter private final int ticketCRC;

	/**
	 * Gets the ticket sequence.
	 */
	@Getter private final int ticketSequence;

	public TicketAuthCallback(CMsgClientTicketAuthComplete tickAuth) {
		steamId = new SteamID(tickAuth.getSteamId());
		gameId = new GameID(tickAuth.getGameId());

		state = tickAuth.getEstate();

		authSessionResponse = EAuthSessionResponse.f(tickAuth.getEauthSessionResponse());

		ticketCRC = tickAuth.getTicketCrc();
		ticketSequence = tickAuth.getTicketSequence();
	}
}
