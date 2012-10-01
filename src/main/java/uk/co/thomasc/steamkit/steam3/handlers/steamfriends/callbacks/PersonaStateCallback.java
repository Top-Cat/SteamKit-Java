package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import java.net.InetAddress;
import java.util.Date;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPersonaState;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EClientPersonaStateFlag;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPersonaState;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.gameid.GameID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.util.NetHelpers;

/**
 * This callback is fired in response to someone changing their friend details over the network.
 */
public final class PersonaStateCallback extends CallbackMsg {
	/**
	 * Gets the status flags. This shows what has changed.
	 */
	@Getter private final EClientPersonaStateFlag statusFlags;

	/**
	 * Gets the friend ID.
	 */
	@Getter private final SteamID friendID;

	/**
	 * Gets the state.
	 */
	@Getter private final EPersonaState state;

	/**
	 * Gets the game app ID.
	 */
	@Getter private final int gameAppID;

	/**
	 * Gets the game ID.
	 */
	@Getter private final GameID gameID;

	/**
	 * Gets the name of the game.
	 */
	@Getter private final String gameName;

	/**
	 * Gets the game server IP.
	 */
	@Getter private final InetAddress gameServerIP;

	/**
	 * Gets the game server port.
	 */
	@Getter private final int gameServerPort;

	/**
	 * Gets the query port.
	 */
	@Getter private final int queryPort;

	/**
	 * Gets the source steam ID.
	 */
	@Getter private final SteamID sourceSteamID;

	/**
	 * Gets the game data blob.
	 */
	@Getter private final byte[] gameDataBlob;

	/**
	 * Gets the name.
	 */
	@Getter private final String name;

	/**
	 * Gets the avatar hash.
	 */
	@Getter private final byte[] avatarHash;

	/**
	 * Gets the last log off.
	 */
	@Getter private final Date lastLogOff;

	/**
	 * Gets the last log on.
	 */
	@Getter private final Date lastLogOn;

	/**
	 * Gets the clan rank.
	 */
	@Getter private final int clanRank;

	/**
	 * Gets the clan tag.
	 */
	@Getter private final String clanTag;

	/**
	 * Gets the online session instances.
	 */
	@Getter private final int onlineSessionInstances;

	/**
	 * Gets the published session ID.
	 */
	@Getter private final int publishedSessionID;

	public PersonaStateCallback(CMsgClientPersonaState.Friend friend) {
		statusFlags = EClientPersonaStateFlag.f(friend.getPersonaStateFlags());

		friendID = new SteamID(friend.getFriendid());
		state = EPersonaState.f(friend.getPersonaState());

		gameAppID = friend.getGamePlayedAppId();
		gameID = new GameID(friend.getGameid());
		gameName = friend.getGameName();

		gameServerIP = NetHelpers.getIPAddress(friend.getGameServerIp());
		gameServerPort = friend.getGameServerPort();
		queryPort = friend.getQueryPort();

		sourceSteamID = new SteamID(friend.getSteamidSource());

		gameDataBlob = friend.getGameDataBlob().toByteArray();

		name = friend.getPlayerName();

		avatarHash = friend.getAvatarHash().toByteArray();

		lastLogOff = new Date(friend.getLastLogoff());
		lastLogOn = new Date(friend.getLastLogon());

		clanRank = friend.getClanRank();
		clanTag = friend.getClanTag();

		onlineSessionInstances = friend.getOnlineSessionInstances();
		publishedSessionID = friend.getPublishedInstanceId();
	}
}
