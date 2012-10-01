package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import lombok.Setter;


import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgHdr implements ISteamSerializableHeader {

	// Static size: 4
	@Setter public EMsg msg = EMsg.Invalid;
	// Static size: 8
	public long targetJobID = BinaryReader.LongMaxValue;
	// Static size: 8
	public long sourceJobID = BinaryReader.LongMaxValue;

	public MsgHdr() {
		
	}

	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(msg.v());
		stream.write(targetJobID);
		stream.write(sourceJobID);
	}

	public void deSerialize(BinaryReader stream) throws IOException {
		msg = EMsg.f(stream.readInt());
		targetJobID = stream.readLong();
		sourceJobID = stream.readLong();
	}
}