package uk.co.thomasc.steamkit.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ExtendedClientMsgHdr;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.logging.Debug;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

/**
 * Represents a struct backed client message.
 * @param <T> The body type of this message.
 */
public final class ClientMsg<T extends ISteamSerializableMessage> extends MsgBase<ExtendedClientMsgHdr> {
	/**
	 * Gets a value indicating whether this client message is protobuf backed.
	 */
	@Override
	public boolean isProto() {
		return false;
	}

	@Override
	public EMsg getMsgType() {
		return getHeader().msg;
	}

	/**
	 * Gets the session id for this client message.
	 */
	@Override
	public int getSessionID() {
		return getHeader().sessionID;
	}

	/**
	 * Sets the session id for this client message.
	 */
	@Override
	public void setSessionID(int sessionID) {
		getHeader().sessionID = sessionID;
	}

	/**
	 * Gets the {@link SteamID} for this client message.
	 */
	@Override
	public SteamID getSteamID() {
		return getHeader().getSteamID();
	}

	/**
	 * Sets the {@link SteamID} for this client message.
	 */
	@Override
	public void setSteamID(SteamID steamID) {
		getHeader().setSteamID(steamID);
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getTargetJobID() {
		return new JobID(getHeader().targetJobID);
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setTargetJobID(JobID jobID) {
		getHeader().targetJobID = jobID.getValue();
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getSourceJobID() {
		return new JobID(getHeader().sourceJobID);
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setSourceJobID(JobID jobID) {
		getHeader().sourceJobID = jobID.getValue();
	}

	/**
	 * Gets the body structure of this message.
	 */
	@Getter private T body;

	public ClientMsg(Class<T> clazz) {
		this(clazz, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientMsg} class.
	 * This is a client send constructor.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public ClientMsg(Class<T> clazz, int payloadReserve) {
		super(ExtendedClientMsgHdr.class, payloadReserve);

		try {
			body = clazz.newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		// assign our emsg
		getHeader().msg = body.getEMsg();
	}

	public ClientMsg(Class<T> clazz, MsgBase<ExtendedClientMsgHdr> msg) {
		this(clazz, msg, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientMsg} class.
	 * This a reply constructor.
	 * @param msg				The message that this instance is a reply for.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public ClientMsg(Class<T> clazz, MsgBase<ExtendedClientMsgHdr> msg, int payloadReserve) {
		this(clazz, payloadReserve);
		// our target is where the message came from
		getHeader().targetJobID = msg.getHeader().sourceJobID;
	}

	/**
	 * Initializes a new instance of the {@link ClientMsg} class.
	 * This is a recieve constructor.
	 * @param msg	The packet message to build this client message from.
	 */
	public ClientMsg(IPacketMsg msg, Class<T> clazz) {
		this(clazz);
		Debug.Assert(!msg.isProto(), "ClientMsg used for proto message!");

		try {
			deSerialize(msg.getData());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * serializes this client message instance to a byte array.
	 * @throws IOException 
	 */
	@Override
	public byte[] serialize() throws IOException {
		final BinaryWriter ms = new BinaryWriter(new ByteArrayOutputStream());

		getHeader().serialize(ms);
		body.serialize(ms);
		ms.write(getOutputStream().toByteArray());

		return ms.toByteArray();
	}

	/**
	 * Initializes this client message by deserializing the specified data.
	 * @throws IOException 
	 */
	@Override
	public void deSerialize(byte[] data) throws IOException {
		final BinaryReader cs = new BinaryReader(data);
		getHeader().deSerialize(cs);
		body.deSerialize(cs);

		// the rest of the data is the payload
		final int payloadOffset = cs.getPosition();
		final int payloadLen = cs.getRemaining();
		
		setReader(new BinaryReader(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen)));
	}
}
