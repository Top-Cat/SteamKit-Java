package uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr;

import lombok.Getter;

import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.logging.Debug;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * This callback is received when a job related operation on the backend has completed, or a client operation should begin.
 * @param <T> The inner callback this job represents.
 */
public final class JobCallback<T extends CallbackMsg> extends BaseJobCallback {
	/**
	 * Gets the inner callback message for this job.
	 */
	@Getter private final T callback;

	public JobCallback(JobID jobId, T callback) {
		super(jobId.getValue());
		Debug.Assert(jobId.getValue() != BinaryReader.LongMaxValue, "JobCallback used for non job based callback!");

		callbackType = callback.getClass();
		this.callback = callback;
	}
}
