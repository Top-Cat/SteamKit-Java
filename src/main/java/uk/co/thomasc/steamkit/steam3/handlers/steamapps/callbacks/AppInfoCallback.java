package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoResponse;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.App;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.AppInfoStatus;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamApps#getAppInfo(int)}.
 */
public final class AppInfoCallback extends CallbackMsg {
	/**
	 * Gets the list of apps this response contains.
	 */
	@Getter private final List<App> apps = new ArrayList<App>();

	/**
	 * Gets the number of apps pending in this response.
	 */
	@Getter private final int appsPending;

	public AppInfoCallback(CMsgClientAppInfoResponse msg) {
		for (final CMsgClientAppInfoResponse.App app : msg.getAppsList()) {
			apps.add(new App(app, AppInfoStatus.OK));
		}

		for (final Integer app : msg.getAppsUnknownList()) {
			apps.add(new App(app, AppInfoStatus.Unknown));
		}

		appsPending = msg.getAppsPending();
	}
}
