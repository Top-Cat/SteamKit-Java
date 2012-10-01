package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks;

import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserPublishedFilesResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserPublishedFilesResponse.PublishedFileId;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.EnumerationUserDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.File;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamWorkshop#enumerateUserPublishedFiles(EnumerationUserDetails)}.
 */
public final class UserPublishedFilesCallback extends CallbackMsg {
	/**
	 * Gets the result.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the list of enumerated files.
	 */
	@Getter private List<File> files;

	/**
	 * Gets the count of total results.
	 */
	@Getter private final int totalResults;

	public UserPublishedFilesCallback(CMsgClientUCMEnumerateUserPublishedFilesResponse msg) {
		result = EResult.f(msg.getEresult());

		for (final PublishedFileId f : msg.getPublishedFilesList()) {
			files.add(new File(f));
		}

		totalResults = msg.getTotalResults();
	}
}
