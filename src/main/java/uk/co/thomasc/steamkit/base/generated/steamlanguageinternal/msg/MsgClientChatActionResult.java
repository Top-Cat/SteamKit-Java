package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatAction;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatActionResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientChatActionResult implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatActionResult;
	}

	// Static size: 8
	private long steamIdChat = 0;

	public SteamID getSteamIdChat() {
		return new SteamID(steamIdChat);
	}

	public void setSteamIdChat(SteamID steamId) {
		steamIdChat = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdUserActedOn = 0;

	public SteamID getSteamIdUserActedOn() {
		return new SteamID(steamIdUserActedOn);
	}

	public void setSteamIdUserActedOn(SteamID steamId) {
		steamIdUserActedOn = steamId.convertToLong();
	}

	// Static size: 4
	public EChatAction chatAction = null;
	// Static size: 4
	public EChatActionResult actionResult = null;

	public MsgClientChatActionResult() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(steamIdUserActedOn);
		stream.write(chatAction.v());
		stream.write(actionResult.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		steamIdUserActedOn = stream.readLong();
		chatAction = EChatAction.f(stream.readInt());
		actionResult = EChatActionResult.f(stream.readInt());
	}
}
