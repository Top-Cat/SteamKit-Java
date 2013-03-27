package uk.co.thomasc.steamkit.steam3.handlers.steamfriends;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

import com.google.protobuf.ByteString;

import uk.co.thomasc.steamkit.base.ClientMsg;
import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAccountInfo;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAddFriend;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAddFriendResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientChangeStatus;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientChatInvite;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendMsgIncoming;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendsList;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPersonaState;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientRemoveFriend;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientRequestFriendData;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatAction;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatEntryType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatInfoType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatMemberStateChange;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EClanRelationship;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EClientPersonaStateFlag;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EFriendRelationship;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPersonaState;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatAction;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatActionResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatEnter;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatMemberInfo;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientJoinChat;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientSetIgnoreFriend;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientSetIgnoreFriendResponse;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.ChatActionResultCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.ChatEnterCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.ChatInviteCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.ChatMemberInfoCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.ChatMsgCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.FriendAddedCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.FriendMsgCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.FriendsListCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.IgnoreFriendCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks.PersonaStateCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types.AccountCache;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types.Clan;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types.User;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.gameid.GameID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This handler handles all interaction with other users on the Steam3 network.
 */
public final class SteamFriends extends ClientMsgHandler {
	private final Object listLock = new Object();
	@Getter private final List<SteamID> friendList;
	@Getter private final List<SteamID> clanList;

	AccountCache cache;

	public SteamFriends() {
		friendList = new ArrayList<SteamID>();
		clanList = new ArrayList<SteamID>();

		cache = new AccountCache();
	}

	/**
	 * Gets the local user's persona name.
	 * @return The name.
	 */
	public String getPersonaName() {
		return cache.getLocalUser().name;
	}

	/**
	 * Sets the local user's persona name and broadcasts it over the network.
	 * @param name	The name.
	 */
	public void setPersonaName(String name) {
		// cache the local name right away, so that early calls to SetPersonaState don't reset the set name
		cache.getLocalUser().name = name;

		final ClientMsgProtobuf<CMsgClientChangeStatus.Builder> stateMsg = new ClientMsgProtobuf<CMsgClientChangeStatus.Builder>(CMsgClientChangeStatus.class, EMsg.ClientChangeStatus);
		stateMsg.getBody().setPersonaState(cache.getLocalUser().personaState.v());
		stateMsg.getBody().setPlayerName(name);

		getClient().send(stateMsg);
	}

	/**
	 * Gets the local user's persona state.
	 * @return The persona state.
	 */
	public EPersonaState getPersonaState() {
		return cache.getLocalUser().personaState;
	}

	/**
	 * Sets the local user's persona state and broadcasts it over the network.
	 * @param state	The state.
	 */
	public void setPersonaState(EPersonaState state) {
		cache.getLocalUser().personaState = state;

		final ClientMsgProtobuf<CMsgClientChangeStatus.Builder> stateMsg = new ClientMsgProtobuf<CMsgClientChangeStatus.Builder>(CMsgClientChangeStatus.class, EMsg.ClientChangeStatus);
		stateMsg.getBody().setPersonaState(state.v());
		stateMsg.getBody().setPlayerName(cache.getLocalUser().name);

		getClient().send(stateMsg);
	}

	/**
	 * Gets the friend count of the local user.
	 * @return The number of friends.
	 */
	public int getFriendCount() {
		synchronized (listLock) {
			return friendList.size();
		}
	}

	/**
	 * Gets a friend by index.
	 * @param index	The index.
	 * @return A valid steamid of a friend if the index is in range; otherwise a steamid representing 0.
	 */
	public SteamID getFriendByIndex(int index) {
		synchronized (listLock) {
			if (index < 0 || index >= friendList.size()) {
				return new SteamID();
			}

			return friendList.get(index);
		}
	}

	/**
	 * Gets the persona name of a friend.
	 * @param steamId	The steam id.
	 * @return The name.
	 */
	public String getFriendPersonaName(SteamID steamId) {
		return cache.getUser(steamId).name;
	}

	/**
	 * Gets the persona state of a friend.
	 * @param steamId	The steam id.
	 * @return The persona state.
	 */
	public EPersonaState getFriendPersonaState(SteamID steamId) {
		return cache.getUser(steamId).personaState;
	}

