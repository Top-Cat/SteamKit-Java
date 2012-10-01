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
	@Getter private EClientPersonaStateFlag statusFlags;

	/**
	 * Gets the friend ID.
	 */
	@Getter private SteamID friendID;

	/**
	 * Gets the state.
	 */
	@Getter private EPersonaState state;

	/**
	 * Gets the game app ID.
	 */
	@Getter private int gameAppID;

	/**
	 * Gets the game ID.
	 */
	@Getter private GameID gameID;

	/**
	 * Gets the name of the game.
	 */
	@Getter private String gameName;

	/**
	 * Gets the game server IP.
	 */
	@Getter private InetAddress gameServerIP;

	/**
	 * Gets the game server port.
	 */
	@Getter private int gameServerPort;

	/**
	 * Gets the query port.
	 */
	@Getter private int queryPort;

	/**
	 * Gets the source steam ID.
	 */
	@Getter private SteamID sourceSteamID;

	/**
	 * Gets the game data blob.
	 */
	@Getter private byte[] gameDataBlob;

	/**
	 * Gets the name.
	 */
	@Getter private String name;

	/**
	 * Gets the avatar hash.
	 */
	@Getter private byte[] avatarHash;

	/**
	 * Gets the last log off.
	 */
	@Getter private Date lastLogOff;

	/**
	 * Gets the last log on.
	 */
	@Getter private Date lastLogOn;

	/**
	 * Gets the clan rank.
	 */
	@Getter private int clanRank;

	/**
	 * Gets the clan tag.
	 */
	@Getter private String clanTag;

	/**
	 * Gets the online session instances.
	 */
	@Getter private int onlineSessionInstances;

	/**
	 * Gets the published session ID.
	 */
	@Getter private int publishedSessionID;

	public PersonaStateCallback(CMsgClientPersonaState.Friend friend) {
		this.statusFlags = EClientPersonaStateFlag.f(friend.getPersonaStateFlags());

		this.friendID = new SteamID(friend.getFriendid());
		this.state = EPersonaState.f(friend.getPersonaState());

		this.gameAppID = friend.getGamePlayedAppId();
		this.gameID = new GameID(friend.getGameid());
		this.gameName = friend.getGameName();

		this.gameServerIP = NetHelpers.getIPAddress(friend.getGameServerIp());
		this.gameServerPort = friend.getGameServerPort();
		this.queryPort = friend.getQueryPort();

		this.sourceSteamID = new SteamID(friend.getSteamidSource());

		this.gameDataBlob = friend.getGameDataBlob().toByteArray();

		this.name = friend.getPlayerName();

		this.avatarHash = friend.getAvatarHash().toByteArray();

		this.lastLogOff = new Date(friend.getLastLogoff());
		this.lastLogOn = new Date(friend.getLastLogon());

		this.clanRank = friend.getClanRank();
		this.clanTag = friend.getClanTag();

		this.onlineSessionInstances = friend.getOnlineSessionInstances();
		this.publishedSessionID = friend.getPublishedInstanceId();
	}
}