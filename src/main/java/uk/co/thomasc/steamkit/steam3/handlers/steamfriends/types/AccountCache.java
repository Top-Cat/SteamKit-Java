package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import lombok.Getter;

import uk.co.thomasc.steamkit.types.steamid.SteamID;

public class AccountCache {
	@Getter private final User localUser;

	@Getter private final AccountList<User> users;
	@Getter private final AccountList<Clan> clans;

	public AccountCache() {
		localUser = new User();
		localUser.name = "[unassigned]";

		users = new AccountList<User>(User.class);
		clans = new AccountList<Clan>(Clan.class);
	}

	public User getUser(SteamID steamId) {
		if (isLocalUser(steamId)) {
			return localUser;
		} else {
			return users.getAccount(steamId);
		}
	}

	public boolean isLocalUser(SteamID steamId) {
		return localUser.steamId == steamId;
	}
}
