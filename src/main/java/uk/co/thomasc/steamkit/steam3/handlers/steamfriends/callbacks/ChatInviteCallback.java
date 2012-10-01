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
	@Getter private SteamID invitedID;

	/**
	 * Gets the chat room SteamID.
	 */
	@Getter private SteamID chatRoomID;

	/**
	 * Gets the SteamID of the user who performed the invitation.
	 */
	@Getter private SteamID patronID;

	/**
	 * Gets the chat room type.
	 */
	@Getter private EChatRoomType chatRoomType;

	/**
	 * Gets the SteamID of the chat friend.
	 */
	@Getter private SteamID friendChatID;

	/**
	 * Gets the name of the chat room.
	 */
	@Getter private String chatRoomName;

	/**
	 * Gets the GameID associated with this chat room, if it's a game lobby.
	 */
	@Getter private GameID gameID;

	public ChatInviteCallback(CMsgClientChatInvite invite) {
		this.invitedID = new SteamID(invite.getSteamIdInvited());
		this.chatRoomID = new SteamID(invite.getSteamIdChat());

		this.patronID = new SteamID(invite.getSteamIdPatron());

		this.chatRoomType = EChatRoomType.f(invite.getChatroomType());

		this.friendChatID = new SteamID(invite.getSteamIdFriendChat());

		this.chatRoomName = invite.getChatName();
		this.gameID = new GameID(invite.getGameId());
	}
}