	/**
	 * Gets the relationship of a friend.
	 * @param steamId	The steam id.
	 * @return The relationship of the friend to the local user.
	 */
	public EFriendRelationship getFriendRelationship(SteamID steamId) {
		return cache.getUser(steamId).relationship;
	}

	/**
	 * Gets the game name of a friend playing a game.
	 * @param steamId	The steam id.
	 * @return The game name of a friend playing a game, or null if they haven't been cached yet.
	 */
	public String getFriendGamePlayedName(SteamID steamId) {
		return cache.getUser(steamId).gameName;
	}

	/**
	 * Gets the GameID of a friend playing a game.
	 * @param steamId	The steam id.
	 * @return The gameid of a friend playing a game, or 0 if they haven't been cached yet.
	 */
	public GameID getFriendGamePlayed(SteamID steamId) {
		return cache.getUser(steamId).gameId;
	}

	/**
	 * Gets a SHA-1 hash representing the friend's avatar.
	 * @param steamId	The SteamID of the friend to get the avatar of.
	 * @return A byte array representing a SHA-1 hash of the friend's avatar.
	 */
	public byte[] getFriendAvatar(SteamID steamId) {
		return cache.getUser(steamId).avatarHash;
	}

	/**
	 * Gets the count of clans the local user is a member of.
	 * @return The number of clans this user is a member of.
	 */
	public int getClanCount() {
		synchronized (listLock) {
			return clanList.size();
		}
	}

	/**
	 * Gets a clan SteamID by index.
	 * @param index	The index.
	 * @return A valid steamid of a clan if the index is in range; otherwise a steamid representing 0.
	 */
	public SteamID getClanByIndex(int index) {
		synchronized (listLock) {
			if (index < 0 || index >= clanList.size()) {
				return new SteamID();
			}

			return clanList.get(index);
		}
	}

	/**
	 * Gets the name of a clan.
	 * @param steamId	The clan SteamID.
	 * @return The name.
	 */
	public String getClanName(SteamID steamId) {
		return cache.getClans().getAccount(steamId).name;
	}

	/**
	 * Gets the relationship of a clan.
	 * @param steamId	The clan steamid.
	 * @return The relationship of the clan to the local user.
	 */
	public EClanRelationship getClanRelationship(SteamID steamId) {
		return cache.getClans().getAccount(steamId).relationship;
	}

	/**
	 * Gets a SHA-1 hash representing the clan's avatar.
	 * @param steamId	The SteamID of the clan to get the avatar of.
	 * @return A byte array representing a SHA-1 hash of the clan's avatar, or null if the clan could not be found.
	 */
	public byte[] getClanAvatar(SteamID steamId) {
		return cache.getClans().getAccount(steamId).avatarHash;
	}

	/**
	 * Sends a chat message to a friend.
	 * @param target	The target to send to.
	 * @param type		The type of message to send.
	 * @param message	The message to send.
	 */
	public void sendChatMessage(SteamID target, EChatEntryType type, String message) {
		final ClientMsgProtobuf<CMsgClientFriendMsg.Builder> chatMsg = new ClientMsgProtobuf<CMsgClientFriendMsg.Builder>(CMsgClientFriendMsg.class, EMsg.ClientFriendMsg);

		chatMsg.getBody().setSteamid(target.convertToLong());
		chatMsg.getBody().setChatEntryType(type.v());
		chatMsg.getBody().setMessage(ByteString.copyFromUtf8(message));

		getClient().send(chatMsg);
	}

	/**
	 * Sends a friend request to a user.
	 * @param accountNameOrEmail	The account name or email of the user.
	 */
	public void addFriend(String accountNameOrEmail) {
		final ClientMsgProtobuf<CMsgClientAddFriend.Builder> addFriend = new ClientMsgProtobuf<CMsgClientAddFriend.Builder>(CMsgClientAddFriend.class, EMsg.ClientAddFriend);

		addFriend.getBody().setAccountnameOrEmailToAdd(accountNameOrEmail);

		getClient().send(addFriend);
	}

	/**
	 * Sends a friend request to a user.
	 * @param steamId	The SteamID of the friend to add.
	 */
	public void addFriend(SteamID steamId) {
		final ClientMsgProtobuf<CMsgClientAddFriend.Builder> addFriend = new ClientMsgProtobuf<CMsgClientAddFriend.Builder>(CMsgClientAddFriend.class, EMsg.ClientAddFriend);

		addFriend.getBody().setSteamidToAdd(steamId.convertToLong());

		getClient().send(addFriend);
	}

