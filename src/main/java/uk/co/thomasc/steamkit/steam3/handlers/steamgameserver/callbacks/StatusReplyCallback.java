package uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGSStatusReply;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired when the game server receives a status reply.
 */
public final class StatusReplyCallback extends CallbackMsg {
	/**
	 * Gets a value indicating whether this game server is VAC secure.
	 */
	@Getter private final boolean isSecure;

	public StatusReplyCallback(CMsgGSStatusReply reply) {
		isSecure = reply.getIsSecure();
	}
}
