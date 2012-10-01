package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientNewLoginKey;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is returned some time after logging onto the network.
 */
public final class LoginKeyCallback extends CallbackMsg {
	/**
	 * Gets the login key.
	 */
	@Getter private final String loginKey;

	/**
	 * Gets the unique ID.
	 */
	@Getter private final int uniqueId;

	public LoginKeyCallback(CMsgClientNewLoginKey logKey) {
		loginKey = logKey.getLoginKey();
		uniqueId = logKey.getUniqueId();
	}
}
