package uk.co.thomasc.steamkit.steam3.handlers.steamuser.types;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents the details required to log into Steam3 as a user.
 */
@Accessors(fluent = true)
public final class LogOnDetails {
	/**
	 * Gets or sets the username.
	 */
	@Setter public String username = "";

	/**
	 * Gets or sets the password.
	 */
	@Setter public String password = "";

	/**
	 * Gets or sets the Steam Guard auth code used to login. This is the code sent to the user's email.
	 */
	@Setter public String authCode = "";

	/**
	 * Gets or sets the sentry file hash for this logon attempt, or null if no sentry file is available.
	 */
	public byte[] sentryFileHash = null;

	/**
	 * Gets or sets the account instance. 1 for the PC instance or 2 for the Console (PS3) instance.
	 */
	public int accountInstance = 1; // use the default pc steam instance

	/**
	 * Gets or sets a value indicating whether to request the Steam2 ticket.
	 * This is an optional request only needed for Steam2 content downloads.
	 */
	public boolean requestSteam2Ticket = false;

	/**
	 * Initializes a new instance of the {@link LogOnDetails} class.
	 */
	public LogOnDetails() {

	}
}
