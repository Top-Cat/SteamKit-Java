package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientVACBanStatus implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientVACBanStatus;
	}

	// Static size: 4
	public int numBans = 0;

	public MsgClientVACBanStatus() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(numBans);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		numBans = stream.readInt();
	}
}
