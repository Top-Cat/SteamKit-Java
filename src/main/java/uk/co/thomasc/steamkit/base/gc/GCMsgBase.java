package uk.co.thomasc.steamkit.base.gc;

import lombok.Getter;


import uk.co.thomasc.steamkit.base.AMsgBase;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.IGCSerializableHeader;
import uk.co.thomasc.steamkit.types.JobID;

/**
 * This is the abstract base class for all available game coordinator messages.
 * It's used to maintain packet payloads and provide a header for all gc messages.
 * @param <T>	The header type for this gc message.
 */
public abstract class GCMsgBase<T extends IGCSerializableHeader> extends AMsgBase implements IClientGCMsg {

	/**
	 * true if this instance is protobuf backed; otherwise, false
	 */
	@Getter private boolean IsProto;
	
	/**
	 * The network message type.
	 */
	@Getter private int MsgType;

	/**
	 * The target job id.
	 */
	public JobID TargetJobID;
	
	/**
	 * The source job id.
	 */
	public JobID SourceJobID;

	/**
	 * Gets the header for this message type. 
	 */
	@Getter private T Header;

	/**
	 * Initializes a new instance of the {@link GCMsgBase} class.
	 * @param payloadReserve	The number of bytes to initialize the payload capacity to.
	 */
	public GCMsgBase(int payloadReserve, Class<T> aClass) {
		super(payloadReserve);
		try {
			Header = aClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * serializes this client message instance to a byte array.
	 * @return Data representing a client message.
	 */
	public abstract byte[] serialize();

	/**
	 * Initializes this client message by deserializing the specified data.
	 * @param data	The data representing a client message.
	 */
	public abstract void deSerialize(byte[] data);

}