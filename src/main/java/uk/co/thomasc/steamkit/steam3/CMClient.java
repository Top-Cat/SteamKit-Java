package uk.co.thomasc.steamkit.steam3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IClientMsg;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.Msg;
import uk.co.thomasc.steamkit.base.PacketClientMsg;
import uk.co.thomasc.steamkit.base.PacketClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.PacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesBase.CMsgMulti;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientHeartBeat;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLogonResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientServerList;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientServerList.Server;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EServerType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUniverse;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgChannelEncryptRequest;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgChannelEncryptResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgChannelEncryptResult;
import uk.co.thomasc.steamkit.networking.steam3.Connection;
import uk.co.thomasc.steamkit.networking.steam3.NetFilterEncryption;
import uk.co.thomasc.steamkit.networking.steam3.NetMsgEventArgs;
import uk.co.thomasc.steamkit.networking.steam3.TcpConnection;
import uk.co.thomasc.steamkit.networking.steam3.UdpConnection;
import uk.co.thomasc.steamkit.steam3.steamclient.SteamClient;
import uk.co.thomasc.steamkit.steam3.steamclient.callbacks.ConnectedCallback;
import uk.co.thomasc.steamkit.types.steamid.SteamID;
import uk.co.thomasc.steamkit.util.KeyDictionary;
import uk.co.thomasc.steamkit.util.ScheduledFunction;
import uk.co.thomasc.steamkit.util.ZipUtil;
import uk.co.thomasc.steamkit.util.cSharp.events.Action;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgsGeneric;
import uk.co.thomasc.steamkit.util.cSharp.events.EventHandler;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;
import uk.co.thomasc.steamkit.util.cSharp.ip.ProtocolType;
import uk.co.thomasc.steamkit.util.crypto.CryptoHelper;
import uk.co.thomasc.steamkit.util.crypto.RSACrypto;
import uk.co.thomasc.steamkit.util.logging.DebugLog;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.util.MsgUtil;
import uk.co.thomasc.steamkit.util.util.NetHelpers;

/**
 * This base client handles the underlying connection to a CM server. This class should not be use directly, but through the {@link SteamClient} class.
 */
public abstract class CMClient {
	final short PortCM_PublicEncrypted = 27017;
	final short PortCM_Public = 27014;

	/**
	 * Bootstrap list of CM servers.
	 */
	public static final InetAddress[] Servers;
	static {
		InetAddress[] temp = null;
		try {
			temp = new InetAddress[] {
				InetAddress.getByName("68.142.64.164"),
				InetAddress.getByName("68.142.64.165"),
				InetAddress.getByName("68.142.91.34"),
				InetAddress.getByName("68.142.91.35"),
				InetAddress.getByName("68.142.91.36"),
				InetAddress.getByName("68.142.116.178"),
				InetAddress.getByName("68.142.116.179"),

				InetAddress.getByName("69.28.145.170"),
				InetAddress.getByName("69.28.145.171"),
				InetAddress.getByName("69.28.145.172"),
				InetAddress.getByName("69.28.156.250"),

				InetAddress.getByName("72.165.61.174"),
				InetAddress.getByName("72.165.61.175"),
				InetAddress.getByName("72.165.61.176"),
				InetAddress.getByName("72.165.61.185"),
				InetAddress.getByName("72.165.61.187"),
				InetAddress.getByName("72.165.61.188"),

				InetAddress.getByName("81.171.115.34"),
				InetAddress.getByName("81.171.115.35"),
				InetAddress.getByName("81.171.115.36"),
				InetAddress.getByName("81.171.115.37"),

				InetAddress.getByName("146.66.152.12"),
				InetAddress.getByName("146.66.152.13"),
				InetAddress.getByName("146.66.152.14"),
				InetAddress.getByName("146.66.152.15"),

				InetAddress.getByName("203.77.185.4"),
				InetAddress.getByName("203.77.185.5"),

				InetAddress.getByName("205.185.220.133"),

				InetAddress.getByName("208.64.200.201"),
				InetAddress.getByName("208.111.171.82"),
				InetAddress.getByName("208.111.133.84"),
				InetAddress.getByName("208.111.133.85"),

				InetAddress.getByName("209.197.20.104"),
				InetAddress.getByName("209.197.30.36"),
				InetAddress.getByName("209.197.6.233"),
			};
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}
		Servers = temp;
	}

