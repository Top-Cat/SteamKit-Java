package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import java.util.Date;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserActionResponse;

import lombok.Getter;

/**
 * Represents the details of a single published file.
 */
public class FilePublished extends File {
	/**
	 * Gets the timestamp of this file.
	 */
	@Getter private Date timeStamp;

	public FilePublished(CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.PublishedFileId file) {
		super(file.getPublishedFileId());
		
		this.timeStamp = new Date(file.getRtimeTimeStamp());
	}
}