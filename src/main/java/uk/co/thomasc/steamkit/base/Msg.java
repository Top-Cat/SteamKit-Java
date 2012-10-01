package uk.co.thomasc.steamkit.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.MsgHdr;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public final class Msg<T extends ISteamSerializableMessage> extends MsgBase<MsgHdr> {
	/**
	 * Gets a value indicating whether this client message is protobuf backed.
	 */
	@Override
	public boolean isProto() {
		return false;
	}

	/**
	 * Gets the network message type of this client message.
	 */
	@Override
	public EMsg getMsgType() {
		return getHeader().msg;
	}

	/**
	 * Gets the session id for this client message.
	 */
	@Override
	public int getSessionID() {
		return sessionID;
	}

	/**
	 * Sets the session id for this client message.
	 */
	@Override
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Gets the {@link SteamID} for this client message.
	 */
	@Override
	public SteamID getSteamID() {
		return steamID;
	}

	/**
	 * Sets the {@link SteamID} for this client message.
	 */
	@Override
	public void setSteamID(SteamID steamID) {
		this.steamID = steamID;
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getTargetJobID() {
		return targetJobID;
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setTargetJobID(JobID jobID) {
		targetJobID = jobID;
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getSourceJobID() {
		return sourceJobID;
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setSourceJobID(JobID jobID) {
		sourceJobID = jobID;
	}

	/**
	 * The structure body of the message.
	 */
	@Getter private T body;

	public Msg(Class<T> clazz) {
		this(clazz, 0);
	}

	/**
	 * Initializes a new instance of the {@link Msg} class.
	 * This is a client send constructor.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public Msg(Class<T> clazz, int payloadReserve) {
		super(MsgHdr.class, payloadReserve);

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

	/**
	 * Initializes a new instance of the {@link Msg} class.
	 * This a reply constructor.
	 * @param msg				The message that this instance is a reply for.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public Msg(MsgBase<MsgHdr> msg, Class<T> clazz, int payloadReserve) {
		this(clazz, payloadReserve);
		// our target is where the message came from
		getHeader().targetJobID = msg.getHeader().sourceJobID;
	}

	/**
	 * Initializes a new instance of the {@link Msg} class.
	 * This is a recieve constructor.
	 * @param msg	The packet message to build this client message from.
	 */
	public Msg(IPacketMsg msg, Class<T> clazz) {
		this(clazz);
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
		final BinaryWriter ms = new BinaryWriter();

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
		final BinaryReader ms = new BinaryReader(data);
		getHeader().deSerialize(ms);
		body.deSerialize(ms);

		// the rest of the data is the payload
		final int payloadOffset = ms.getPosition();
		final int payloadLen = ms.getRemaining();

		setReader(new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen))));
	}

}
