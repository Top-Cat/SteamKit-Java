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
	@Getter private EResult result;

	/**
	 * Gets the file ID.
	 */
	@Getter private long fileID;

	/**
	 * Gets the SteamID of the creator of this file.
	 */
	@Getter private SteamID creator;

	/**
	 * Gets the AppID used during creation.
	 */
	@Getter private int creatorAppID;

	/**
	 * Gets the AppID used during consumption.
	 */
	@Getter private int consumerAppID;

	/**
	 * Gets the handle for the UGC file this published file represents.
	 */
	@Getter private long fileUGC;

	/**
	 * Gets the handle for the UGC preview file this published file represents, normally an image or thumbnail.
	 */
	@Getter private long previewFileUGC;

	/**
	 * Gets the title.
	 */
	@Getter private String title;

	/**
	 * Gets the description.
	 */
	@Getter private String description;

	/**
	 * Gets the creation time.
	 */
	@Getter private Date creationTime;

	/**
	 * Gets the last update time.
	 */
	@Getter private Date updateTime;

	/**
	 * Gets the visiblity of this file.
	 */
	@Getter private EPublishedFileVisibility visiblity;

	/**
	 * Gets a value indicating whether this instance is banned.
	 */
	@Getter private boolean isBanned;

	/**
	 * Gets the tags associated with this file.
	 */
	@Getter private List<String> tags;

	/**
	 * Gets the name of the file.
	 */
	@Getter private String fileName;

	/**
	 * Gets the size of the file.
	 */
	@Getter private int fileSize;

	/**
	 * Gets the size of the preview file.
	 */
	@Getter private int previewFileSize;

	/**
	 * Gets the URL.
	 */
	@Getter private String URL;

	public PublishedFileDetailsCallback(CMsgClientUCMGetPublishedFileDetailsResponse msg) {
		this.result = EResult.f(msg.getEresult());

		this.fileID = msg.getPublishedFileId();

		this.creator = new SteamID(msg.getCreatorSteamId());

		this.creatorAppID = msg.getConsumerAppId();
		this.consumerAppID = msg.getConsumerAppId();

		this.fileUGC = msg.getFileHcontent();
		this.previewFileUGC = msg.getPreviewHcontent();

		this.title = msg.getTitle();
		this.description = msg.getDescription();

		this.creationTime = new Date(msg.getRtime32Created());
		this.updateTime = new Date(msg.getRtime32Updated());

		this.visiblity = EPublishedFileVisibility.f(msg.getVisibility());

		this.isBanned = msg.getBanned();

		this.tags = msg.getTagList();

		this.fileName = msg.getFilename();

		this.fileSize = msg.getFileSize();
		this.previewFileSize = msg.getPreviewFileSize();

		this.URL = msg.getUrl();
	}
}