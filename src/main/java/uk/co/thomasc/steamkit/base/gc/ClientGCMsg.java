package uk.co.thomasc.steamkit.base.gc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.IGCSerializableMessage;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.MsgGCHdr;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.logging.Debug;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

/**
 * Represents a struct backed game coordinator message.
 * @param <T>	The body type of this message.
 */
public final class ClientGCMsg<T extends IGCSerializableMessage> extends GCMsgBase<MsgGCHdr> {
	/**
	 * Gets a value indicating whether this gc message is protobuf backed.
	 * @return true if this instance is protobuf backed; otherwise, false. 
	 */
	@Override
	public boolean isProto() {
		return false;
	}

	int msgType;

	/**
	 * Gets the network message type of this gc message.
	 * @return The network message type.
	 */
	@Override
	public int getMsgType() {
		return msgType;
	}

	/**
	 * Gets the target job id for this gc message.
	 * @return The target job id
	 */
	@Override
	public JobID getTargetJobID() {
		return new JobID(getHeader().targetJobID);
	}

	/**
	 * Sets the target job id for this gc message.
	 * @param value The target job id
	 */
	@Override
	public void setTargetJobID(JobID value) {
		getHeader().targetJobID = value.getValue();
	}

	/**
	 * Gets the source job id for this gc message.
	 * @return The source job id
	 */
	@Override
	public JobID getSourceJobID() {
		return new JobID(getHeader().sourceJobID);
	}

	/**
	 * Sets the source job id for this gc message.
	 * @param value The source job id
	 */
	@Override
	public void setSourceJobID(JobID value) {
		getHeader().sourceJobID = value.getValue();
	}

	/**
	 * Gets the body structure of this message.
	 */
	@Getter private T body;

	/**
	 * Initializes a new instance of the {@link ClientGCMsg} class.
	 * This is a client send constructor.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public ClientGCMsg(Class<T> clazz, int payloadReserve) {
		super(MsgGCHdr.class, payloadReserve);

		try {
			body = clazz.newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		// assign our emsg
		msgType = body.getEMsg();
	}

	public ClientGCMsg(Class<T> clazz) {
		this(clazz, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientGCMsg} class.
	 * This a reply constructor.
	 * @param msg				The message that this instance is a reply for.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public ClientGCMsg(Class<T> clazz, GCMsgBase<MsgGCHdr> msg, int payloadReserve) {
		this(clazz, payloadReserve);
		// our target is where the message came from
		getHeader().targetJobID = msg.getHeader().sourceJobID;
	}

	public ClientGCMsg(Class<T> clazz, GCMsgBase<MsgGCHdr> msg) {
		this(clazz, msg, 64);
	}

	/**
	 * Initializes a new instance of the {@link ClientGCMsg} class.
	 * This is a recieve constructor.
	 * @param msg	The packet message to build this gc message from.
	 */
	public ClientGCMsg(Class<T> clazz, IPacketGCMsg msg) {
		this(clazz);
		Debug.Assert(!msg.isProto(), "ClientGCMsg used for proto message!");

		try {
			deSerialize(msg.getData());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Serializes this gc message instance to a byte array.
	 * @return Data representing a client message.
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
	 * Initializes this gc message by deserializing the specified data.
	 * @param data	The data representing a client message.
	 */
	@Override
	public void deSerialize(byte[] data) throws IOException {
		final BinaryReader cs = new BinaryReader(data);
		getHeader().deSerialize(cs);
		body.deSerialize(cs);

		// the rest of the data is the payload
		final int payloadOffset = cs.getPosition();
		final int payloadLen = cs.getRemaining();

		setReader(new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen))));
	}
}
