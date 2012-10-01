package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUpdateMachineAuth;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is recieved when the backend wants the client to update it's local machine authentication data.
 */
public final class UpdateMachineAuthCallback extends CallbackMsg {

	/**
	 * Gets the sentry file data that should be written.
	 */
	@Getter private final byte[] data;

	/**
	 * Gets the number of bytes to write.
	 */
	@Getter private final int bytesToWrite;

	/**
	 * Gets the offset to write to.
	 */
	@Getter private final int offset;

	/**
	 * Gets the name of the sentry file to write.
	 */
	@Getter private final String fileName;

	/**
	 * Gets the one-time-password details.
	 */
	@Getter private final OTPDetailsCallback oneTimePassword;

	public UpdateMachineAuthCallback(CMsgClientUpdateMachineAuth msg) {
		data = msg.getBytes().toByteArray();

		bytesToWrite = msg.getCubtowrite();
		offset = msg.getOffset();

		fileName = msg.getFilename();

		oneTimePassword = new OTPDetailsCallbackInternal();
		oneTimePassword.type = msg.getOtpType();
		oneTimePassword.identifier = msg.getOtpIdentifier();
		((OTPDetailsCallbackInternal) oneTimePassword).setSharedSecret(msg.getOtpSharedsecret().toByteArray());
		((OTPDetailsCallbackInternal) oneTimePassword).setTimeDrift(msg.getOtpTimedrift());
	}
}
