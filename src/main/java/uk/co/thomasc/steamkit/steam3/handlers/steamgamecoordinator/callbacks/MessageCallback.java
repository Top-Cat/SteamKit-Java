package uk.co.thomasc.steamkit.steam3.handlers.steamgamecoordinator.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.PacketClientGCMsg;
import uk.co.thomasc.steamkit.base.PacketClientGCMsgProtobuf;
import uk.co.thomasc.steamkit.base.gc.IPacketGCMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGCClient;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.util.util.MsgUtil;

/**
 * This callback is fired when a game coordinator message is recieved from the network.
 */
public class MessageCallback extends CallbackMsg {
	/**
	 * raw emsg (with protobuf flag, if present)
	 */
	private final int eMsg;

	/**
	 * Gets the game coordinator message type.
	 */
	public int getEMsg() {
		return MsgUtil.getGCMsg(eMsg);
	}

	/**
	 * Gets the AppID of the game coordinator the message is from.
	 */
	@Getter private final int appId;

	/**
	 * Gets a value indicating whether this message is protobuf'd.
	 * @return true if this instance is protobuf'd; otherwise, false.
	 */
	public boolean isProto() {
		return MsgUtil.isProtoBuf(eMsg);
	}

	/**
	 * Gets the actual message.
	 */
	@Getter private final IPacketGCMsg message;

	public MessageCallback(CMsgGCClient gcMsg) {
		eMsg = gcMsg.getMsgtype();
		appId = gcMsg.getAppid();

		// we are knowingly using this obsolete property
		//this.Payload = gcMsg.payload;

		message = MessageCallback.getPacketGCMsg(gcMsg.getMsgtype(), gcMsg.getPayload().toByteArray());
	}

	static IPacketGCMsg getPacketGCMsg(int eMsg, byte[] data) {
		// strip off the protobuf flag
		final int realEMsg = MsgUtil.getGCMsg(eMsg);

		if (MsgUtil.isProtoBuf(eMsg)) {
			return new PacketClientGCMsgProtobuf(realEMsg, data);
		} else {
			return new PacketClientGCMsg(realEMsg, data);
		}
	}
}
