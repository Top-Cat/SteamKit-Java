package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientChatInvite;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatRoomType;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.gameid.GameID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired when a chat invite is recieved.
 */
public final class ChatInviteCallback extends CallbackMsg {
	/**
	 * Gets the SteamID of the user who was invited to the chat.
	 */
	@Getter private final SteamID invitedID;

	/**
	 * Gets the chat room SteamID.
	 */
	@Getter private final SteamID chatRoomID;

	/**
	 * Gets the SteamID of the user who performed the invitation.
	 */
	@Getter private final SteamID patronID;

	/**
	 * Gets the chat room type.
	 */
	@Getter private final EChatRoomType chatRoomType;

	/**
	 * Gets the SteamID of the chat friend.
	 */
	@Getter private final SteamID friendChatID;

	/**
	 * Gets the name of the chat room.
	 */
	@Getter private final String chatRoomName;

	/**
	 * Gets the GameID associated with this chat room, if it's a game lobby.
	 */
	@Getter private final GameID gameID;

	public ChatInviteCallback(CMsgClientChatInvite invite) {
		invitedID = new SteamID(invite.getSteamIdInvited());
		chatRoomID = new SteamID(invite.getSteamIdChat());

		patronID = new SteamID(invite.getSteamIdPatron());

		chatRoomType = EChatRoomType.f(invite.getChatroomType());

		friendChatID = new SteamID(invite.getSteamIdFriendChat());

		chatRoomName = invite.getChatName();
		gameID = new GameID(invite.getGameId());
	}
}