	/**
	 * Removes a friend from your friends list.
	 * @param steamId	The SteamID of the friend to remove.
	 */
	public void removeFriend(SteamID steamId) {
		final ClientMsgProtobuf<CMsgClientRemoveFriend.Builder> removeFriend = new ClientMsgProtobuf<CMsgClientRemoveFriend.Builder>(CMsgClientRemoveFriend.class, EMsg.ClientRemoveFriend);

		removeFriend.getBody().setFriendid(steamId.convertToLong());

		getClient().send(removeFriend);
	}

	/**
	 * Attempts to join a chat room.
	 * @param steamId	The SteamID of the chat room.
	 */
	public void joinChat(SteamID steamId) {
		final SteamID chatId = steamId.clone(); // copy the steamid so we don't modify it

		final ClientMsg<MsgClientJoinChat> joinChat = new ClientMsg<MsgClientJoinChat>(MsgClientJoinChat.class);

		if (chatId.isClanAccount()) {
			// this steamid is incorrect, so we'll fix it up
			chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
			chatId.setAccountType(EAccountType.Chat);
		}

		joinChat.getBody().setSteamIdChat(chatId);

		getClient().send(joinChat);

	}

	/**
	 * Attempts to leave a chat room.
	 * @param steamId	The SteamID of the chat room.
	 */
	public void leaveChat(SteamID steamId) {
		final SteamID chatId = steamId.clone(); // copy the steamid so we don't modify it

		final ClientMsg<MsgClientChatMemberInfo> leaveChat = new ClientMsg<MsgClientChatMemberInfo>(MsgClientChatMemberInfo.class);

		if (chatId.isClanAccount()) {
			// this steamid is incorrect, so we'll fix it up
			chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
			chatId.setAccountType(EAccountType.Chat);
		}

		leaveChat.getBody().setSteamIdChat(chatId);
		leaveChat.getBody().type = EChatInfoType.StateChange;

		try {
			leaveChat.write(getClient().getSteamId().convertToLong()); // ChatterActedOn
			leaveChat.write(EChatMemberStateChange.Left.v()); // StateChange
			leaveChat.write(getClient().getSteamId().convertToLong()); // ChatterActedBy
		} catch (final IOException e) {
			e.printStackTrace();
		}

		getClient().send(leaveChat);
	}

	/**
	 * Sends a message to a chat room.
	 * @param steamIdChat	The SteamID of the chat room.
	 * @param type			The message type.
	 * @param message		The message.
	 */
	public void sendChatRoomMessage(SteamID steamIdChat, EChatEntryType type, String message) {
		final SteamID chatId = steamIdChat.clone(); // copy the steamid so we don't modify it

		if (chatId.isClanAccount()) {
			// this steamid is incorrect, so we'll fix it up
			chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
			chatId.setAccountType(EAccountType.Chat);
		}

		final ClientMsg<MsgClientChatMsg> chatMsg = new ClientMsg<MsgClientChatMsg>(MsgClientChatMsg.class);

		chatMsg.getBody().chatMsgType = type;
		chatMsg.getBody().setSteamIdChatRoom(chatId);
		chatMsg.getBody().setSteamIdChatter(getClient().getSteamId());

		try {
			chatMsg.writeNullTermString(message, Charset.forName("UTF8"));
		} catch (final IOException e) {
			e.printStackTrace();
		}

		getClient().send(chatMsg);
	}

	/**
	 * Kicks the specified chat member from the given chat room.
	 * @param steamIdChat	The SteamID of chat room to kick the member from.
	 * @param steamIdMember	The SteamID of the member to kick from the chat.
	 */
	public void kickChatMember(SteamID steamIdChat, SteamID steamIdMember) {
		final SteamID chatId = steamIdChat.clone(); // copy the steamid so we don't modify it

		final ClientMsg<MsgClientChatAction> kickMember = new ClientMsg<MsgClientChatAction>(MsgClientChatAction.class);

		if (chatId.isClanAccount()) {
			// this steamid is incorrect, so we'll fix it up
			chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
			chatId.setAccountType(EAccountType.Chat);
		}

		kickMember.getBody().setSteamIdChat(chatId);
		kickMember.getBody().setSteamIdUserToActOn(steamIdMember);

		kickMember.getBody().chatAction = EChatAction.Kick;

		getClient().send(kickMember);
	}

