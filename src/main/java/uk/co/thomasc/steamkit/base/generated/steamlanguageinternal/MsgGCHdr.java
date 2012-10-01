package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgGCHdr implements IGCSerializableHeader {

	@Override
	public void setEMsg(int msg) {

	}

	// Static size: 2
	public short headerVersion = 1;
	// Static size: 8
	public long targetJobID = BinaryReader.LongMaxValue;
	// Static size: 8
	public long sourceJobID = BinaryReader.LongMaxValue;

	public MsgGCHdr() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(headerVersion);
		stream.write(targetJobID);
		stream.write(sourceJobID);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		headerVersion = stream.readShort();
		targetJobID = stream.readLong();
		sourceJobID = stream.readLong();
	}
}
