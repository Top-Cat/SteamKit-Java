package uk.co.thomasc.steamkit.steam3.steamclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.openmbean.InvalidOpenTypeException;

import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.Msg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgChannelEncryptResult;
import uk.co.thomasc.steamkit.steam3.CMClient;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.handlers.steamcloud.SteamCloud;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.SteamFriends;
import uk.co.thomasc.steamkit.steam3.handlers.steamgamecoordinator.SteamGameCoordinator;
import uk.co.thomasc.steamkit.steam3.handlers.steamgameserver.SteamGameServer;
import uk.co.thomasc.steamkit.steam3.handlers.steammasterserver.SteamMasterServer;
import uk.co.thomasc.steamkit.steam3.handlers.steamtrading.SteamTrading;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.SteamUser;
import uk.co.thomasc.steamkit.steam3.handlers.steamuserstats.SteamUserStats;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.steam3.steamclient.callbacks.ConnectedCallback;
import uk.co.thomasc.steamkit.steam3.steamclient.callbacks.DisconnectedCallback;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.cSharp.ip.ProtocolType;

/**
 * Represents a single client that connects to the Steam3 network.
 * This class is also responsible for handling the registration of client message handlers and callbacks.
 */
public final class SteamClient extends CMClient {
	Map<Class<? extends ClientMsgHandler>, ClientMsgHandler> handlers;

	AtomicLong currentJobId = new AtomicLong();

	Object callbackLock = new Object();
	Queue<CallbackMsg> callbackQueue;

	public SteamClient() {
		this(ProtocolType.Tcp);
	}

	/**
	 * Initializes a new instance of the {@link SteamClient} class with a specific connection type.
	 * @param type	The connection type to use.
	 */
	public SteamClient(ProtocolType type) {
		super(type);
		callbackQueue = new LinkedList<CallbackMsg>();
		handlers = new HashMap<Class<? extends ClientMsgHandler>, ClientMsgHandler>();

		// add this library's handlers
		addHandler(new SteamUser());
		addHandler(new SteamFriends());
		addHandler(new SteamApps());
		addHandler(new SteamGameCoordinator());
		addHandler(new SteamGameServer());
		addHandler(new SteamUserStats());
		addHandler(new SteamMasterServer());
		addHandler(new SteamCloud());
		addHandler(new SteamWorkshop());
		addHandler(new SteamTrading());
	}

	/**
	 * Adds a new handler to the internal list of message handlers.
	 * @param handler	The handler to add.
	 * @throws IllegalArgumentException A handler of that type is already registered.
	 */
	public void addHandler(ClientMsgHandler handler) throws InvalidOpenTypeException {
		if (handlers.containsKey(handler.getClass())) {
			throw new IllegalArgumentException(String.format("A handler of type \"%s\" is already registered.", handler.getClass()));
		}

		handlers.put(handler.getClass(), handler);
		handler.setup(this);
	}

	/**
	 * Removes a registered handler by name.
	 * @param handler	The handler name to remove.
	 */
	public void removeHandler(Class<? extends ClientMsgHandler> handler) {
		if (!handlers.containsKey(handler.getClass())) {
			return;
		}

		handlers.remove(handler.getClass());
	}

	/**
	 * Removes a registered handler.
	 * @param handler	The handler to remove.
	 */
	public void removeHandler(ClientMsgHandler handler) {
		removeHandler(handler.getClass());
	}

	/**
	 * Returns a registered handler.
	 * T - The type of the handler to cast to. Must derive from ClientMsgHandler.
	 * @return A registered handler on success, or null if the handler could not be found.
	 */
	@SuppressWarnings("unchecked")
	public <T extends ClientMsgHandler> T getHandler(Class<T> type) {
		if (handlers.containsKey(type)) {
			return (T) handlers.get(type);
		}

		return null;
	}

	/**
	 * Gets the next callback object in the queue.
	 * This function does not dequeue the callback, you must call FreeLastCallback after processing it.
	 * @return The next callback in the queue, or null if no callback is waiting.
	 */
	public CallbackMsg getCallback() {
		return getCallback(false);
	}

	/**
	 * Gets the next callback object in the queue, and optionally frees it.
	 * @param freeLast	if set to true this function also frees the last callback if one existed.
	 * @return The next callback in the queue, or null if no callback is waiting.
	 */
	public CallbackMsg getCallback(boolean freeLast) {
		synchronized (callbackLock) {
			if (callbackQueue.size() > 0) {
				return freeLast ? callbackQueue.poll() : callbackQueue.peek();
			}
		}

		return null;
	}

