package uk.co.thomasc.steamkit.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.Getter;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.GeneratedMessage;

import uk.co.thomasc.steamkit.base.generated.SteammessagesBase.CMsgProtoBufHeader.Builder;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.MsgHdrProtoBuf;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.logging.Debug;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

/**
 * Represents a protobuf backed client message.
 * @param <U> The builder for T
 */
public final class ClientMsgProtobuf<U extends GeneratedMessage.Builder<U>> extends MsgBase<MsgHdrProtoBuf> {

	/**
	 * Client messages of this type are always protobuf backed.
	 */
	@Override
	public boolean isProto() {
		return true;
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
		return getProtoHeader().getClientSessionid();
	}

	/**
	 * Sets the session id for this client message.
	 */
	@Override
	public void setSessionID(int sessionID) {
		getProtoHeader().setClientSessionid(sessionID);
	}

	/**
	 * Gets the {@link SteamID} for this client message.
	 */
	@Override
	public SteamID getSteamID() {
		return new SteamID(getProtoHeader().getSteamid());
	}

	/**
	 * Sets the {@link SteamID} for this client message.
	 */
	@Override
	public void setSteamID(SteamID steamID) {
		getProtoHeader().setSteamid(steamID.convertToLong());
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getTargetJobID() {
		return new JobID(getProtoHeader().getJobidTarget());
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setTargetJobID(JobID JobID) {
		getProtoHeader().setJobidTarget(JobID.getValue());
	}

	/**
	 * Gets or sets the target job id for this client message.
	 */
	@Override
	public JobID getSourceJobID() {
		return new JobID(getProtoHeader().getJobidSource());
	}

	/**
	 * Sets the target job id for this client message.
	 */
	@Override
	public void setSourceJobID(JobID JobID) {
		getProtoHeader().setJobidSource(JobID.getValue());
	}

	/**
	 * Shorthand accessor for the protobuf header.
	 */
	public Builder getProtoHeader() {
		return getHeader().proto;
	}

	/**
	 * Gets the body structure of this message.
	 */
	@Getter private U body;

	private Class<? extends AbstractMessage> clazz;

	public ClientMsgProtobuf(Class<? extends AbstractMessage> clazz, EMsg eMsg) {
		this(clazz, eMsg, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientMsgProtobuf} class.
	 * This is a client send constructor.
	 * @param eMsg				The network message type this client message represents.
	 * @param clazz				The class of T
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	@SuppressWarnings("unchecked")
	public ClientMsgProtobuf(Class<? extends AbstractMessage> clazz, EMsg eMsg, int payloadReserve) {
		super(MsgHdrProtoBuf.class, payloadReserve);

		this.clazz = clazz;

		try {
			final Method m = clazz.getMethod("newBuilder");
			body = (U) m.invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// set our emsg
		getHeader().msg = eMsg;
	}

	public ClientMsgProtobuf(Class<? extends AbstractMessage> clazz, EMsg eMsg, MsgBase<MsgHdrProtoBuf> msg) {
		this(clazz, eMsg, msg, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientMsgProtobuf} class.
	 * This a reply constructor.
	 * @param eMsg				The network message type this client message represents.
	 * @param msg				The message that this instance is a reply for.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public ClientMsgProtobuf(Class<? extends AbstractMessage> clazz, EMsg eMsg, MsgBase<MsgHdrProtoBuf> msg, int payloadReserve) {
		this(clazz, eMsg, payloadReserve);
		// our target is where the message came from
		getHeader().proto.setJobidTarget(msg.getHeader().proto.getJobidSource());
	}

	/**
	 * Initializes a new instance of the {@link ClientMsgProtobuf} class.
	 * This is a recieve constructor.
	 * @param msg	The packet message to build this client message from.
	 */
	public ClientMsgProtobuf(Class<? extends AbstractMessage> clazz, IPacketMsg msg) {
		this(clazz, msg.getMsgType());

		Debug.Assert(msg.isProto(), "ClientMsgProtobuf used for non-proto message!");

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
		ms.write(body.build().toByteArray());
		ms.write(getOutputStream().toByteArray());

		return ms.toByteArray();
	}

	/**
	 * Initializes this client message by deserializing the specified data.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deSerialize(byte[] data) throws IOException {
		final BinaryReader is = new BinaryReader(data);
		getHeader().deSerialize(is);
		try {
			final Method m = clazz.getMethod("newBuilder");
			body = (U) m.invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		body.mergeFrom(is.getStream());

		// the rest of the data is the payload
		final int payloadOffset = is.getPosition();
		final int payloadLen = is.getRemaining();

		setReader(new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen))));
	}
}
