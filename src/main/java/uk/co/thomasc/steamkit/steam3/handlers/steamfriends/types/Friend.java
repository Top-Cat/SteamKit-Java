package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendsList;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EFriendRelationship;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * Represents a single friend entry in a client's friendlist.
 */
public final class Friend {
	/**
	 * Gets the SteamID of the friend.
	 */
	@Getter private final SteamID steamId;

	/**
	 * Gets the relationship to this friend.
	 */
	@Getter private final EFriendRelationship relationship;

	public Friend(CMsgClientFriendsList.Friend friend) {
		steamId = new SteamID(friend.getUlfriendid());
		relationship = EFriendRelationship.f(friend.getEfriendrelationship());
	}
}
