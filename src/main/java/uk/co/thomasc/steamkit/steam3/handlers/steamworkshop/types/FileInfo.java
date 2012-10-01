package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFilesResponse;

import lombok.Getter;

/**
 * Represents the details of a single published file.
 */
public final class FileInfo extends File {
	/**
	 * Gets the number of reports for this file.
	 */
	@Getter private int reports;

	/**
	 * Gets the score of this file, based on up and down votes.
	 */
	@Getter private float score;

	/**
	 * Gets the total count of up votes.
	 */
	@Getter private int upVotes;

	/**
	 * Gets the total count of down votes.
	 */
	@Getter private int downVotes;

	public FileInfo(CMsgCREEnumeratePublishedFilesResponse.PublishedFileId file) {
		super(file.getPublishedFileId());

		this.reports = file.getReports();

		this.score = file.getScore();

		this.upVotes = file.getVotesFor();
		this.downVotes = file.getVotesAgainst();
	}
}