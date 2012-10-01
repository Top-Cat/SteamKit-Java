package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFilesResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFilesResponse.PublishedFileId;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.EnumerationDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.FileInfo;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received in response to calling {@link SteamWorkshop#enumeratePublishedFiles(EnumerationDetails)}.
 */
public final class PublishedFilesCallback extends CallbackMsg {
	/**
	 * Gets the result.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the list of enumerated files.
	 */
	@Getter private final List<FileInfo> files = new ArrayList<FileInfo>();

	/**
	 * Gets the count of total results.
	 */
	@Getter private final int totalResults;

	public PublishedFilesCallback(CMsgCREEnumeratePublishedFilesResponse msg) {
		result = EResult.f(msg.getEresult());

		for (final PublishedFileId file : msg.getPublishedFilesList()) {
			files.add(new FileInfo(file));
		}

		totalResults = msg.getTotalResults();
	}
}
