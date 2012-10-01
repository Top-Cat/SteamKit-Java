package uk.co.thomasc.steamkit.steam3.handlers.steamuser;

import com.google.protobuf.ByteString;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAccountInfo;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogOff;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLoggedOff;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogon;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogonResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientNewLoginKey;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientNewLoginKeyAccepted;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientSessionToken;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUpdateMachineAuth;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUpdateMachineAuthResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientWalletInfoUpdate;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientLogon;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.AccountInfoCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.LoggedOffCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.LoggedOnCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.LoginKeyCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.SessionTokenCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.UpdateMachineAuthCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.WalletInfoCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.types.LogOnDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.types.MachineAuthDetails;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.util.NetHelpers;
import uk.co.thomasc.steamkit.util.util.Utils;

/**
 * This handler handles all user log on/log off related actions and callbacks.
 */
public final class SteamUser extends ClientMsgHandler {

	/**
	 * Gets the SteamID of this client. This value is assigned after a logon attempt has succeeded.
	 * @return The SteamID.
	 */
	public SteamID getSteamId() {
		return getClient().getSteamId();
	}

	/**
	 * Logs the client into the Steam3 network.
	 * The client should already have been connected at this point.
	 * Results are returned in a {@link LoggedOnCallback}.
	 * @param details	The details to use for logging on.
	 * @exception IllegalArgumentException Username and password are not set or are not provided.
	 */
	public void logOn(LogOnDetails details) {
		if (details == null) {
			throw new IllegalArgumentException("details");
		}
		if (details.username.length() == 0 || details.password.length() == 0) {
			throw new IllegalArgumentException("LogOn requires a username and password to be set in 'details'.");
		}

		final ClientMsgProtobuf<CMsgClientLogon.Builder> logon = new ClientMsgProtobuf<CMsgClientLogon.Builder>(EMsg.ClientLogon, CMsgClientLogon.class);

		final SteamID steamId = new SteamID(0, details.accountInstance, getClient().getConnectedUniverse(), EAccountType.Individual);

		final int localIp = (int) NetHelpers.getIPAddress(getClient().getLocalIP());

		logon.getProtoHeader().setClientSessionid(0);
		logon.getProtoHeader().setSteamid(steamId.convertToUInt64());

		logon.getBody().setObfustucatedPrivateIp(localIp ^ MsgClientLogon.ObfuscationMask);

		logon.getBody().setAccountName(details.username);
		logon.getBody().setPassword(details.password);

		logon.getBody().setProtocolVersion(MsgClientLogon.CurrentProtocol);
		logon.getBody().setClientOsType(Utils.getOSType().v());
		logon.getBody().setClientLanguage("english");

		logon.getBody().setSteam2TicketRequest(details.requestSteam2Ticket);

		// we're now using the latest steamclient package version, this is required to get a proper sentry file for steam guard
		logon.getBody().setClientPackageVersion(1771); // TODO: determine if this is still required

		// this is not a proper machine id that Steam accepts
		// but it's good enough for identifying a machine
		logon.getBody().setMachineId(ByteString.copyFrom(Utils.generateMachineID()));

		// steam guard 
		logon.getBody().setAuthCode(details.authCode);

		logon.getBody().setShaSentryfile(details.sentryFileHash != null ? ByteString.copyFrom(details.sentryFileHash) : ByteString.EMPTY);
		logon.getBody().setEresultSentryfile((details.sentryFileHash != null ? EResult.OK : EResult.FileNotFound).v());

		getClient().send(logon);
	}

	/**
	 * Logs the client into the Steam3 network as an anonymous user.
	 * The client should already have been connected at this point.
	 * Results are returned in a {@link LoggedOnCallback}.
	 */
	public void logOnAnonymous() {
		final ClientMsgProtobuf<CMsgClientLogon.Builder> logon = new ClientMsgProtobuf<CMsgClientLogon.Builder>(EMsg.ClientLogon, CMsgClientLogon.class);

		final SteamID auId = new SteamID(0, 0, getClient().getConnectedUniverse(), EAccountType.AnonUser);

		logon.getProtoHeader().setClientSessionid(0);
		logon.getProtoHeader().setSteamid(auId.convertToUInt64());

		logon.getBody().setProtocolVersion(MsgClientLogon.CurrentProtocol);
		logon.getBody().setClientOsType(Utils.getOSType().v());

		// this is not a proper machine id that Steam accepts
		// but it's good enough for identifying a machine
		logon.getBody().setMachineId(ByteString.copyFrom(Utils.generateMachineID()));

		getClient().send(logon);
	}

	/**
	 * Logs the user off of the Steam3 network.
	 * This method does not disconnect the client.
	 * Results are returned in a {@link LoggedOffCallback}.
	 */
	public void logOff() {
		final ClientMsgProtobuf<?> logOff = new ClientMsgProtobuf<CMsgClientLogOff.Builder>(EMsg.ClientLogOff, CMsgClientLogOff.class);
		getClient().send(logOff);
	}

