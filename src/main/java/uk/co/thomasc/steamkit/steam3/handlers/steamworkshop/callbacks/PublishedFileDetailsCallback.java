package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks;

import java.util.Date;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMGetPublishedFileDetailsResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPublishedFileVisibility;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.SteamWorkshop;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is received in response to calling {@link SteamWorkshop#requestPublishedFileDetails(long)}.
 */
public final class PublishedFileDetailsCallback extends CallbackMsg {
	/**
	 * Gets the result.
	 */
	@Getter private final EResult result;

	/**
	 * Gets the file ID.
	 */
	@Getter private final long fileId;

	/**
	 * Gets the SteamID of the creator of this file.
	 */
	@Getter private final SteamID creator;

	/**
	 * Gets the AppID used during creation.
	 */
	@Getter private final int creatorAppId;

	/**
	 * Gets the AppID used during consumption.
	 */
	@Getter private final int consumerAppId;

	/**
	 * Gets the handle for the UGC file this published file represents.
	 */
	@Getter private final long fileUGC;

	/**
	 * Gets the handle for the UGC preview file this published file represents, normally an image or thumbnail.
	 */
	@Getter private final long previewFileUGC;

	/**
	 * Gets the title.
	 */
	@Getter private final String title;

	/**
	 * Gets the description.
	 */
	@Getter private final String description;

	/**
	 * Gets the creation time.
	 */
	@Getter private final Date creationTime;

	/**
	 * Gets the last update time.
	 */
	@Getter private final Date updateTime;

	/**
	 * Gets the visiblity of this file.
	 */
	@Getter private final EPublishedFileVisibility visiblity;

	/**
	 * Gets a value indicating whether this instance is banned.
	 */
	@Getter private final boolean isBanned;

	/**
	 * Gets the tags associated with this file.
	 */
	@Getter private final List<String> tags;

	/**
	 * Gets the name of the file.
	 */
	@Getter private final String fileName;

	/**
	 * Gets the size of the file.
	 */
	@Getter private final int fileSize;

	/**
	 * Gets the size of the preview file.
	 */
	@Getter private final int previewFileSize;

	/**
	 * Gets the URL.
	 */
	@Getter private final String URL;

	public PublishedFileDetailsCallback(CMsgClientUCMGetPublishedFileDetailsResponse msg) {
		result = EResult.f(msg.getEresult());

		fileId = msg.getPublishedFileId();

		creator = new SteamID(msg.getCreatorSteamId());

		creatorAppId = msg.getConsumerAppId();
		consumerAppId = msg.getConsumerAppId();

		fileUGC = msg.getFileHcontent();
		previewFileUGC = msg.getPreviewHcontent();

		title = msg.getTitle();
		description = msg.getDescription();

		creationTime = new Date(msg.getRtime32Created());
		updateTime = new Date(msg.getRtime32Updated());

		visiblity = EPublishedFileVisibility.f(msg.getVisibility());

		isBanned = msg.getBanned();

		tags = msg.getTagList();

		fileName = msg.getFilename();

		fileSize = msg.getFileSize();
		previewFileSize = msg.getPreviewFileSize();

		URL = msg.getUrl();
	}
}
