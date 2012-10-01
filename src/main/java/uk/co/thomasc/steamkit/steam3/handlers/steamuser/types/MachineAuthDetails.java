package uk.co.thomasc.steamkit.steam3.handlers.steamuser.types;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks.UpdateMachineAuthCallback;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;

/**
 * Represents details required to complete a machine auth request.
 */
public final class MachineAuthDetails {
	/**
	 * Gets or sets the target Job ID for the request.
	 * This is provided in the {@link JobCallback} for a {@link UpdateMachineAuthCallback}.
	 */
	public long jobId;

	/**
	 * Gets or sets the result of updating the machine auth.
	 */
	public EResult result;

	/**
	 * Gets or sets the number of bytes written for the sentry file.
	 */
	public int bytesWritten;

	/**
	 * Gets or sets the offset within the sentry file that was written.
	 */
	public int offset;

	/**
	 * Gets or sets the filename of the sentry file that was written.
	 */
	public String fileName;

	/**
	 * Gets or sets the size of the sentry file.
	 */
	public int fileSize;

	/**
	 * Gets or sets the last error that occurred while writing the sentry file, or 0 if no error occurred.
	 */
	public int lastError;

	/**
	 * Gets or sets the SHA-1 hash of the sentry file.
	 */
	public byte[] sentryFileHash;

	/**
	 * Gets or sets the one-time-password details.
	 */
	public OTPDetails oneTimePassword = new OTPDetails();

	/**
	 * Initializes a new instance of the {@link MachineAuthDetails} class.
	 */
	public MachineAuthDetails() {

	}
}