	/**
	 * Sends a machine auth response.
	 * This should normally be used in response to a {@link UpdateMachineAuthCallback}.
	 * @param details	The details pertaining to the response.
	 */
	public void sendMachineAuthResponse(MachineAuthDetails details) {
		final ClientMsgProtobuf<CMsgClientUpdateMachineAuthResponse.Builder> response = new ClientMsgProtobuf<CMsgClientUpdateMachineAuthResponse.Builder>(EMsg.ClientUpdateMachineAuthResponse, CMsgClientUpdateMachineAuthResponse.class);

		// so we respond to the correct message
		response.getProtoHeader().setJobidTarget(details.jobId);

		response.getBody().setCubwrote(details.bytesWritten);
		response.getBody().setEresult(details.result.v());

		response.getBody().setFilename(details.fileName);
		response.getBody().setFilesize(details.fileSize);

		response.getBody().setGetlasterror(details.lastError);
		response.getBody().setOffset(details.offset);

		response.getBody().setShaFile(ByteString.copyFrom(details.sentryFileHash));

		response.getBody().setOtpIdentifier(details.oneTimePassword.identifier);
		response.getBody().setOtpType(details.oneTimePassword.type);
		response.getBody().setOtpValue(details.oneTimePassword.value);

		getClient().send(response);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case ClientLogOnResponse:
				HandleLogOnResponse(packetMsg);
				break;
			case ClientNewLoginKey:
				HandleLoginKey(packetMsg);
				break;
			case ClientSessionToken:
				HandleSessionToken(packetMsg);
				break;
			case ClientLoggedOff:
				HandleLoggedOff(packetMsg);
				break;
			case ClientUpdateMachineAuth:
				HandleUpdateMachineAuth(packetMsg);
				break;
			case ClientAccountInfo:
				HandleAccountInfo(packetMsg);
				break;
			case ClientWalletInfoUpdate:
				HandleWalletInfo(packetMsg);
				break;
		}
	}

	void HandleLoggedOff(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientLoggedOff.Builder> loggedOff = new ClientMsgProtobuf<CMsgClientLoggedOff.Builder>(packetMsg, CMsgClientLoggedOff.class);

		getClient().postCallback(new LoggedOffCallback(loggedOff.getBody().build()));
	}

	void HandleUpdateMachineAuth(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientUpdateMachineAuth.Builder> machineAuth = new ClientMsgProtobuf<CMsgClientUpdateMachineAuth.Builder>(packetMsg, CMsgClientUpdateMachineAuth.class);

		final UpdateMachineAuthCallback innerCallback = new UpdateMachineAuthCallback(machineAuth.getBody().build());
		final JobCallback<?> callback = new JobCallback<UpdateMachineAuthCallback>(new JobID(packetMsg.getSourceJobID()), innerCallback);
		getClient().postCallback(callback);
	}

	void HandleSessionToken(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientSessionToken.Builder> sessToken = new ClientMsgProtobuf<CMsgClientSessionToken.Builder>(packetMsg, CMsgClientSessionToken.class);

		final SessionTokenCallback callback = new SessionTokenCallback(sessToken.getBody().build());
		getClient().postCallback(callback);
	}

	void HandleLoginKey(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientNewLoginKey.Builder> loginKey = new ClientMsgProtobuf<CMsgClientNewLoginKey.Builder>(packetMsg, CMsgClientNewLoginKey.class);

		final ClientMsgProtobuf<CMsgClientNewLoginKeyAccepted.Builder> resp = new ClientMsgProtobuf<CMsgClientNewLoginKeyAccepted.Builder>(EMsg.ClientNewLoginKeyAccepted, CMsgClientNewLoginKeyAccepted.class);
		resp.getBody().setUniqueId(loginKey.getBody().getUniqueId());

		getClient().send(resp);

		final LoginKeyCallback callback = new LoginKeyCallback(loginKey.getBody().build());
		getClient().postCallback(callback);
	}

	void HandleLogOnResponse(IPacketMsg packetMsg) {
		if (packetMsg.isProto()) {
			final ClientMsgProtobuf<CMsgClientLogonResponse.Builder> logonResp = new ClientMsgProtobuf<CMsgClientLogonResponse.Builder>(packetMsg, CMsgClientLogonResponse.class);

			final LoggedOnCallback callback = new LoggedOnCallback(logonResp.getBody().build());
			getClient().postCallback(callback);
		}
	}

	void HandleAccountInfo(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientAccountInfo.Builder> accInfo = new ClientMsgProtobuf<CMsgClientAccountInfo.Builder>(packetMsg, CMsgClientAccountInfo.class);

		final AccountInfoCallback callback = new AccountInfoCallback(accInfo.getBody().build());
		getClient().postCallback(callback);
	}

	void HandleWalletInfo(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientWalletInfoUpdate.Builder> walletInfo = new ClientMsgProtobuf<CMsgClientWalletInfoUpdate.Builder>(packetMsg, CMsgClientWalletInfoUpdate.class);

		final WalletInfoCallback callback = new WalletInfoCallback(walletInfo.getBody().build());
		getClient().postCallback(callback);
	}
}
