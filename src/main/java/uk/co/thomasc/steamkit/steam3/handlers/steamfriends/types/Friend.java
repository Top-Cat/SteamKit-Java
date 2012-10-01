package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendsList;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EFriendRelationship;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

/**
 * Represents a single friend entry in a client's friendlist.
 */
public final class Friend {
	/**
	 * Gets the SteamID of the friend.
	 */
	@Getter private SteamID steamID;

	/**
	 * Gets the relationship to this friend.
	 */
	@Getter private EFriendRelationship relationship;

	public Friend(CMsgClientFriendsList.Friend friend) {
		this.steamID = new SteamID(friend.getUlfriendid());
		this.relationship = EFriendRelationship.f(friend.getEfriendrelationship());
	}
}