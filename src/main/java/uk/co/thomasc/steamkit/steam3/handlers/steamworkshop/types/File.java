package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserPublishedFilesResponse;

import lombok.Getter;

/**
 * Represents the details of a single published file.
 */
public class File {
	/**
	 * Gets the file ID.
	 */
	@Getter private long fileID;

	public File(CMsgClientUCMEnumerateUserPublishedFilesResponse.PublishedFileId file) {
		this.fileID = file.getPublishedFileId();
	}
	
	public File(long fileId) {
		this.fileID = fileId;
	}
}