package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUdpPacketType;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class UdpHeader implements ISteamSerializable {
	public static final int MAGIC = 0x31305356;
	// Static size: 4
	public int magic = UdpHeader.MAGIC;
	// Static size: 2
	public short payloadSize = 0;
	// Static size: 1
	public EUdpPacketType packetType = EUdpPacketType.Invalid;
	// Static size: 1
	public byte flags = 0;
	// Static size: 4
	public int sourceConnID = 512;
	// Static size: 4
	public int destConnID = 0;
	// Static size: 4
	public int seqThis = 0;
	// Static size: 4
	public int seqAck = 0;
	// Static size: 4
	public int packetsInMsg = 0;
	// Static size: 4
	public int msgStartSeq = 0;
	// Static size: 4
	public int msgSize = 0;

	public UdpHeader() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(magic);
		stream.write(payloadSize);
		stream.write(packetType.v());
		stream.write(flags);
		stream.write(sourceConnID);
		stream.write(destConnID);
		stream.write(seqThis);
		stream.write(seqAck);
		stream.write(packetsInMsg);
		stream.write(msgStartSeq);
		stream.write(msgSize);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		magic = stream.readInt();
		payloadSize = stream.readShort();
		packetType = EUdpPacketType.fromCode(stream.readByte());
		flags = stream.readByte();
		sourceConnID = stream.readInt();
		destConnID = stream.readInt();
		seqThis = stream.readInt();
		seqAck = stream.readInt();
		packetsInMsg = stream.readInt();
		msgStartSeq = stream.readInt();
		msgSize = stream.readInt();
	}
}
