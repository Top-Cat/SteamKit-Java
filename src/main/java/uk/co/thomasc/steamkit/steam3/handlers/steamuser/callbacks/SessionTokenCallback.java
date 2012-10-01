package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientSessionToken;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired when the client recieves it's unique Steam3 session token. This token is used for authenticated content downloading in Steam2.
 */
public final class SessionTokenCallback extends CallbackMsg {
	/**
	 * Gets the Steam3 session token used for authenticating to various other services.
	 */
	@Getter private final long sessionToken;

	public SessionTokenCallback(CMsgClientSessionToken msg) {
		sessionToken = msg.getToken();
	}
}
