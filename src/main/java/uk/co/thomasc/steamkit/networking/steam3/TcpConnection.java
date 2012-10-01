package uk.co.thomasc.steamkit.networking.steam3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import uk.co.thomasc.steamkit.base.IClientMsg;
import uk.co.thomasc.steamkit.util.cSharp.events.EventArgs;
import uk.co.thomasc.steamkit.util.cSharp.ip.IPEndPoint;
import uk.co.thomasc.steamkit.util.logging.DebugLog;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class TcpConnection extends Connection {
	final static int MAGIC = 0x31305456; // "VT01"

	boolean isConnected;

	Socket sock;

	//NetworkStream netStream;

	BinaryReader netReader;
	BinaryWriter netWriter;

	Thread netThread;

	/**
	 * Connects to the specified end point.
	 * @param endPoint	The end point.
	 */
	@Override
	public void connect(IPEndPoint endPoint) {
		// if we're connected, disconnect
		disconnect();

		DebugLog.writeLine("TcpConnection", "Connecting to %s...", endPoint);
		Socket socket = null;
		try {
			socket = new Socket(endPoint.getIpAddress(), endPoint.getPort());
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			connectCompleted(socket);
		} catch (final IOException e) {
		};
	}

	void connectCompleted(Socket socket) throws IOException {

		sock = socket;

		if (sock == null || !sock.isConnected()) {
			onDisconnected(EventArgs.Empty);
			return;
		}

		DebugLog.writeLine("TcpConnection", "Connected!");

		isConnected = true;

		//netStream = ;

		netReader = new BinaryReader(sock.getInputStream());
		netWriter = new BinaryWriter(sock.getOutputStream());

		// initialize our network thread
		netThread = new Thread(new NetLoop());
		netThread.setName("TcpConnection Thread");
		netThread.start();

		onConnected(EventArgs.Empty);
	}

	/**
	 * Disconnects this instance.
	 */
	@Override
	public void disconnect() {
		if (!isConnected) {
			return;
		}

		isConnected = false;

		// wait for our network thread to terminate
		try {
			netThread.join();
		} catch (final InterruptedException e) {
		};
		netThread = null;

		cleanup();

		onDisconnected(EventArgs.Empty);
	}

	/**
	 * Sends the specified client net message.
	 * @param clientMsg	The client net message.
	 * @throws IOException 
	 */
	@Override
	public void send(IClientMsg clientMsg) throws IOException {
		if (!isConnected) {
			DebugLog.writeLine("TcpConnection", "Attempting to send client message when not connected: %s", clientMsg.getMsgType());
			return;
		}

		byte[] data = clientMsg.serialize();

		// encrypt outgoing traffic if we need to
		if (NetFilter != null) {
			data = NetFilter.processOutgoing(data);
		}

		synchronized (sock) {
			// write header
			netWriter.write(data.length);
			netWriter.write(TcpConnection.MAGIC);

			netWriter.write(data);

			netWriter.flush();
		}
	}

	class NetLoop implements Runnable {
		/**
		 * Nets the loop.
		 */
		@Override
		public void run() {
			// poll for readable data every 100ms
			//final int POLL_MS = 100; 

			while (true) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e1) {
					e1.printStackTrace();
				}

				if (!isConnected) {
					break;
				}

				boolean canRead = false;
				try {
					canRead = !netReader.isAtEnd();
				} catch (final IOException e) {
				};

				if (!canRead) {
					// nothing to read yet
					continue;
				}

				// read the packet off the network
				readPacket();
			}
		}
	}

	void readPacket() {
		// the tcp packet header is considerably less complex than the udp one
		// it only consists of the packet length, followed by the "VT01" magic
		int packetLen = 0;
		int packetMagic = 0;

		byte[] packData = null;

		try {
			try {
				packetLen = netReader.readInt();
				packetMagic = netReader.readInt();
			} catch (final IOException ex) {
				throw new IOException("Connection lost while reading packet header.", ex);
			}

			if (packetMagic != TcpConnection.MAGIC) {
				throw new IOException("Got a packet with invalid magic!");
			}

			// rest of the packet is the physical data
			packData = netReader.readBytes(packetLen);

			if (packData.length != packetLen) {
				throw new IOException("Connection lost while reading packet payload");
			}

			// decrypt the data off the wire if needed
			if (NetFilter != null) {
				packData = NetFilter.processIncoming(packData);
			}
		} catch (final IOException ex) {
			DebugLog.writeLine("TcpConnection", "Socket exception occurred while reading packet: %s", ex);

			// signal that our connection is dead
			isConnected = false;

			cleanup();

			onDisconnected(EventArgs.Empty);
			return;
		}

		onNetMsgReceived(new NetMsgEventArgs(packData, new IPEndPoint(sock.getInetAddress().getHostAddress(), sock.getPort())));
	}

	void cleanup() {
		// cleanup streams
		/*if ( netReader != null) {
			try {
				netReader.close();
			} catch (IOException e) {};
			netReader = null;
		}

		if (netWriter != null) {
			try {
				netWriter.close();
			} catch (IOException e) {};
			netWriter = null;
		}

		if (netStream != null) {
			netStream.Dispose();
			netStream = null;
		}*/

		if (sock != null) {
			// cleanup socket
			try {
				if (sock.isConnected()) {
					sock.shutdownInput();
					sock.shutdownOutput();
				}
				sock.close();
			} catch (final IOException e) {
			};

			sock = null;
		}
	}

	/**
	 * Gets the local IP.
	 */
	@Override
	public InetAddress getLocalIP() {
		return sock.getLocalAddress();
	}
}
