package uk.co.thomasc.steamkit.base.gc;

import java.io.IOException;

import uk.co.thomasc.steamkit.types.JobID;

/**
 * Represents a unified interface into client messages.
 */
public interface IClientGCMsg {
	/**
	 * True if this instance is protobuf backed; otherwise, false.
	 */
	boolean isProto();

	/**
	 * The message type.
	 */
	int getMsgType();

	/**
	 * The target job id.
	 */
	JobID getTargetJobID();

	void setTargetJobID(JobID JobID);

	/**
	 * The source job id.
	 */
	JobID getSourceJobID();

	void setSourceJobID(JobID JobID);

	/**
	 * serializes this client message instance to a byte array.
	 * @return Data representing a client message.
	 */
	byte[] serialize() throws IOException;

	/**
	 * Initializes this client message by deserializing the specified data.
	 * @param data The data representing a client message.
	 * @throws IOException 
	 */
	void deSerialize(byte[] data) throws IOException;
}
