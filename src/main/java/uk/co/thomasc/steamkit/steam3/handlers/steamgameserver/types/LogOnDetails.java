package uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.types;

/**
 * Represents the details required to log into Steam3 as a game server.
 */
public final class LogOnDetails {
	/**
	 * Gets or sets the username.
	 */
	public String username;

	/**
	 * Gets or sets the password.
	 */
	public String password;

	/**
	 * Gets or sets the AppID this gameserver will serve.
	 */
	public int appId;
}
