package uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr;

import java.security.InvalidParameterException;

import uk.co.thomasc.steamkit.util.cSharp.events.ActionT;

/**
 * Represents the base object all callbacks are based off.
 */
public abstract class CallbackMsg {

	/**
	 * Initializes a new instance of the {@link CallbackMsg} class.
	 */
	protected CallbackMsg() {
	}

	/**
	 * Determines whether this callback is a certain type.
	 * @param type	The type to check against.
	 * @return true if this callback is the type specified; otherwise, false
	 */
	public <T extends CallbackMsg> boolean isType(Class<T> type) {
		return getClass().equals(type);
	}

	/**
	 * Invokes the specified handler delegate if the callback matches the type parameter.
	 * @param type		The type to check against.
	 * @param handler	The handler to invoke.
	 * @throws InvalidParameterException handler is null.
	 */
	@SuppressWarnings("unchecked")
	public <T extends CallbackMsg> void handle(Class<T> type, ActionT<T> handler) throws InvalidParameterException {
		if (handler == null) {
			throw new InvalidParameterException("handler");
		}

		if (type.isAssignableFrom(this.getClass())) {
			handler.call((T) this);
		}
	}
}
