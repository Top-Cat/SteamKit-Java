package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserActionResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.PublishedFileId;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.EnumerationUserDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.FilePublished;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamWorkshop#enumeratePublishedFilesByUserAction(EnumerationUserDetails)}.
 */
public final class UserActionPublishedFilesCallback extends CallbackMsg {
	/**
	 * Gets the result.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the list of enumerated files.
	 */
	@Getter private final List<FilePublished> files = new ArrayList<FilePublished>();

	/**
	 * Gets the count of total results.
	 */
	@Getter private final int totalResults;

	public UserActionPublishedFilesCallback(CMsgClientUCMEnumeratePublishedFilesByUserActionResponse msg) {
		result = EResult.f(msg.getEresult());

		for (final PublishedFileId f : msg.getPublishedFilesList()) {
			files.add(new FilePublished(f));
		}

		totalResults = msg.getTotalResults();
	}
}
