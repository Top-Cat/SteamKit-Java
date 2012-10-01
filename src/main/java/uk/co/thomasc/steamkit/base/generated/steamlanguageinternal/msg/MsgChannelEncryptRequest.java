package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUniverse;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgChannelEncryptRequest implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ChannelEncryptRequest;
	}

	public static final int PROTOCOL_VERSION = 1;
	// Static size: 4
	public int protocolVersion = MsgChannelEncryptRequest.PROTOCOL_VERSION;
	// Static size: 4
	public EUniverse universe = EUniverse.Invalid;

	public MsgChannelEncryptRequest() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(protocolVersion);
		stream.write(universe.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		protocolVersion = stream.readInt();
		universe = EUniverse.f(stream.readInt());
	}
}
