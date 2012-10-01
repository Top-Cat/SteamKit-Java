package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLicenseList;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.License;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired during logon, informing the client of it's available licenses.
 */
public final class LicenseListCallback extends CallbackMsg {
	/**
	 * Gets the result of the message.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the license list.
	 */
	@Getter private final List<License> licenseList = new ArrayList<License>();

	public LicenseListCallback(CMsgClientLicenseList msg) {
		result = EResult.f(msg.getEresult());

		for (final CMsgClientLicenseList.License l : msg.getLicensesList()) {
			licenseList.add(new License(l));
		}
	}
}
