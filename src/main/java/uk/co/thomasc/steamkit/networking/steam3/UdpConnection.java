package uk.co.thomasc.steamkit.networking.steam3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.util.Arrays;

import uk.co.thomasc.steamkit.base.IClientMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUdpPacketType;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ChallengeData;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ConnectData;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;
import uk.co.thomasc.steamkit.util.logging.DebugLog;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class UdpConnection extends Connection {
	/**
	 * Seconds to wait before sending packets.
	 */
	private final int RESEND_DELAY = 3;

	/**
	 * Seconds to wait before considering the connection dead.
	 */
	private final int TIMEOUT_DELAY = 60;

	/**
	 * Maximum number of packets to resend when RESEND_DELAY is exceeded.
	 */
	private final int RESEND_COUNT = 3;

	/**
	 * Maximum number of packets that we can be waiting on at a time.
	 */
	private final int AHEAD_COUNT = 5;

	/**
	 * Contains information about the state of the connection, used to filter out packets that are
	 * unexpected or not valid given the state of the connection.
	 */
	private UdpState state;

	private Thread netThread;
	private DatagramSocket sock;
	private IPEndPoint remoteEndPoint;

	private Date timeOut;
	private Date nextResend;

	private int sourceConnId = 512;
	private int remoteConnId;

	/**
	 * The next outgoing sequence number to be used.
	 */
	private int outSeq;

	/**
	 * The highest sequence number of an outbound packet that has been sent.
	 */
	private int outSeqSent;

	/**
	 * The sequence number of the highest packet acknowledged by the server.
	 */
	private int outSeqAcked;

	/**
	 * The sequence number we plan on acknowledging receiving with the next Ack. All packets below or equal
	 * to inSeq *must* have been received, but not necessarily handled.
	 */
	private int inSeq;

	/**
	 * The highest sequence number we've acknowledged receiving.
	 */
	private int inSeqAcked;

	/**
	 * The highest sequence number we've processed.
	 */
	private int inSeqHandled;

	private List<UdpPacket> outPackets;
	private Map<Integer, UdpPacket> inPackets;

	public UdpConnection() {
		try {
			sock = new DatagramSocket();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		state = UdpState.Disconnected;
	}

	/**
	 * Connects to the specified CM server.
	 * @param endPoint	The CM server.
	 */
	@Override
	public void connect(IPEndPoint endPoint) {
		disconnect();

		outPackets = new ArrayList<UdpPacket>();
		inPackets = new HashMap<Integer, UdpPacket>();

		remoteEndPoint = endPoint;
		remoteConnId = 0;

		outSeq = 1;
		outSeqSent = 0;
		outSeqAcked = 0;

		inSeq = 0;
		inSeqAcked = 0;
		inSeqHandled = 0;

		netThread = new Thread(new NetLoop());
		netThread.setName("UdpConnection Thread");
		netThread.start();
	}

	/**
	 * Disconnects this instance, blocking until the queue of messages is empty or the connection
	 * is otherwise terminated.
	 */
	@Override
	public void disconnect() {
		if (netThread == null) {
			return;
		}

		// Play nicely and let the server know that we're done. Other party is expected to Ack this,
		// so it needs to be sent sequenced.
		sendSequenced(new UdpPacket(EUdpPacketType.Disconnect));

		state = UdpState.Disconnecting;

		// Graceful shutdown allows for the connection to empty its queue of messages to send
		try {
			netThread.join();
		} catch (final InterruptedException e) {
		};

		// Advance this the same way that steam does, when a socket gets reused.
		sourceConnId += 256;
	}

	/**
	 * Serializes and sends the provided message to the server in as many packets as is necessary.
	 * @param clientMsg	The ClientMsg
	 */
	@Override
	public void send(IClientMsg clientMsg) {
		if (state != UdpState.Connected) {
			return;
		}

		try {
			byte[] data = clientMsg.serialize();

			if (netFilter != null) {
				data = netFilter.processOutgoing(data);
			}

			sendData(new BinaryReader(data));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the data sequenced as a single message, splitting it into multiple parts if necessary.
	 * @param ms	The data to send.
	 */
	private void sendData(BinaryReader ms) {
		final UdpPacket[] packets = new UdpPacket[ms.getRemaining() / UdpPacket.MAX_PAYLOAD + 1];

		for (int i = 0; i < packets.length; i++) {
			final long index = i * UdpPacket.MAX_PAYLOAD;
			final int length = (int) Math.min(UdpPacket.MAX_PAYLOAD, ms.getRemaining() - index);

			packets[i] = new UdpPacket(EUdpPacketType.Data, ms, length);
			packets[i].getHeader().msgSize = length;
		}

		sendSequenced(packets);
	}

	/**
	 * Sends the packet as a sequenced, reliable packet.
	 * @param packet	The packet.
	 */
	private void sendSequenced(UdpPacket packet) {
		packet.getHeader().seqThis = outSeq;
		packet.getHeader().msgStartSeq = outSeq;
		packet.getHeader().packetsInMsg = 1;

		outPackets.add(packet);

		outSeq++;
	}

	/**
	 * Sends the packets as one sequenced, reliable net message.
	 * @param packets	The packets that make up the single net message
	 */
	private void sendSequenced(UdpPacket[] packets) {
		final int msgStart = outSeq;

		for (final UdpPacket packet : packets) {
			sendSequenced(packet);

			// Correct for any assumptions made for the single-packet case.
			packet.getHeader().packetsInMsg = packets.length;
			packet.getHeader().msgStartSeq = msgStart;
		}
	}

	/**
	 * Sends a packet immediately.
	 * @param packet	The packet.
	 */
	private void sendPacket(UdpPacket packet) {
		packet.getHeader().sourceConnID = sourceConnId;
		packet.getHeader().destConnID = remoteConnId;
		packet.getHeader().seqAck = inSeqAcked = inSeq;

		DebugLog.writeLine("UdpConnection", "Sent -> %s Seq %d Ack %d; %d bytes; Message: %d bytes %d packets", packet.getHeader().packetType, packet.getHeader().seqThis, packet.getHeader().seqAck, packet.getHeader().payloadSize, packet.getHeader().msgSize, packet.getHeader().packetsInMsg);

		final byte[] data = packet.getData();

		try {
			sock.send(new DatagramPacket(data, data.length, new InetSocketAddress(remoteEndPoint.getIpAddress(), remoteEndPoint.getPort())));
		} catch (final IOException e) {
			DebugLog.writeLine("UdpConnection", "Critical socket failure: " + e.getMessage());

			state = UdpState.Disconnected;
			return;
		}

		// If we've been idle but completely acked for more than two seconds, the next sent
		// packet will trip the resend detection. This fixes that.
		if (outSeqSent == outSeqAcked) {
			final Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND, RESEND_DELAY);
			nextResend = cl.getTime();
		}

		// Sending should generally carry on from the packet most recently sent, even if it was a
		// resend (who knows what else was lost).
		if (packet.getHeader().seqThis > 0) {
			outSeqSent = packet.getHeader().seqThis;
		}
	}

	/**
	 * Sends a datagram Ack, used when an Ack needs to be sent but there is no data response to piggy-back on.
	 */
	private void sendAck() {
		sendPacket(new UdpPacket(EUdpPacketType.Datagram));
	}

	/**
	 * Sends or resends sequenced messages, if necessary. Also responsible for throttling
	 * the rate at which they are sent.
	 */
	private void sendPendingMessages() {
		if (new Date().compareTo(nextResend) > 0 && outSeqSent > outSeqAcked) {
			DebugLog.writeLine("UdpConnection", "Sequenced packet resend required");

			// Don't send more than 3 (Steam behavior?)
			for (int i = 0; i < RESEND_COUNT && i < outPackets.size(); i++) {
				sendPacket(outPackets.get(i));
			}

			final Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND, RESEND_DELAY);
			nextResend = cl.getTime();
		} else if (outSeqSent < outSeqAcked + AHEAD_COUNT) {
			// I've never seen Steam send more than 4 packets before it gets an Ack, so this limits the
			// number of sequenced packets that can be sent out at one time.
			for (int i = outSeqSent - outSeqAcked; i < AHEAD_COUNT && i < outPackets.size(); i++) {
				sendPacket(outPackets.get(i));
			}
		}
	}

	/**
	 * Returns the number of message parts in the next message.
	 * @return Non-zero number of message parts if a message is ready to be handled, 0 otherwise
	 */
	private int readyMessageParts() {
		// Make sure that the first packet of the next message to handle is present
		if (!inPackets.containsKey(inSeqHandled + 1)) {
			return 0;
		}

		final UdpPacket packet = inPackets.get(inSeqHandled + 1);

		// ...and if relevant, all subparts of the message also
		for (int i = 1; i < packet.getHeader().packetsInMsg; i++) {
			if (!inPackets.containsKey(inSeqHandled + 1 + i)) {
				return 0;
			}
		}

		return packet.getHeader().packetsInMsg;
	}

	/**
	 * Dispatches up to one message to the rest of SteamKit
	 * @return True if a message was dispatched, false otherwise
	 */
	private boolean dispatchMessage() {
		final int numPackets = readyMessageParts();

		if (numPackets == 0) {
			return false;
		}

		final BinaryWriter payload = new BinaryWriter();
		for (int i = 0; i < numPackets; i++) {
			final UdpPacket packet = inPackets.get(++inSeqHandled);
			inPackets.remove(inSeqHandled);

			try {
				payload.write(packet.getPayload().readBytes());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		byte[] data = payload.toByteArray();

		if (netFilter != null) {
			data = netFilter.processIncoming(data);
		}

		DebugLog.writeLine("UdpConnection", "Dispatching message; %d bytes", data.length);

		onNetMsgReceived(new NetMsgEventArgs(data, remoteEndPoint));

		return true;
	}

	class NetLoop implements Runnable {
		/**
		 * Processes incoming packets, maintains connection consistency, and oversees outgoing packets.
		 */
		@Override
		public void run() {
			// Variables that will be used deeper in the function; locating them here avoids recreating
			// them since they don't need to be.
			final byte[] buf = new byte[2048];
			final DatagramPacket packet = new DatagramPacket(buf, buf.length);
			boolean received = false;
			try {
				sock.setSoTimeout(150);
			} catch (final SocketException e1) {
				e1.printStackTrace();
			}

			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND, TIMEOUT_DELAY);
			timeOut = cl.getTime();

			cl = Calendar.getInstance();
			cl.add(Calendar.SECOND, RESEND_DELAY);
			nextResend = cl.getTime();

			// Begin by sending off the challenge request
			sendPacket(new UdpPacket(EUdpPacketType.ChallengeReq));
			state = UdpState.ChallengeReqSent;

			while (state != UdpState.Disconnected) {
				try {
					try {
						sock.receive(packet);
						received = true;
					} catch (final SocketTimeoutException e) {
						received = false;
					}

					// Wait up to 150ms for data, if none is found and the timeout is exceeded, we're done here.
					if (!received && new Date().compareTo(timeOut) > 0) {
						DebugLog.writeLine("UdpConnection", "Connection timed out");

						state = UdpState.Disconnected;
						break;
					}

					// By using a 10ms wait, we allow for multiple packets sent at the time to all be processed before moving on
					// to processing output and therefore Acks (the more we process at the same time, the fewer acks we have to send)
					try {
						Thread.sleep(10);
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
					while (received) {
						// Data from the desired server was received; delay timeout
						cl = Calendar.getInstance();
						cl.add(Calendar.SECOND, TIMEOUT_DELAY);
						timeOut = cl.getTime();

						final BinaryReader ms = new BinaryReader(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
						final UdpPacket pkt = new UdpPacket(ms);

						receivePacket(pkt);

						try {
							sock.receive(packet);
							received = true;
						} catch (final SocketTimeoutException e) {
							received = false;
						}
					}
				} catch (final IOException e) {
					DebugLog.writeLine("UdpConnection", "Critical socket failure: " + e.getMessage());

					state = UdpState.Disconnected;
					break;
				}

				// Send or resend any sequenced packets; a call to ReceivePacket can set our state to disconnected
				// so don't send anything we have queued in that case
				if (state != UdpState.Disconnected) {
					sendPendingMessages();
				}

				// If we received data but had no data to send back, we need to manually Ack (usually tags along with
				// outgoing data); also acks disconnections
				if (inSeq != inSeqAcked) {
					sendAck();
				}

				// If a graceful shutdown has been requested, nothing in the outgoing queue is discarded.
				// Once it's empty, we exit, since the last packet was our disconnect notification.
				if (state == UdpState.Disconnecting && outPackets.size() == 0) {
					DebugLog.writeLine("UdpConnection", "Graceful disconnect completed");

					state = UdpState.Disconnected;
				}
			}

			DebugLog.writeLine("UdpConnection", "Calling OnDisconnected");
			onDisconnected(false);
		}
	}

	/**
	 * Receives the packet, performs all sanity checks and then passes it along as necessary.
	 * @param packet	The packet.
	 */
	private void receivePacket(UdpPacket packet) {
		// Check for a malformed packet
		if (!packet.isValid()) {
			return;
		} else if (remoteConnId > 0 && packet.getHeader().sourceConnID != remoteConnId) {
			return;
		}

		DebugLog.writeLine("UdpConnection", "<- Recv'd %s Seq %d Ack %d; %d bytes; Message: %d bytes %d packets", packet.getHeader().packetType, packet.getHeader().seqThis, packet.getHeader().seqAck, packet.getHeader().payloadSize, packet.getHeader().msgSize, packet.getHeader().packetsInMsg);

		// Throw away any duplicate messages we've already received, making sure to
		// re-ack it in case it got lost.
		if (packet.getHeader().packetType == EUdpPacketType.Data && packet.getHeader().seqThis < inSeq) {
			sendAck();
			return;
		}

		// When we get a SeqAck, all packets with sequence numbers below that have been safely received by
		// the server; we are now free to remove our copies
		if (outSeqAcked < packet.getHeader().seqAck) {
			outSeqAcked = packet.getHeader().seqAck;

			// outSeqSent can be less than this in a very rare case involving resent packets.
			if (outSeqSent < outSeqAcked) {
				outSeqSent = outSeqAcked;
			}

			final List<UdpPacket> toRemove = new ArrayList<UdpPacket>();
			for (final UdpPacket pkt : outPackets) {
				if (pkt.getHeader().seqThis <= outSeqAcked) {
					toRemove.add(pkt);
				}
			}

			outPackets.removeAll(toRemove);

			final Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND, RESEND_DELAY);
			nextResend = cl.getTime();
		}

		// inSeq should always be the latest value that we can ack, so advance it as far as is possible.
		if (packet.getHeader().seqThis == inSeq + 1) {
			do {
				inSeq++;
			} while (inPackets.containsKey(inSeq + 1));
		}

		switch (packet.getHeader().packetType) {
			case Challenge:
				receiveChallenge(packet);
				break;
			case Accept:
				receiveAccept(packet);
				break;
			case Data:
				receiveData(packet);
				break;
			case Disconnect:
				DebugLog.writeLine("UdpConnection", "Disconnected by server");
				state = UdpState.Disconnected;
				return;
			case Datagram:
				break;
			default:
				DebugLog.writeLine("UdpConnection", "Received unexpected packet type " + packet.getHeader().packetType);
				break;
		}
	}

	/**
	 * Receives the challenge and responds with a Connect request
	 * @param packet	The packet.
	 */
	private void receiveChallenge(UdpPacket packet) {
		if (state != UdpState.ChallengeReqSent) {
			return;
		}

		final ChallengeData cr = new ChallengeData();
		try {
			cr.deSerialize(packet.getPayload());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		final ConnectData cd = new ConnectData();
		cd.challengeValue = cr.challengeValue ^ ConnectData.CHALLENGE_MASK;

		final BinaryWriter ms = new BinaryWriter();
		try {
			cd.serialize(ms);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		sendSequenced(new UdpPacket(EUdpPacketType.Connect, new BinaryReader(ms.toByteArray())));

		state = UdpState.ConnectSent;
		inSeqHandled = packet.getHeader().seqThis;
	}

	/**
	 * Receives the notification of an accepted connection and sets the connection id that will be used for the
	 * connection's duration.
	 * @param packet
	 */
	private void receiveAccept(UdpPacket packet) {
		if (state != UdpState.ConnectSent) {
			return;
		}

		DebugLog.writeLine("UdpConnection", "Connection established");

		state = UdpState.Connected;
		remoteConnId = packet.getHeader().sourceConnID;
		inSeqHandled = packet.getHeader().seqThis;

		onConnected(EventArgs.Empty);
	}

	/**
	 * Receives typical data packets before dispatching them for consumption by the rest of SteamKit
	 * @param packet	The packet.
	 */
	private void receiveData(UdpPacket packet) {
		// Data packets are unexpected if a valid connection has not been established
		if (state != UdpState.Connected && state != UdpState.Disconnecting) {
			return;
		}

		// If we receive a packet that we've already processed (e.g. it got resent due to a lost ack)
		// or that is already waiting to be processed, do nothing.
		if (packet.getHeader().seqThis <= inSeqHandled || inPackets.containsKey(packet.getHeader().seqThis)) {
			return;
		}

		inPackets.put(packet.getHeader().seqThis, packet);

		while (dispatchMessage()) {
			;
		}
	}

	@Override
	public InetAddress getLocalIP() {
		return sock.getLocalAddress();
	}
}
