package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoChanges;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamApps#getAppChanges()}.
 */
public final class AppChangesCallback extends CallbackMsg {
	/**
	 * Gets the list of AppIDs that have changed since the last change number request.
	 */
	@Getter private final List<Integer> appIDs;

	/**
	 * Gets the current change number.
	 */
	@Getter private final int currentChangeNumber;

	/**
	 * Gets a value indicating whether the backend wishes for the client to perform a full update.
	 */
	public boolean forceFullUpdate;

	public AppChangesCallback(CMsgClientAppInfoChanges msg) {
		appIDs = msg.getAppIDsList();
		currentChangeNumber = msg.getCurrentChangeNumber();

		forceFullUpdate = msg.getForceFullUpdate();
	}
}
