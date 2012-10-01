package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import lombok.Getter;

import uk.co.thomasc.steamkit.types.steamid.SteamID;

public class AccountCache {
	@Getter private final User LocalUser;

	@Getter private final AccountList<User> Users;
	@Getter private final AccountList<Clan> Clans;

	public AccountCache() {
		LocalUser = new User();
		LocalUser.name = "[unassigned]";

		Users = new AccountList<User>(User.class);
		Clans = new AccountList<Clan>(Clan.class);
	}

	public User getUser(SteamID steamId) {
		if (isLocalUser(steamId)) {
			return LocalUser;
		} else {
			return Users.getAccount(steamId);
		}
	}

	public boolean isLocalUser(SteamID steamId) {
		return LocalUser.SteamID == steamId;
	}
}
