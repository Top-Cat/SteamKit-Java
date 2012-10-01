package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EWorkshopFileAction;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;

/**
 * Represents the details of an enumeration request used for the local user's files.
 */
public final class EnumerationUserDetails {
	/**
	 * Gets or sets the AppID of the workshop to enumerate.
	 */
	public int appId;

	/**
	 * Gets or sets the sort order.
	 * This value is only used by {@link SteamWorkshop#enumeratePublishedFiles(EnumerationDetails)}
	 */
	public int sortOrder;

	/**
	 * Gets or sets the start index.
	 */
	public int startIndex;

	/**
	 * Gets or sets the user action to filter by.
	 * This value is only used by {@link SteamWorkshop#enumeratePublishedFilesByUserAction(EnumerationUserDetails)}.
	 */
	public EWorkshopFileAction userAction;
}
