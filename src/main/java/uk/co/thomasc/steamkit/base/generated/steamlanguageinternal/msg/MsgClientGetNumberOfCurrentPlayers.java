package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.gameid.GameID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientGetNumberOfCurrentPlayers implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientGetNumberOfCurrentPlayers;
	}

	// Static size: 8
	private long gameID = 0;

	public GameID getGameId() {
		return new GameID(gameID);
	}

	public void setGameId(GameID GameID) {
		gameID = GameID.toLong();
	}

	public MsgClientGetNumberOfCurrentPlayers() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(gameID);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		gameID = stream.readLong();
	}
}
