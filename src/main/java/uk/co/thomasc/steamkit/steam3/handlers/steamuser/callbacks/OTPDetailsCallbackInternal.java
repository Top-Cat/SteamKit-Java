package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

/**
 * Represents various one-time-password details.
 */
public final class OTPDetailsCallbackInternal extends OTPDetailsCallback {

	public void setSharedSecret(byte[] value) {
		sharedSecret = value;
	}

	public void setTimeDrift(int value) {
		timeDrift = value;
	}

}
