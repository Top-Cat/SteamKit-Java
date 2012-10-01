package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendsList;

import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types.Friend;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired when the client receives a list of friends.
 */
public final class FriendsListCallback extends CallbackMsg {
	/**
	 * Gets a value indicating whether this {@link FriendsListCallback} is an incremental update.
	 */
	@Getter private boolean incremental;

	/**
	 * Gets the friend list.
	 */
	@Getter private Set<Friend> friendList = new HashSet<Friend>();

	public FriendsListCallback(CMsgClientFriendsList msg) {
		this.incremental = msg.getBincremental();

		for (CMsgClientFriendsList.Friend friend : msg.getFriendsList()) {
			friendList.add(new Friend(friend));
		}
	}
}