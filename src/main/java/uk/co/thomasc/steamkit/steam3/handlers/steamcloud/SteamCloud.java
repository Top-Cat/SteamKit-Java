package uk.co.thomasc.steamkit.steam3.handlers.steamcloud;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUFSGetUGCDetails;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUFSGetUGCDetailsResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamcloud.callbacks.UGCDetailsCallback;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;

/**
 * This handler is used for interacting with remote storage and user generated content.
 */
public final class SteamCloud extends ClientMsgHandler {
	/**
	 * Requests details for a specific item of user generated content from the Steam servers.
	 * Results are returned in a {@link UGCDetailsCallback} from a {@link JobCallback}.
	 * @param ugcId	The unique user generated content id.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID requestUGCDetails(long ugcId) {
		final ClientMsgProtobuf<CMsgClientUFSGetUGCDetails.Builder> request = new ClientMsgProtobuf<CMsgClientUFSGetUGCDetails.Builder>(CMsgClientUFSGetUGCDetails.class, EMsg.ClientUFSGetUGCDetails);
		request.setSourceJobID(getClient().getNextJobID());

		request.getBody().setHcontent(ugcId);

		getClient().send(request);

		return request.getSourceJobID();
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case ClientUFSGetUGCDetailsResponse:
				handleUGCDetailsResponse(packetMsg);
				break;
		}
	}

	void handleUGCDetailsResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientUFSGetUGCDetailsResponse.Builder> infoResponse = new ClientMsgProtobuf<CMsgClientUFSGetUGCDetailsResponse.Builder>(CMsgClientUFSGetUGCDetailsResponse.class, packetMsg);

		final UGCDetailsCallback innerCallback = new UGCDetailsCallback(infoResponse.getBody().build());
		final JobCallback<?> callback = new JobCallback<UGCDetailsCallback>(infoResponse.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

}
