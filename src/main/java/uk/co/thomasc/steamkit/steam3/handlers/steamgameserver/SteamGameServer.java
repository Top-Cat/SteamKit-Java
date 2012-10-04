package uk.co.thomasc.steamkit.steam3.handlers.steamgameserver;

import java.security.InvalidParameterException;

import com.google.protobuf.ByteString;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogOff;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogon;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientTicketAuthComplete;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgGSStatusReply;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientLogon;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.callbacks.StatusReplyCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.callbacks.TicketAuthCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.types.LogOnDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.LoggedOffCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.LoggedOnCallback;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.util.NetHelpers;
import uk.co.thomasc.steamkit.util.util.Utils;

/**
 * This handler is used for interacting with the Steam network as a game server.
 */
public final class SteamGameServer extends ClientMsgHandler {
	/**
	 * Logs onto the Steam network as a persistent game server.
	 * The client should already have been connected at this point.
	 * Results are return in a {@link LoggedOnCallback}.
	 * @param details	The details to use for logging on.
	 */
	public void logOn(LogOnDetails details) {
		if (details == null) {
			throw new InvalidParameterException("details");
		}

		if (details.username == null || details.username.length() == 0 || details.password == null || details.password.length() == 0) {
			throw new InvalidParameterException("LogOn requires a username and password to be set in 'details'.");
		}

		final ClientMsgProtobuf<CMsgClientLogon.Builder> logon = new ClientMsgProtobuf<CMsgClientLogon.Builder>(CMsgClientLogon.class, EMsg.ClientLogon);

		final SteamID gsId = new SteamID(0, 0, getClient().getConnectedUniverse(), EAccountType.GameServer);

		logon.getProtoHeader().setClientSessionid(0);
		logon.getProtoHeader().setSteamid(gsId.convertToLong());

		final int localIp = (int) NetHelpers.getIPAddress(getClient().getLocalIP());
		logon.getBody().setObfustucatedPrivateIp(localIp ^ MsgClientLogon.ObfuscationMask);

		logon.getBody().setProtocolVersion(MsgClientLogon.CurrentProtocol);

		logon.getBody().setClientOsType(Utils.getOSType().v());
		logon.getBody().setGameServerAppId(details.appId);
		logon.getBody().setMachineId(ByteString.copyFrom(Utils.generateMachineID()));

		logon.getBody().setAccountName(details.username);
		logon.getBody().setPassword(details.password);

		getClient().send(logon);
	}

	/**
	 * Logs the client into the Steam3 network as an anonymous game server.
	 * The client should already have been connected at this point.
	 * Results are returned in a {@link LoggedOnCallback}.
	 * @param appId	The AppID served by this game server, or 0 for the default.
	 */
	public void logOnAnonymous(int appId) {
		final ClientMsgProtobuf<CMsgClientLogon.Builder> logon = new ClientMsgProtobuf<CMsgClientLogon.Builder>(CMsgClientLogon.class, EMsg.ClientLogon);

		final SteamID gsId = new SteamID(0, 0, getClient().getConnectedUniverse(), EAccountType.AnonGameServer);

		logon.getProtoHeader().setClientSessionid(0);
		logon.getProtoHeader().setSteamid(gsId.convertToLong());

		final int localIp = (int) NetHelpers.getIPAddress(getClient().getLocalIP());
		logon.getBody().setObfustucatedPrivateIp(localIp ^ MsgClientLogon.ObfuscationMask);

		logon.getBody().setProtocolVersion(MsgClientLogon.CurrentProtocol);

		logon.getBody().setClientOsType(Utils.getOSType().v());
		logon.getBody().setGameServerAppId(appId);
		logon.getBody().setMachineId(ByteString.copyFrom(Utils.generateMachineID()));

		getClient().send(logon);
	}

	public void logOnAnonymous() {
		logOnAnonymous(0);
	}

	/**
	 * Logs the game server off of the Steam3 network.
	 * This method does not disconnect the client.
	 * Results are returned in a {@link LoggedOffCallback}.
	 */
	public void logOff() {
		final ClientMsgProtobuf<CMsgClientLogOff.Builder> logOff = new ClientMsgProtobuf<CMsgClientLogOff.Builder>(CMsgClientLogOff.class, EMsg.ClientLogOff);
		getClient().send(logOff);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case GSStatusReply:
				handleStatusReply(packetMsg);
				break;
			case ClientTicketAuthComplete:
				handleAuthComplete(packetMsg);
				break;
		}
	}

	void handleStatusReply(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgGSStatusReply.Builder> statusReply = new ClientMsgProtobuf<CMsgGSStatusReply.Builder>(CMsgGSStatusReply.class, packetMsg);

		final StatusReplyCallback callback = new StatusReplyCallback(statusReply.getBody().build());
		getClient().postCallback(callback);
	}

	void handleAuthComplete(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientTicketAuthComplete.Builder> authComplete = new ClientMsgProtobuf<CMsgClientTicketAuthComplete.Builder>(CMsgClientTicketAuthComplete.class, packetMsg);

		final TicketAuthCallback callback = new TicketAuthCallback(authComplete.getBody().build());
		getClient().postCallback(callback);
	}
}
