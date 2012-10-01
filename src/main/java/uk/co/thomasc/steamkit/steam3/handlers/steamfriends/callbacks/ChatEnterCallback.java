package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatRoomEnterResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatRoomType;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatEnter;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired in response to attempting to join a chat.
 */
public final class ChatEnterCallback extends CallbackMsg {
	/**
	 * Gets SteamID of the chat room.
	 */
	@Getter private final SteamID chatID;

	/**
	 * Gets the friend ID.
	 */
	@Getter private final SteamID friendID;

	/**
	 * Gets the type of the chat room.
	 */
	@Getter private final EChatRoomType chatRoomType;

	/**
	 * Gets the SteamID of the chat room owner.
	 */
	@Getter private final SteamID ownerID;

	/**
	 * Gets clan SteamID that owns this chat room.
	 */
	@Getter private final SteamID clanID;

	/**
	 * Gets the chat flags.
	 */
	@Getter private final byte chatFlags;

	/**
	 * Gets the chat enter response.
	 */
	@Getter private final EChatRoomEnterResponse enterResponse;

	public ChatEnterCallback(MsgClientChatEnter msg) {
		chatID = msg.getSteamIdChat();
		friendID = msg.getSteamIdFriend();

		chatRoomType = msg.chatRoomType;

		ownerID = msg.getSteamIdOwner();
		clanID = msg.getSteamIdClan();

		chatFlags = msg.chatFlags;

		enterResponse = msg.enterResponse;
	}
}
