package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EFriendRelationship;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPersonaState;
import uk.co.thomasc.steamkit.types.gameid.GameID;

public final class User extends Account {
	public EFriendRelationship relationship = EFriendRelationship.None;

	public EPersonaState personaState = EPersonaState.Offline;

	public int gameAppId;
	public GameID gameId = new GameID();
	public String gameName;
}
