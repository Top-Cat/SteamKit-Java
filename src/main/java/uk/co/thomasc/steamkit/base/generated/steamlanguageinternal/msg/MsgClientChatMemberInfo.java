package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatInfoType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientChatMemberInfo implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatMemberInfo;
	}

	// Static size: 8
	private long steamIdChat = 0;

	public SteamID getSteamIdChat() {
		return new SteamID(steamIdChat);
	}

	public void setSteamIdChat(SteamID steamId) {
		steamIdChat = steamId.convertToLong();
	}

	// Static size: 4
	public EChatInfoType type = null;

	public MsgClientChatMemberInfo() {
	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(type.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		type = EChatInfoType.f(stream.readInt());
	}
}
