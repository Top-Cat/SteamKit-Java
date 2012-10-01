package uk.co.thomasc.steamkit.base;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import uk.co.thomasc.steamkit.base.gc.IPacketGCMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.MsgGCHdrProtoBuf;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a protobuf backed packet message.
 */
public final class PacketClientGCMsgProtobuf implements IPacketGCMsg {
	/**
	 * Gets a value indicating whether this packet message is protobuf backed.
	 * This type of message is always protobuf backed.
	 */
	@Override
	public boolean isProto() {
		return true;
	}

	/**
	 * Gets the network message type of this packet message.
	 */
	@Getter private final int msgType;

	/**
	 * Gets the target job id for this packet message.
	 */
	@Getter @Setter private JobID targetJobID;

	/**
	 * Gets the source job id for this packet message.
	 */
	@Getter @Setter private JobID sourceJobID;

	byte[] payload;

	/**
	 * Initializes a new instance of the {@link PacketClientGCMsgProtobuf} class.
	 * @param eMsg	The network message type for this packet message.
	 * @param data	The data.
	 */
	public PacketClientGCMsgProtobuf(int eMsg, byte[] data) {
		msgType = eMsg;
		payload = data;

		final MsgGCHdrProtoBuf protobufHeader = new MsgGCHdrProtoBuf();

		// we need to pull out the job ids, so we deserialize the protobuf header
		final BinaryReader is = new BinaryReader(data);

		try {
			protobufHeader.deSerialize(is);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		targetJobID = new JobID(protobufHeader.proto.getJobidTarget());
		sourceJobID = new JobID(protobufHeader.proto.getJobidSource());
	}

	/**
	 * Gets the underlying data that represents this client message.
	 * @return The data.
	 */
	@Override
	public byte[] getData() {
		return payload;
	}
}
