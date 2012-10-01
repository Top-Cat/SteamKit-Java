package uk.co.thomasc.steamkit.steam3.handlers.steamgamecoordinator;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGCClient;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.gc.IClientGCMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamgamecoordinator.callbacks.MessageCallback;
import uk.co.thomasc.steamkit.util.util.MsgUtil;

import com.google.protobuf.ByteString;

/**
 * This handler handles all game coordinator messaging.
 */
public final class SteamGameCoordinator extends ClientMsgHandler {
	/**
	 * Sends a game coordinator message for a specific appid.
	 * @param msg	The GC message to send.
	 * @param appId	The app id of the game coordinator to send to.
	 */
	public void send(IClientGCMsg msg, int appId) {
		ClientMsgProtobuf<CMsgGCClient.Builder> clientMsg = new ClientMsgProtobuf<CMsgGCClient.Builder>(EMsg.ClientToGC, CMsgGCClient.class);

		clientMsg.getBody().setMsgtype(MsgUtil.makeGCMsg(msg.getMsgType(), msg.isProto()));
		clientMsg.getBody().setAppid(appId);

		clientMsg.getBody().setPayload(ByteString.copyFrom(msg.serialize()));

		getClient().send(clientMsg);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 * @param packetMsg	The packet message that contains the data.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		if (packetMsg.getMsgType() == EMsg.ClientFromGC) {
			ClientMsgProtobuf<CMsgGCClient.Builder> msg = new ClientMsgProtobuf<CMsgGCClient.Builder>(packetMsg, CMsgGCClient.class);

			MessageCallback callback = new MessageCallback(msg.getBody().build());
			getClient().postCallback(callback);
		}
	}
}