	/**
	 * Bans the specified chat member from the given chat room.
	 * @param steamIdChat	The SteamID of chat room to ban the member from.
	 * @param steamIdMember	The SteamID of the member to ban from the chat.
	 */
	public void banChatMember(SteamID steamIdChat, SteamID steamIdMember) {
		final SteamID chatId = steamIdChat.clone(); // copy the steamid so we don't modify it

		final ClientMsg<MsgClientChatAction> banMember = new ClientMsg<MsgClientChatAction>(MsgClientChatAction.class);

		if (chatId.isClanAccount()) {
			// this steamid is incorrect, so we'll fix it up
			chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
			chatId.setAccountType(EAccountType.Chat);
		}

		banMember.getBody().setSteamIdChat(chatId);
		banMember.getBody().setSteamIdUserToActOn(steamIdMember);

		banMember.getBody().chatAction = EChatAction.Ban;

		getClient().send(banMember);
	}

	// the default details to request in most situations
	final int defaultInfoRequest = EClientPersonaStateFlag.PlayerName.v() | EClientPersonaStateFlag.Presence.v() | EClientPersonaStateFlag.SourceID.v() | EClientPersonaStateFlag.GameExtraInfo.v();

	/**
	 * Requests persona state for a list of specified SteamID.
	 * Results are returned in {@link PersonaStateCallback}.
	 * @param steamIdList	A list of SteamIDs to request the info of.
	 * @param requestedInfo	The requested info flags.
	 */
	public void requestFriendInfo(Collection<SteamID> steamIdList, int requestedInfo) {
		final ClientMsgProtobuf<CMsgClientRequestFriendData.Builder> request = new ClientMsgProtobuf<CMsgClientRequestFriendData.Builder>(CMsgClientRequestFriendData.class, EMsg.ClientRequestFriendData);

		for (final SteamID steamId : steamIdList) {
			request.getBody().getFriendsList().add(steamId.convertToLong());
		}
		request.getBody().setPersonaStateRequested(requestedInfo);

		getClient().send(request);
	}

	public void requestFriendInfo(Collection<SteamID> steamIdList, EClientPersonaStateFlag requestedInfo) {
		requestFriendInfo(steamIdList, requestedInfo.v());
	}

	public void requestFriendInfo(Collection<SteamID> steamIdList) {
		requestFriendInfo(steamIdList, defaultInfoRequest);
	}

	/**
	 * Requests persona state for a specified SteamID.
	 * Results are returned in {@link PersonaStateCallback}.
	 * @param steamId		A SteamID to request the info of.
	 * @param requestedInfo	The requested info flags.
	 */
	public void requestFriendInfo(SteamID steamId, int requestedInfo) {
		final List<SteamID> temp = new ArrayList<SteamID>();
		temp.add(steamId);
		requestFriendInfo(temp, defaultInfoRequest);
	}

	public void requestFriendInfo(SteamID steamId, EClientPersonaStateFlag requestedInfo) {
		requestFriendInfo(steamId, requestedInfo.v());
	}

	public void requestFriendInfo(SteamID steamId) {
		requestFriendInfo(steamId, defaultInfoRequest);
	}

