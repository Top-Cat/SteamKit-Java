package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import java.util.Arrays;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatEntryType;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatMsg;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired when a chat room message arrives.
 */
public final class ChatMsgCallback extends CallbackMsg {
	/**
	 * Gets the SteamID of the chatter.
	 */
	@Getter private final SteamID chatterID;

	/**
	 * Gets the SteamID of the chat room.
	 */
	@Getter private final SteamID chatRoomID;

	/**
	 * Gets chat entry type.
	 */
	@Getter private final EChatEntryType chatMsgType;

	/**
	 * Gets the message.
	 */
	@Getter private String message = "";

	public ChatMsgCallback(MsgClientChatMsg msg, byte[] payload) {
		chatterID = msg.getSteamIdChatter();
		chatRoomID = msg.getSteamIdChatRoom();

		chatMsgType = msg.chatMsgType;

		if (payload != null && payload.length > 0) {
			message = new String(Arrays.copyOfRange(payload, 0, payload.length - 1));
		}
	}
}
