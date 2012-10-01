package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

public class AccountCache {
	@Getter private User LocalUser;

	@Getter private AccountList<User> Users;
	@Getter private AccountList<Clan> Clans;

	public AccountCache() {
		LocalUser = new User();
		LocalUser.name = "[unassigned]"; 

		Users = new AccountList<User>(User.class);
		Clans = new AccountList<Clan>(Clan.class);
	}


	public User getUser(SteamID steamId) {
		if (isLocalUser( steamId )) {
			return LocalUser;
		} else {
			return Users.getAccount(steamId);
		}
	}

	public boolean isLocalUser(SteamID steamId) {
		return LocalUser.SteamID == steamId;
	}
}