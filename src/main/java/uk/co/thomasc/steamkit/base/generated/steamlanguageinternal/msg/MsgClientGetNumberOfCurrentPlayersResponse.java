package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientGetNumberOfCurrentPlayersResponse implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientGetNumberOfCurrentPlayersResponse;
	}

	// Static size: 4
	public EResult result = EResult.Invalid;
	// Static size: 4
	public int numPlayers = 0;

	public MsgClientGetNumberOfCurrentPlayersResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(result.v());
		stream.write(numPlayers);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		result = EResult.f(stream.readInt());
		numPlayers = stream.readInt();
	}
}
