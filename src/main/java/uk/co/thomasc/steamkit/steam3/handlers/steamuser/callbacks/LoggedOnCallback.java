package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import java.net.InetAddress;
import java.util.Date;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogonResponse;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountFlags;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.SteamUser;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.types.LogOnDetails;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steam2ticket.Steam2Ticket;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.util.NetHelpers;

import com.google.protobuf.ByteString;

/**
 * This callback is returned in response to an attempt to log on to the Steam3 network through {@link SteamUser}.
 */
public final class LoggedOnCallback extends CallbackMsg {
	/**
	 * Gets the result of the logon.
	 */
	@Getter private EResult result;

	/**
	 * Gets the extended result of the logon.
	 */
	@Getter private EResult extendedResult;

	/**
	 * Gets the out of game secs per heartbeat value.
	 * This is used internally by SteamKit to initialize heartbeating.
	 */
	@Getter private int outOfGameSecsPerHeartbeat;

	/**
	 * Gets the in game secs per heartbeat value.
	 * This is used internally by SteamKit to initialize heartbeating.
	 */
	@Getter private int inGameSecsPerHeartbeat;

	/**
	 * Gets or sets the public IP of the client
	 */
	@Getter private InetAddress publicIP;

	/**
	 * Gets the Steam3 server time.
	 */
	@Getter private Date serverTime;

	/**
	 * Gets the account flags assigned by the server.
	 */
	@Getter private EAccountFlags accountFlags;

	/**
	 * Gets the client steam ID.
	 */
	@Getter private SteamID clientSteamID;

	/**
	 * Gets the email domain.
	 */
	@Getter private String emailDomain;

	/**
	 * Gets the Steam2 CellID.
	 */
	@Getter private int cellID;

	/**
	 * Gets the Steam2 ticket.
	 * This is used for authenticated content downloads in Steam2.
	 * This field will only be set when {@link LogOnDetails#requestSteam2Ticket} has been set to true.
	 */
	@Getter private Steam2Ticket steam2Ticket;

	public LoggedOnCallback(CMsgClientLogonResponse resp) {
		this.result = EResult.f(resp.getEresult());
		this.extendedResult = EResult.f(resp.getEresultExtended());

		this.outOfGameSecsPerHeartbeat = resp.getOutOfGameHeartbeatSeconds();
		this.inGameSecsPerHeartbeat = resp.getInGameHeartbeatSeconds();

		this.publicIP = NetHelpers.getIPAddress(resp.getPublicIp());

		this.serverTime = new Date(resp.getRtime32ServerTime());

		this.accountFlags = EAccountFlags.f(resp.getAccountFlags());

		this.clientSteamID = new SteamID(resp.getClientSuppliedSteamid());

		this.emailDomain = resp.getEmailDomain();

		this.cellID = resp.getCellId();

		if (resp.getSteam2Ticket() != ByteString.EMPTY) {
			this.steam2Ticket = new Steam2Ticket(resp.getSteam2Ticket().toByteArray());
		}
	}
}