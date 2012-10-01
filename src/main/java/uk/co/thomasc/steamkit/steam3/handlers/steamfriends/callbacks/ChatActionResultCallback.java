package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;


import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatAction;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatActionResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatActionResult;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

/**
 * This callback is fired when a chat action has completed.
 */
public final class ChatActionResultCallback extends CallbackMsg {
	/**
	 * Gets the SteamID of the chat room the action was performed in.
	 */
	@Getter private SteamID chatRoomID;

	/**
	 * Gets the SteamID of the chat member the action was performed on.
	 */
	@Getter private SteamID chatterID;

	/**
	 * Gets the chat action that was performed.
	 */
	@Getter private EChatAction action;

	/**
	 * Gets the result of the chat action.
	 */
	@Getter private EChatActionResult result;

	public ChatActionResultCallback(MsgClientChatActionResult result) {
		chatRoomID = result.getSteamIdChat();
		chatterID = result.getSteamIdUserActedOn();

		action = result.chatAction;
		this.result = result.actionResult;
	}
}