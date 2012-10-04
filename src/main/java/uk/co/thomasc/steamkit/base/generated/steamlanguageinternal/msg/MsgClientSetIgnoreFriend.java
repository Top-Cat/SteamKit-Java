package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientSetIgnoreFriend implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientSetIgnoreFriend;
	}

	// Static size: 8
	private long mySteamId = 0;

	public SteamID getMySteamId() {
		return new SteamID(mySteamId);
	}

	public void setMySteamId(SteamID steamId) {
		mySteamId = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdFriend = 0;

	public SteamID getSteamIdFriend() {
		return new SteamID(steamIdFriend);
	}

	public void setSteamIdFriend(SteamID steamId) {
		steamIdFriend = steamId.convertToLong();
	}

	// Static size: 1
	public byte ignore = 0;

	public MsgClientSetIgnoreFriend() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(mySteamId);
		stream.write(steamIdFriend);
		stream.write(ignore);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		mySteamId = stream.readLong();
		steamIdFriend = stream.readLong();
		ignore = stream.readByte();
	}
}