	/**
	 * Ignores or unignores a friend on Steam.
	 * Results are returned in a {@link IgnoreFriendCallback}.
	 * @param steamId	The SteamID of the friend to ignore or unignore.
	 * @param setIgnore	if set to true, the friend will be ignored; otherwise, they will be unignored.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID ignoreFriend(SteamID steamId, boolean setIgnore) {
		final ClientMsg<MsgClientSetIgnoreFriend> ignore = new ClientMsg<MsgClientSetIgnoreFriend>(MsgClientSetIgnoreFriend.class);
		ignore.setSourceJobID(getClient().getNextJobID());

		ignore.getBody().setMySteamId(getClient().getSteamId());
		ignore.getBody().ignore = (byte) (setIgnore ? 1 : 0);
		ignore.getBody().setSteamIdFriend(steamId);

		getClient().send(ignore);

		return ignore.getSourceJobID();
	}

	public JobID ignoreFriend(SteamID steamId) {
		return ignoreFriend(steamId, true);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case ClientPersonaState:
				handlePersonaState(packetMsg);
				break;
			case ClientFriendsList:
				handleFriendsList(packetMsg);
				break;
			case ClientFriendMsgIncoming:
				handleFriendMsg(packetMsg);
				break;
			case ClientAccountInfo:
				handleAccountInfo(packetMsg);
				break;
			case ClientAddFriendResponse:
				handleFriendResponse(packetMsg);
				break;
			case ClientChatEnter:
				handleChatEnter(packetMsg);
				break;
			case ClientChatMsg:
				handleChatMsg(packetMsg);
				break;
			case ClientChatMemberInfo:
				handleChatMemberInfo(packetMsg);
				break;
			case ClientChatActionResult:
				handleChatActionResult(packetMsg);
				break;
			case ClientChatInvite:
				handleChatInvite(packetMsg);
				break;
			case ClientSetIgnoreFriendResponse:
				handleIgnoreFriendResponse(packetMsg);
				break;
		}
	}

	void handleAccountInfo(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientAccountInfo.Builder> accInfo = new ClientMsgProtobuf<CMsgClientAccountInfo.Builder>(CMsgClientAccountInfo.class, packetMsg);

		// cache off our local name
		cache.getLocalUser().name = accInfo.getBody().getPersonaName();
	}

	void handleFriendMsg(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientFriendMsgIncoming.Builder> friendMsg = new ClientMsgProtobuf<CMsgClientFriendMsgIncoming.Builder>(CMsgClientFriendMsgIncoming.class, packetMsg);

		final FriendMsgCallback callback = new FriendMsgCallback(friendMsg.getBody().build());
		getClient().postCallback(callback);
	}

	void handleFriendsList(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientFriendsList.Builder> list = new ClientMsgProtobuf<CMsgClientFriendsList.Builder>(CMsgClientFriendsList.class, packetMsg);

		cache.getLocalUser().steamId = getClient().getSteamId();

		if (!list.getBody().getBincremental()) {
			// if we're not an incremental update, the message contains all friends, so we should clear our current list
			synchronized (listLock) {
				friendList.clear();
				clanList.clear();
			}
		}

		// we have to request information for all of our friends because steam only sends persona information for online friends
		final ClientMsgProtobuf<CMsgClientRequestFriendData.Builder> reqInfo = new ClientMsgProtobuf<CMsgClientRequestFriendData.Builder>(CMsgClientRequestFriendData.class, EMsg.ClientRequestFriendData);

		reqInfo.getBody().setPersonaStateRequested(defaultInfoRequest);

		synchronized (listLock) {
			final List<SteamID> friendsToRemove = new ArrayList<SteamID>();
			final List<SteamID> clansToRemove = new ArrayList<SteamID>();

			for (final CMsgClientFriendsList.Friend friendObj : list.getBody().getFriendsList()) {
				final SteamID friendId = new SteamID(friendObj.getUlfriendid());

				if (friendId.isIndividualAccount()) {
					final User user = cache.getUser(friendId);

					user.relationship = EFriendRelationship.f(friendObj.getEfriendrelationship());

					if (friendList.contains(friendId)) {
						// if this is a friend on our list, and they removed us, mark them for removal
						if (user.relationship == EFriendRelationship.None) {
							friendsToRemove.add(friendId);
						}
					} else {
						// we don't know about this friend yet, lets add them
						friendList.add(friendId);
					}
				} else if (friendId.isClanAccount()) {
					final Clan clan = cache.getClans().getAccount(friendId);

					clan.relationship = EClanRelationship.f(friendObj.getEfriendrelationship());

					if (clanList.contains(friendId)) {
						// mark clans we were removed/kicked from
						// note: not actually sure about the kicked relationship, but i'm using it for good measure
						if (clan.relationship == EClanRelationship.None || clan.relationship == EClanRelationship.Kicked) {
							clansToRemove.add(friendId);
						}
					} else {
						// don't know about this clan, add it
						clanList.add(friendId);
					}
				}

				if (!list.getBody().getBincremental()) {
					// request persona state for our friend & clan list when it's a non-incremental update
					reqInfo.getBody().addFriends(friendId.convertToLong());
				}
			}

			// remove anything we marked for removal
			friendList.removeAll(friendsToRemove);
			clanList.removeAll(clansToRemove);
		}

		if (reqInfo.getBody().getFriendsCount() > 0) {
			getClient().send(reqInfo);
		}

		final FriendsListCallback callback = new FriendsListCallback(list.getBody().build());
		getClient().postCallback(callback);
	}

	void handlePersonaState(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientPersonaState.Builder> perState = new ClientMsgProtobuf<CMsgClientPersonaState.Builder>(CMsgClientPersonaState.class, packetMsg);

		final int flags = perState.getBody().getStatusFlags();

		for (final CMsgClientPersonaState.Friend friend : perState.getBody().getFriendsList()) {
			final SteamID friendId = new SteamID(friend.getFriendid());

			//SteamID sourceId = new SteamID(friend.getSteamidSource());

			if (friendId.isIndividualAccount()) {
				final User cacheFriend = cache.getUser(friendId);

				if ((flags & EClientPersonaStateFlag.PlayerName.v()) == EClientPersonaStateFlag.PlayerName.v()) {
					cacheFriend.name = friend.getPlayerName();
				}

				if ((flags & EClientPersonaStateFlag.Presence.v()) == EClientPersonaStateFlag.Presence.v()) {
					cacheFriend.avatarHash = friend.getAvatarHash().toByteArray();
					cacheFriend.personaState = EPersonaState.f(friend.getPersonaState());
				}

				if ((flags & EClientPersonaStateFlag.GameExtraInfo.v()) == EClientPersonaStateFlag.GameExtraInfo.v()) {
					cacheFriend.gameName = friend.getGameName();
					cacheFriend.gameId = new GameID(friend.getGameid());
					cacheFriend.gameAppId = friend.getGamePlayedAppId();
				}
			} else if (friendId.isClanAccount()) {
				final Clan cacheClan = cache.getClans().getAccount(friendId);

				if ((flags & EClientPersonaStateFlag.PlayerName.v()) == EClientPersonaStateFlag.PlayerName.v()) {
					cacheClan.name = friend.getPlayerName();
				}
			}

			// TODO: cache other details/account types?
		}

		for (final CMsgClientPersonaState.Friend friend : perState.getBody().getFriendsList()) {
			final PersonaStateCallback callback = new PersonaStateCallback(friend);
			getClient().postCallback(callback);
		}
	}

	void handleFriendResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientAddFriendResponse.Builder> friendResponse = new ClientMsgProtobuf<CMsgClientAddFriendResponse.Builder>(CMsgClientAddFriendResponse.class, packetMsg);

		final FriendAddedCallback callback = new FriendAddedCallback(friendResponse.getBody().build());
		getClient().postCallback(callback);
	}

	void handleChatEnter(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientChatEnter> chatEnter = new ClientMsg<MsgClientChatEnter>(packetMsg, MsgClientChatEnter.class);

		final ChatEnterCallback callback = new ChatEnterCallback(chatEnter.getBody());
		getClient().postCallback(callback);
	}

	void handleChatMsg(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientChatMsg> chatMsg = new ClientMsg<MsgClientChatMsg>(packetMsg, MsgClientChatMsg.class);

		byte[] msgData = new byte[0];
		try {
			msgData = chatMsg.getReader().readBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final ChatMsgCallback callback = new ChatMsgCallback(chatMsg.getBody(), msgData);
		getClient().postCallback(callback);
	}

	void handleChatMemberInfo(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientChatMemberInfo> membInfo = new ClientMsg<MsgClientChatMemberInfo>(packetMsg, MsgClientChatMemberInfo.class);

		try {
			byte[] payload = membInfo.getReader().readBytes();
			
			final ChatMemberInfoCallback callback = new ChatMemberInfoCallback(membInfo.getBody(), payload);
			getClient().postCallback(callback);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void handleChatActionResult(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientChatActionResult> actionResult = new ClientMsg<MsgClientChatActionResult>(packetMsg, MsgClientChatActionResult.class);

		final ChatActionResultCallback callback = new ChatActionResultCallback(actionResult.getBody());
		getClient().postCallback(callback);
	}

	void handleChatInvite(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientChatInvite.Builder> chatInvite = new ClientMsgProtobuf<CMsgClientChatInvite.Builder>(CMsgClientChatInvite.class, packetMsg);

		final ChatInviteCallback callback = new ChatInviteCallback(chatInvite.getBody().build());
		getClient().postCallback(callback);
	}

	void handleIgnoreFriendResponse(IPacketMsg packetMsg) {
		final ClientMsg<MsgClientSetIgnoreFriendResponse> response = new ClientMsg<MsgClientSetIgnoreFriendResponse>(packetMsg, MsgClientSetIgnoreFriendResponse.class);

		final IgnoreFriendCallback innerCallback = new IgnoreFriendCallback(response.getBody());
		final JobCallback<?> callback = new JobCallback<IgnoreFriendCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}
}
