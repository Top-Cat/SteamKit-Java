package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientJoinChat implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientJoinChat;
	}

	// Static size: 8
	private long steamIdChat = 0;

	public SteamID getSteamIdChat() {
		return new SteamID(steamIdChat);
	}

	public void setSteamIdChat(SteamID steamId) {
		steamIdChat = steamId.convertToLong();
	}

	// Static size: 1
	private byte isVoiceSpeaker = 0;

	public boolean isVoiceSpeaker() {
		return isVoiceSpeaker == 1;
	}

	public void setIsVoiceSpeaker(boolean value) {
		isVoiceSpeaker = (byte) (value ? 1 : 0);
	}

	public MsgClientJoinChat() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(isVoiceSpeaker);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		isVoiceSpeaker = stream.readByte();
	}
}
