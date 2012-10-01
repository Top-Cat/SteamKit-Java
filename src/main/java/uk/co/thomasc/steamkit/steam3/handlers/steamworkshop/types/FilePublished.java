package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import java.util.Date;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserActionResponse;

/**
 * Represents the details of a single published file.
 */
public class FilePublished extends File {
	/**
	 * Gets the timestamp of this file.
	 */
	@Getter private final Date timeStamp;

	public FilePublished(CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.PublishedFileId file) {
		super(file.getPublishedFileId());

		timeStamp = new Date(file.getRtimeTimeStamp());
	}
}
