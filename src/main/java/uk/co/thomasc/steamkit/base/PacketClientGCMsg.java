package uk.co.thomasc.steamkit.base;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import uk.co.thomasc.steamkit.base.gc.IPacketGCMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.MsgGCHdr;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a packet message with extended header information.
 */
public final class PacketClientGCMsg implements IPacketGCMsg {
	/**
	 * Gets a value indicating whether this packet message is protobuf backed.
	 * This type of message is never protobuf backed.
	 */
	@Override
	public boolean isProto() {
		return false;
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
	 * Initializes a new instance of the {@link PacketClientGCMsg} class.
	 * @param eMsg	The network message type for this packet message.
	 * @param data	The data.
	 */
	public PacketClientGCMsg(int eMsg, byte[] data) {
		msgType = eMsg;
		payload = data;

		final MsgGCHdr gcHdr = new MsgGCHdr();

		// deserialize the gc header to get our hands on the job ids
		final BinaryReader is = new BinaryReader(data);
		try {
			gcHdr.deSerialize(is);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		targetJobID = new JobID(gcHdr.targetJobID);
		sourceJobID = new JobID(gcHdr.sourceJobID);
	}

	/**
	 * Gets the underlying data that represents this packet message.
	 * @return The data.
	 */
	@Override
	public byte[] getData() {
		return payload;
	}
}
