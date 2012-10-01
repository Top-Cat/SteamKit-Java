package uk.co.thomasc.steamkit.steam3.handlers.steamuser.types;

/**
 * The One-Time-Password details for this response.
 */
public class OTPDetails {
	/**
	 * Gets or sets the one-time-password type.
	 */
	public int type;

	/**
	 * Gets or sets the one-time-password identifier.
	 */
	public String identifier;

	/**
	 * Gets or sets the one-time-password value.
	 */
	public int value;

	/**
	 * Gets the OTP shared secret.
	 */
	protected byte[] sharedSecret;

	/**
	 * Gets the OTP time drift.
	 */
	protected int timeDrift;
}
