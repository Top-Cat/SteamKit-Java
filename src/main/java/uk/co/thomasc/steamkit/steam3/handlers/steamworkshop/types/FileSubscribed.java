package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import java.util.Date;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserSubscribedFilesResponse;

import lombok.Getter;

/**
 * Represents the details of a single published file.
 */
public class FileSubscribed extends File {
	/**
	 * Gets the time this file was subscribed to.
	 */
	@Getter private Date timeSubscribed;

	public FileSubscribed(CMsgClientUCMEnumerateUserSubscribedFilesResponse.PublishedFileId file) {
		super(file.getPublishedFileId());
		
		this.timeSubscribed = new Date(file.getRtime32Subscribed());
	}
}