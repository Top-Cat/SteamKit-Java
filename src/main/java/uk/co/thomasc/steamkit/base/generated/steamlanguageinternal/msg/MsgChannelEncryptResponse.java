package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgChannelEncryptResponse implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ChannelEncryptResponse;
	}

	// Static size: 4
	public int protocolVersion = MsgChannelEncryptRequest.PROTOCOL_VERSION;
	// Static size: 4
	public int keySize = 128;

	public MsgChannelEncryptResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(protocolVersion);
		stream.write(keySize);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		protocolVersion = stream.readInt();
		keySize = stream.readInt();
	}
}
