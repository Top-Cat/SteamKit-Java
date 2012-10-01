package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetDepotDecryptionKeyResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is recieved in response to calling {@link SteamApps#getDepotDecryptionKey()}.
 */
public final class DepotKeyCallback extends CallbackMsg {
	/**
	 * Gets the result of requesting this encryption key.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the DepotID this encryption key is for.
	 */
	@Getter private final int depotID;

	/**
	 * Gets the encryption key for this depot.
	 */
	@Getter private final byte[] depotKey;

	public DepotKeyCallback(CMsgClientGetDepotDecryptionKeyResponse msg) {
		result = EResult.f(msg.getEresult());
		depotID = msg.getDepotId();
		depotKey = msg.getDepotEncryptionKey().toByteArray();
	}
}
