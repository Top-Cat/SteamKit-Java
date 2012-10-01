package uk.co.thomasc.steamkit.base;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * Represents a unified interface into client messages.
 */
public interface IClientMsg {
	/**
	 * True if this instance is protobuf backed; otherwise, false.
	 */
	boolean isProto();

	/**
	 * The message type.
	 */
	EMsg getMsgType();

	/**
	 * The session id.
	 */
	int getSessionID();

	void setSessionID(int sessionID);

	/**
	 * The {@link SteamID}
	 */
	SteamID getSteamID();

	void setSteamID(SteamID SteamID);

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
	 * @throws IOException 
	 */
	byte[] serialize() throws IOException;

	/**
	 * Initializes this client message by deserializing the specified data.
	 * @param data The data representing a client message.
	 */
	void deSerialize(byte[] data) throws IOException;
}
