package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPackageInfoResponse;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.Package;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.PackageStatus;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamApps#getPackageInfo(int)}.
 */
public final class PackageInfoCallback extends CallbackMsg {
	/**
	 * Gets the list of packages this response contains.
	 */
	@Getter private final List<Package> packages = new ArrayList<Package>();

	/**
	 * Gets a count of packages pending in this response.
	 */
	public int packagesPending;

	public PackageInfoCallback(CMsgClientPackageInfoResponse msg) {
		for (final CMsgClientPackageInfoResponse.Package pkg : msg.getPackagesList()) {
			packages.add(new Package(pkg, PackageStatus.OK));
		}

		for (final Integer pkg : msg.getPackagesUnknownList()) {
			packages.add(new Package(pkg, PackageStatus.Unknown));
		}

		packagesPending = msg.getPackagesPending();
	}
}
