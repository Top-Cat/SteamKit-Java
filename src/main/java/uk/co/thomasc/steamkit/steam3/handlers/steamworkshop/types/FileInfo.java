package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFilesResponse;

/**
 * Represents the details of a single published file.
 */
public final class FileInfo extends File {
	/**
	 * Gets the number of reports for this file.
	 */
	@Getter private final int reports;

	/**
	 * Gets the score of this file, based on up and down votes.
	 */
	@Getter private final float score;

	/**
	 * Gets the total count of up votes.
	 */
	@Getter private final int upVotes;

	/**
	 * Gets the total count of down votes.
	 */
	@Getter private final int downVotes;

	public FileInfo(CMsgCREEnumeratePublishedFilesResponse.PublishedFileId file) {
		super(file.getPublishedFileId());

		reports = file.getReports();

		score = file.getScore();

		upVotes = file.getVotesFor();
		downVotes = file.getVotesAgainst();
	}
}
