package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;


import uk.co.thomasc.steamkit.base.generated.steamlanguage.EFriendRelationship;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPersonaState;
import uk.co.thomasc.steamkit.types.gameid.GameID;

public final class User extends Account {
	public EFriendRelationship relationship;

	public EPersonaState personaState;

	public int gameAppID;
	public GameID gameID = new GameID();
	public String gameName;
}