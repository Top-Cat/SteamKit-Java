package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import uk.co.thomasc.steamkit.steam3.handlers.steamuser.types.OTPDetails;

/**
 * Represents various one-time-password details.
 */
public class OTPDetailsCallback extends OTPDetails {

	public byte[] getSharedSecret() {
		return sharedSecret;
	}

	public int getTimeDrift() {
		return timeDrift;
	}

}
