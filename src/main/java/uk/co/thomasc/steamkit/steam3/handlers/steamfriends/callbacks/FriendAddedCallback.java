package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAddFriendResponse;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

/**
 * This callback is fired in response to adding a user to your friends list.
 */
public final class FriendAddedCallback extends CallbackMsg {
	/**
	 * Gets the result of the request.
	 */
	@Getter private EResult result;

	/**
	 * Gets the SteamID of the friend that was added.
	 */
	@Getter private SteamID steamID;

	/**
	 * Gets the persona name of the friend.
	 */
	@Getter private String personaName;

	public FriendAddedCallback(CMsgClientAddFriendResponse msg) {
		this.result = EResult.f(msg.getEresult());

		this.steamID = new SteamID(msg.getSteamIdAdded());

		this.personaName = msg.getPersonaNameAdded();
	}
}