package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import lombok.Setter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class ExtendedClientMsgHdr implements ISteamSerializableHeader {
	// Static size: 4
	@Setter public EMsg msg = EMsg.Invalid;
	// Static size: 1
	public byte headerSize = 36;
	// Static size: 2
	public short headerVersion = 2;
	// Static size: 8
	public long targetJobID = BinaryReader.LongMaxValue;
	// Static size: 8
	public long sourceJobID = BinaryReader.LongMaxValue;
	// Static size: 1
	public byte headerCanary = (byte) 239;
	// Static size: 8
	private long steamID = 0;

	public SteamID getSteamID() {
		return new SteamID(steamID);
	}

	public void setSteamID(SteamID steamID) {
		this.steamID = steamID.convertToLong();
	}

	// Static size: 4
	public int sessionID = 0;

	public ExtendedClientMsgHdr() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(msg.v());
		stream.write(headerSize);
		stream.write(headerVersion);
		stream.write(targetJobID);
		stream.write(sourceJobID);
		stream.write(headerCanary);
		stream.write(steamID);
		stream.write(sessionID);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		msg = EMsg.f(stream.readInt());
		headerSize = stream.readByte();
		headerVersion = stream.readShort();
		targetJobID = stream.readLong();
		sourceJobID = stream.readLong();
		headerCanary = stream.readByte();
		steamID = stream.readLong();
		sessionID = stream.readInt();
	}
}