	/**
	 * Blocks the calling thread until a callback object is posted to the queue.
	 * This function does not dequeue the callback, you must call FreeLastCallback after processing it.
	 * @return The callback object from the queue.
	 */
	public CallbackMsg waitForCallback() {
		return waitForCallback(false);
	}

	/**
	 * Blocks the calling thread until a callback object is posted to the queue, or null after the timeout has elapsed.
	 * This function does not dequeue the callback, you must call FreeLastCallback after processing it.
	 * @param timeout	The length of time to block.
	 * @return A callback object from the queue if a callback has been posted, or null if the timeout has elapsed.
	 */
	public CallbackMsg waitForCallback(long timeout) {
		synchronized (callbackLock) {
			if (callbackQueue.size() == 0) {
				try {
					callbackLock.wait(timeout);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				if (callbackQueue.size() == 0) {
					return null;
				}
			}

			return callbackQueue.peek();
		}
	}

	/**
	 * Blocks the calling thread until a callback object is posted to the queue, and optionally frees it.
	 * @param freeLast	if set to true this function also frees the last callback.
	 * @return The callback object from the queue.
	 */
	public CallbackMsg waitForCallback(boolean freeLast) {
		synchronized (callbackLock) {
			if (callbackQueue.size() == 0) {
				try {
					callbackLock.wait();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}

			return freeLast ? callbackQueue.poll() : callbackQueue.peek();
		}
	}

	/**
	 * Blocks the calling thread until a callback object is posted to the queue, and optionally frees it.
	 * @param freeLast	if set to true this function also frees the last callback.
	 * @param timeout	The length of time to block.
	 * @return A callback object from the queue if a callback has been posted, or null if the timeout has elapsed.
	 */
	public CallbackMsg waitForCallback(boolean freeLast, long timeout) {
		synchronized (callbackLock) {
			if (callbackQueue.size() == 0) {
				try {
					callbackLock.wait(timeout);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				if (callbackQueue.size() == 0) {
					return null;
				}
			}

			return freeLast ? callbackQueue.poll() : callbackQueue.peek();
		}
	}

	/**
	 * Frees the last callback in the queue.
	 */
	public void freeLastCallback() {
		synchronized (callbackLock) {
			if (callbackQueue.size() == 0) {
				return;
			}

			callbackQueue.poll();
		}
	}

	/**
	 * Posts a callback to the queue. This is normally used directly by client message handlers.
	 * @param msg	The message.
	 */
	public void postCallback(CallbackMsg msg) {
		if (msg == null) {
			return;
		}

		synchronized (callbackLock) {
			callbackQueue.offer(msg);
			callbackLock.notify();
		}
	}

	/**
	 * Returns the next available JobID for job based messages.
	 * This function is thread-safe.
	 * @return The next available JobID.
	 */
	public JobID getNextJobID() {
		return new JobID(currentJobId.incrementAndGet());
	}

	/**
	 * Called when a client message is received from the network.
	 * @param packetMsg	The packet message.
	 * @throws IOException 
	 */
	@Override
	protected void onClientMsgReceived(IPacketMsg packetMsg) throws IOException {
		// let the underlying CMClient handle this message first
		super.onClientMsgReceived(packetMsg);

		if (packetMsg.getMsgType() == EMsg.ChannelEncryptResult) {
			handleEncryptResult(packetMsg); // we're interested in this client message to post the connected callback
		}

		// pass along the clientmsg to all registered handlers
		final Iterator<ClientMsgHandler> it = handlers.values().iterator();
		while (it.hasNext()) {
			it.next().handleMsg(packetMsg);
		}
	}

	/**
	 * Called when the client is physically disconnected from Steam3.
	 */
	@Override
	protected void onClientDisconnected(boolean newconnection) {
		postCallback(new DisconnectedCallback(newconnection));
	}

	/**
	 * Called when the client is connected to Steam3 and is ready to send messages.
	 */
	@Override
	protected void onClientConnected() {
		postCallback(new ConnectedCallback(EResult.OK));
	}

	void handleEncryptResult(IPacketMsg packetMsg) { // Not an override, I know :S
		final Msg<MsgChannelEncryptResult> encResult = new Msg<MsgChannelEncryptResult>(packetMsg, MsgChannelEncryptResult.class);

		postCallback(new ConnectedCallback(encResult.getBody()));
	}

}
