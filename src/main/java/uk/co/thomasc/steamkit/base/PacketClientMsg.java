package uk.co.thomasc.steamkit.base;

import java.io.IOException;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ExtendedClientMsgHdr;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a packet message with extended header information.
 */
public final class PacketClientMsg implements IPacketMsg {
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
	@Getter private final EMsg msgType;

	/**
	 * Gets the target job id for this packet message.
	 */
	@Getter private long targetJobID;

	/**
	 * Gets the source job id for this packet message.
	 */
	@Getter private long sourceJobID;

	byte[] payload;

	/**
	 * Initializes a new instance of the {@link PacketClientMsg} class.
	 * @param eMsg	The network message type for this packet message.
	 * @param data	The data.
	 */
	public PacketClientMsg(EMsg eMsg, byte[] data) {
		msgType = eMsg;
		payload = data;

		final ExtendedClientMsgHdr extendedHdr = new ExtendedClientMsgHdr();

		final BinaryReader is = new BinaryReader(data);
		try {
			extendedHdr.deSerialize(is);

			targetJobID = extendedHdr.targetJobID;
			sourceJobID = extendedHdr.sourceJobID;
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the underlying data that represents this client message.
	 */
	@Override
	public byte[] getData() {
		return payload;
	}
}
