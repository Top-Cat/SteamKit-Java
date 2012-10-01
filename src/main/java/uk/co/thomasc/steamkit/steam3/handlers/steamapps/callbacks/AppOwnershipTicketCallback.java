package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetAppOwnershipTicketResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamApps#getAppOwnershipTicket(int)}.
 */
public final class AppOwnershipTicketCallback extends CallbackMsg {
	/**
	 * Gets the result of requesting the ticket.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the AppID this ticket is for.
	 */
	@Getter private final int appID;

	/**
	 * Gets the ticket data.
	 */
	@Getter private final byte[] ticket;

	public AppOwnershipTicketCallback(CMsgClientGetAppOwnershipTicketResponse msg) {
		result = EResult.f(msg.getEresult());
		appID = msg.getAppId();
		ticket = msg.getTicket().toByteArray();
	}
}
