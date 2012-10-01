package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import uk.co.thomasc.steamkit.types.steamid.SteamID;

abstract class Account {
	public SteamID steamId = new SteamID();

	public String name = "[unknown]";
	public byte[] avatarHash;
}
