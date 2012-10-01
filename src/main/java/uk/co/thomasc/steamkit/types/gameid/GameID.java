package uk.co.thomasc.steamkit.types.gameid;

import uk.co.thomasc.steamkit.types.steamid.BitVector64;

/**
 * This 64bit structure represents an app, mod, shortcut, or p2p file on the Steam network.
 */
public class GameID {

	BitVector64 gameId;

	/**
	 * Initializes a new instance of the {@link GameID} class.
	 */
	public GameID() {
		this(0);
	}

	/**
	 * Initializes a new instance of the {@link GameID} class.
	 * @param id	The 64bit integer to assign this GameID from.
	 */
	public GameID(long id) {
		gameId = new BitVector64(id);
	}

	/**
	 * Initializes a new instance of the {@link GameID} class.
	 * @param nAppID	The 32bit app id to assign this GameID from.
	 */
	public GameID(int nAppID) {
		this((long) nAppID);
	}

	/**
	 * Sets the various components of this GameID from a 64bit integer form.
	 * @param gameId	The 64bit integer to assign this GameID from.
	 */
	public void set(long gameId) {
		this.gameId.setData(gameId);
	}

	/**
	 * Converts this GameID into it's 64bit integer form.
	 * @return	A 64bit integer representing this GameID.
	 */
	public long toLong() {
		return gameId.getData();
	}

	/**
	 * Performs an implicit conversion from {@link GameID} to {@link String}.
	 */
	@Override
	public String toString() {
		return gameId.getData().toString();
	}

	/**
	 * Gets the app id.
	 * @return The app IDid
	 */
	public long getAppID() {
		return gameId.getMask((short) 0, 0xFFFFFF);
	}

	/**
	 * Gets the app id.
	 * @param appID	The app IDid
	 */
	public void setAppID(long appID) {
		gameId.setMask((short) 0, 0xFFFFFF, appID);
	}

	/**
	 * Gets the type of the app.
	 * @return The type of the app.
	 */
	public GameType getAppType() {
		return GameType.fromCode((int) gameId.getMask((short) 24, 0xFF));
	}

	/**
	 * Sets the type of the app.
	 * @param appType	The type of the app.
	 */
	public void setAppType(GameType appType) {
		gameId.setMask((short) 24, 0xFF, appType.v());
	}

	/**
	 * Gets the mod id.
	 * @return The mod ID.
	 */
	public long getModID() {
		return gameId.getMask((short) 32, 0xFFFFFFFF);
	}

	/**
	 * Sets the mod id.
	 * @param modID	The mod ID.
	 */
	public void setModID(long modID) {
		gameId.setMask((short) 32, 0xFFFFFFFF, modID);
	}

	/**
	 * Gets a value indicating whether this instance is a mod.
	 * @return true if this instance is a mod; otherwise, false
	 */
	public boolean isMod() {
		return getAppType() == GameType.GameMod;
	}

	/**
	 * Gets a value indicating whether this instance is a shortcut.
	 * @return true if this instance is a shortcut; otherwise, false
	 */
	public boolean isShortcut() {
		return getAppType() == GameType.Shortcut;
	}

	/**
	 * Gets a value indicating whether this instance is a peer-to-peer file.
	 * @return true if this instance is a p2p file; otherwise, false
	 */
	public boolean IsP2PFile() {
		return getAppType() == GameType.P2P;
	}

	/**
	 * Gets a value indicating whether this instance is a steam app.
	 * @return true if this instance is a steam app; otherwise, false
	 */
	public boolean IsSteamApp() {
		return getAppType() == GameType.App;
	}

	/**
	 * Determines whether the specified {@link Object} is equal to this instance.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof GameID)) {
			return false;
		}

		return gameId.getData().equals(((GameID) obj).gameId.getData());
	}

	/**
	 * Returns a hash code for this instance.
	 */
	@Override
	public int hashCode() {
		return gameId.hashCode();
	}
}
