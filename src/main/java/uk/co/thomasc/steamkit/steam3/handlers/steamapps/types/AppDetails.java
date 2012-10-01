package uk.co.thomasc.steamkit.steam3.handlers.steamapps.types;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.steam3.handlers.steamapps.SteamApps;

/**
 * Represents app request details when calling {@link SteamApps#getAppInfo(int)}.
 */
public final class AppDetails {
	/**
	 * Gets or sets the AppID for this request.
	 */
	public int appID;

	/**
	 * Gets or sets the section flags for this request.
	 */
	public int sectionFlags = 0xFFFF; // request all sections by default

	/**
	 * Gets the Section CRC list for this request.
	 */
	@Getter private final List<Integer> sectionCRC = new ArrayList<Integer>();

	/**
	 * Initializes a new instance of the {@link AppDetails} class.
	 */
	public AppDetails() {

	}
}
