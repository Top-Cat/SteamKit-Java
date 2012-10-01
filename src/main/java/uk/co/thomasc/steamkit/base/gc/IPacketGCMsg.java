package uk.co.thomasc.steamkit.base.gc;

import uk.co.thomasc.steamkit.types.JobID;

/**
 * Represents a simple unified interface into game coordinator messages recieved from the network.
 * This is contrasted with {@link IClientGCMsg} in that this interface is packet body agnostic
 * and only allows simple access into the header. This interface is also immutable, and the underlying
 * data cannot be modified.
 */
public interface IPacketGCMsg {
	/**
	 * true if this instance is protobuf backed; otherwise, false.
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
	 * Gets the underlying data that represents this client message.
	 * @return The data.
	 */
	byte[] getData();
}
