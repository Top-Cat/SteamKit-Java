package uk.co.thomasc.steamkit.types;

import uk.co.thomasc.steamkit.types.ugc.UInt64Handle;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

public final class JobID extends UInt64Handle {

	/**
	 * Represents an invalid JobID.
	 */
	public static final JobID Invalid = new JobID();

	/**
	 * Initializes a new instance of the {@link JobID} class.
	 */
	public JobID() {
		super(BinaryReader.LongMaxValue);
	}

	/**
	 * Initializes a new instance of the {@link JobID} class.
	 * @param jobId	The job ID.
	 */
	public JobID(long jobId) {
		super(jobId);
	}

}
