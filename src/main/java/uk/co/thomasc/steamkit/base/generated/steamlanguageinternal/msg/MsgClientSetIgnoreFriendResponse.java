package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientSetIgnoreFriendResponse implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientSetIgnoreFriendResponse;
	}

	// Static size: 8
	public long unknown = 0;
	// Static size: 4
	public EResult result = EResult.Invalid;

	public MsgClientSetIgnoreFriendResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(unknown);
		stream.write(result.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		unknown = stream.readLong();
		result = EResult.f(stream.readInt());
	}
}
