package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatEntryType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientChatMsg implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatMsg;
	}

	// Static size: 8
	private long steamIdChatter = 0;

	public SteamID getSteamIdChatter() {
		return new SteamID(steamIdChatter);
	}

	public void setSteamIdChatter(SteamID steamId) {
		steamIdChatter = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdChatRoom = 0;

	public SteamID getSteamIdChatRoom() {
		return new SteamID(steamIdChatRoom);
	}

	public void setSteamIdChatRoom(SteamID steamId) {
		steamIdChatRoom = steamId.convertToLong();
	}

	// Static size: 4
	public EChatEntryType chatMsgType = null;

	public MsgClientChatMsg() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChatter);
		stream.write(steamIdChatRoom);
		stream.write(chatMsgType.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChatter = stream.readLong();
		steamIdChatRoom = stream.readLong();
		chatMsgType = EChatEntryType.f(stream.readInt());
	}
}
