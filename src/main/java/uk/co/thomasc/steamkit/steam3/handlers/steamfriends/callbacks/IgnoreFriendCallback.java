package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientSetIgnoreFriendResponse;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired in response to an attempt at ignoring a friend.
 */
public final class IgnoreFriendCallback extends CallbackMsg {
	/**
	 * Gets the result of ignoring a friend.
	 */
	@Getter private final EResult result;

	public IgnoreFriendCallback(MsgClientSetIgnoreFriendResponse response) {
		result = response.result;
	}
}