	/**
	 * Returns the the local IP of this client.
	 * @return The local IP.
	 */
	public InetAddress getLocalIP() {
		return connection.getLocalIP();
	}

	/**
	 * The universe.
	 */
	@Getter private EUniverse connectedUniverse;

	/**
	 * The session ID.
	 */
	@Getter private Integer sessionId;

	/**
	 * The SteamID.
	 */
	@Getter private SteamID steamId;

	Connection connection;
	byte[] tempSessionKey;
	boolean encrypted;

	ScheduledFunction heartBeatFunc;

	Map<EServerType, List<IPEndPoint>> serverMap;

	public CMClient() {
		this(ProtocolType.Tcp);
	}

	/**
	 * Initializes a new instance of the {@link CMClient} class with a specific connection type.
	 * @param type	The connection type to use.
	 * @throws UnsupportedOperationException The provided {@link ProtocolType} is not supported. Only Tcp and Udp are available.
	 */
	public CMClient(ProtocolType type) {
		serverMap = new HashMap<EServerType, List<IPEndPoint>>();

		switch (type) {
			case Tcp:
				connection = new TcpConnection();
				break;
			case Udp:
				connection = new UdpConnection();
				break;
			default:
				throw new UnsupportedOperationException("The provided protocol type is not supported. Only Tcp and Udp are available.");
		}

		connection.netMsgReceived.addEventHandler(new EventHandler<NetMsgEventArgs>() {
			@Override
			public void handleEvent(Object sender, NetMsgEventArgs e) {
				try {
					onClientMsgReceived(CMClient.getPacketMsg(e.getData()));
				} catch (final IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		connection.disconnected.addEventHandler(new EventHandler<EventArgsGeneric<Boolean>>() {
			@Override
			public void handleEvent(Object sender, EventArgsGeneric<Boolean> e) {
				connectedUniverse = EUniverse.Invalid;

				heartBeatFunc.stop();
				connection.netFilter = null;

				onClientDisconnected(e.getValue());
			}
		});
		connection.connected.addEventHandler(new EventHandler<EventArgs>() {
			@Override
			public void handleEvent(Object sender, EventArgs e) {
				// If we're on an encrypted connection, we wait for the handshake to complete
				if (encrypted) {
					return;
				}

				// we only connect to the public universe
				connectedUniverse = EUniverse.Public;

				// since there is no encryption handshake, we're 'connected' after the underlying connection is established
				onClientConnected();
			}
		});
		heartBeatFunc = new ScheduledFunction(new Action() {
			@Override
			public void call() {
				send(new ClientMsgProtobuf<CMsgClientHeartBeat.Builder>(CMsgClientHeartBeat.class, EMsg.ClientHeartBeat));
			}
		});
	}

	public void connect() {
		connect(true);
	}

	/**
	 * Connects this client to a Steam3 server.
	 * This begins the process of connecting and encrypting the data channel between the client and the server.
	 * Results are returned in a {@link ConnectedCallback}
	 * @param bEncrypted	If set to true the underlying connection to Steam will be encrypted. This is the default mode of communication. Previous versions of SteamKit always used encryption.
	 */
	public void connect(boolean bEncrypted) {
		disconnect();
		encrypted = bEncrypted;

		final Random random = new Random();

		final InetAddress server = CMClient.Servers[random.nextInt(CMClient.Servers.length)];
		final IPEndPoint endPoint = new IPEndPoint(server, bEncrypted ? PortCM_PublicEncrypted : PortCM_Public);

		connection.connect(endPoint);
	}

	/**
	 * Disconnects this client.
	 */
	public void disconnect() {
		heartBeatFunc.stop();

		connection.disconnect();
	}

	/**
	 * Sends the specified client message to the server.
	 * This method automatically assigns the correct SessionID and SteamID of the message.
	 * @param msg	The client message to send.
	 */
	public void send(IClientMsg msg) {
		if (sessionId != null) {
			msg.setSessionID(sessionId);
		}

		if (steamId != null) {
			msg.setSteamID(steamId);
		}

		DebugLog.writeLine("CMClient", "Sent -> EMsg: %s (Proto: %s)", msg.getMsgType(), msg.isProto());

		// we'll swallow any network failures here because they will be thrown later
		// on the network thread, and that will lead to a disconnect callback
		// down the line

		try {
			connection.send(msg);
		} catch (final IOException e) {
		}
	}

	/**
	 * Returns the list of servers matching the given type
	 * @param type	Server type requested
	 * @return List of server endpoints
	 */
	public List<IPEndPoint> getServersOfType(EServerType type) {
		if (!serverMap.containsKey(type)) {
			return new ArrayList<IPEndPoint>();
		}

		return serverMap.get(type);
	}

	/**
	 * Called when a client message is received from the network.
	 * @param packetMsg	The packet message.
	 * @throws IOException 
	 */
	protected void onClientMsgReceived(IPacketMsg packetMsg) throws IOException {
		DebugLog.writeLine("CMClient", "<- Recv'd EMsg: %s (%d) (Proto: %s)", packetMsg.getMsgType(), packetMsg.getMsgType().v(), packetMsg.isProto());

		switch (packetMsg.getMsgType()) {
			case ChannelEncryptRequest:
				handleEncryptRequest(packetMsg);
				break;
			case ChannelEncryptResult:
				handleEncryptResult(packetMsg);
				break;
			case Multi:
				handleMulti(packetMsg);
				break;
			case ClientLogOnResponse: // we handle this to get the SteamID/SessionID and to setup heartbeating
				handleLogOnResponse(packetMsg);
				break;
			case ClientLoggedOff: // to stop heartbeating when we get logged off
				handleLoggedOff(packetMsg);
				break;
			case ClientServerList: // Steam server list
				handleServerList(packetMsg);
				break;
		}
	}

	/**
	 * Called when the client is physically disconnected from Steam3.
	 */
	protected abstract void onClientDisconnected(boolean newconnection);

	/**
	 * Called when the client is connected to Steam3 and is ready to send messages.
	 */
	protected abstract void onClientConnected();

	static IPacketMsg getPacketMsg(byte[] data) {
		final byte[] rMsg = new byte[4];
		for (int i = 0; i < 4; i++) {
			rMsg[3 - i] = data[i];
		}
		final ByteBuffer buffer = ByteBuffer.wrap(rMsg);
		final int rawEMsg = buffer.getInt();

		final EMsg eMsg = MsgUtil.getMsg(rawEMsg);

		switch (eMsg) {
		// certain message types are always MsgHdr
			case ChannelEncryptRequest:
			case ChannelEncryptResponse:
			case ChannelEncryptResult:
				return new PacketMsg(eMsg, data);
		}

		if (MsgUtil.isProtoBuf(rawEMsg)) {
			// if the emsg is flagged, we're a proto message
			return new PacketClientMsgProtobuf(eMsg, data);
		} else {
			// otherwise we're a struct message
			return new PacketClientMsg(eMsg, data);
		}
	}

	void handleMulti(IPacketMsg packetMsg) throws IOException {
		if (!packetMsg.isProto()) {
			DebugLog.writeLine("CMClient", "HandleMulti got non-proto MsgMulti!!");
			return;
		}

		final ClientMsgProtobuf<CMsgMulti.Builder> msgMulti = new ClientMsgProtobuf<CMsgMulti.Builder>(CMsgMulti.class, packetMsg);

		byte[] payload = msgMulti.getBody().getMessageBody().toByteArray();

		if (msgMulti.getBody().getSizeUnzipped() > 0) {
			try {
				payload = ZipUtil.deCompress(payload);
			} catch (final IOException ex) {
				DebugLog.writeLine("CMClient", "HandleMulti encountered an exception when decompressing.\n%s", ex.toString());
				return;
			}
		}

		final BinaryReader ds = new BinaryReader(payload);

		while (!ds.isAtEnd()) {
			final int subSize = ds.readInt();
			final byte[] subData = ds.readBytes(subSize);

			onClientMsgReceived(CMClient.getPacketMsg(subData));
		}
	}

	void handleLogOnResponse(IPacketMsg packetMsg) {
		if (!packetMsg.isProto()) {
			DebugLog.writeLine("CMClient", "HandleLogOnResponse got non-proto MsgClientLogonResponse!!");
			return;
		}

		final ClientMsgProtobuf<CMsgClientLogonResponse.Builder> logonResp = new ClientMsgProtobuf<CMsgClientLogonResponse.Builder>(CMsgClientLogonResponse.class, packetMsg);

		if (EResult.f(logonResp.getBody().getEresult()) == EResult.OK) {
			sessionId = logonResp.getProtoHeader().getClientSessionid();
			steamId = new SteamID(logonResp.getProtoHeader().getSteamid());

			final int hbDelay = logonResp.getBody().getOutOfGameHeartbeatSeconds();

			// restart heartbeat
			heartBeatFunc.stop();
			heartBeatFunc.delay = hbDelay * 1000;
			heartBeatFunc.start();
		}
	}

	void handleEncryptRequest(IPacketMsg packetMsg) {
		final Msg<MsgChannelEncryptRequest> encRequest = new Msg<MsgChannelEncryptRequest>(packetMsg, MsgChannelEncryptRequest.class);

		final EUniverse eUniv = encRequest.getBody().universe;
		final int protoVersion = encRequest.getBody().protocolVersion;

		DebugLog.writeLine("CMClient", "Got encryption request. Universe: %s Protocol ver: %d", eUniv, protoVersion);

		final byte[] pubKey = KeyDictionary.getPublicKey(eUniv);

		if (pubKey == null) {
			DebugLog.writeLine("CMClient", "HandleEncryptionRequest got request for invalid universe! Universe: %s Protocol ver: %d", eUniv, protoVersion);
			return;
		}

		connectedUniverse = eUniv;

		final Msg<MsgChannelEncryptResponse> encResp = new Msg<MsgChannelEncryptResponse>(MsgChannelEncryptResponse.class);

		tempSessionKey = CryptoHelper.GenerateRandomBlock(32);
		byte[] cryptedSessKey = null;

		final RSACrypto rsa = new RSACrypto(pubKey);
		cryptedSessKey = rsa.encrypt(tempSessionKey);

		final byte[] keyCrc = CryptoHelper.CRCHash(cryptedSessKey);

		try {
			encResp.write(cryptedSessKey);
			encResp.write(keyCrc);
			encResp.write(0);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		send(encResp);
	}

	void handleEncryptResult(IPacketMsg packetMsg) {
		final Msg<MsgChannelEncryptResult> encResult = new Msg<MsgChannelEncryptResult>(packetMsg, MsgChannelEncryptResult.class);

		DebugLog.writeLine("CMClient", "Encryption result: %s", encResult.getBody().result);

		if (encResult.getBody().result == EResult.OK) {
			connection.netFilter = new NetFilterEncryption(tempSessionKey);
		}
	}

	void handleLoggedOff(IPacketMsg packetMsg) {
		sessionId = null;
		steamId = null;

		heartBeatFunc.stop();
	}

	void handleServerList(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientServerList.Builder> listMsg = new ClientMsgProtobuf<CMsgClientServerList.Builder>(CMsgClientServerList.class, packetMsg);

		for (final Server server : listMsg.getBody().getServersList()) {
			final EServerType type = EServerType.f(server.getServerType());

			if (!serverMap.containsKey(type)) {
				serverMap.put(type, new ArrayList<IPEndPoint>());
			}

			serverMap.get(type).add(new IPEndPoint(NetHelpers.getIPAddress(server.getServerIp()), (short) server.getServerPort()));
		}
	}
}
