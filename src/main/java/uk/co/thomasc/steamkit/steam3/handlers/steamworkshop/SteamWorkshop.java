package uk.co.thomasc.steamkit.steam3.handlers.steamworkshop;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFiles;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgCREEnumeratePublishedFilesResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserAction;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumeratePublishedFilesByUserActionResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserPublishedFiles;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserPublishedFilesResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserSubscribedFiles;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMEnumerateUserSubscribedFilesResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMGetPublishedFileDetails;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUCMGetPublishedFileDetailsResponse;

import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks.PublishedFileDetailsCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks.PublishedFilesCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks.UserActionPublishedFilesCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks.UserPublishedFilesCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.callbacks.UserSubscribedFilesCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.EnumerationDetails;
import uk.co.thomasc.steamkit.steam3.handlers.steamworkshop.types.EnumerationUserDetails;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;

/**
 * This handler is used for requesting files published on the Steam Workshop.
 */
public final class SteamWorkshop extends ClientMsgHandler {
	/**
	 * Requests details for a given published workshop file.
	 * Requests details for a given published workshop file.
	 * @param publishedFileId	The file ID being requested.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID requestPublishedFileDetails(long publishedFileId) {
		ClientMsgProtobuf<CMsgClientUCMGetPublishedFileDetails.Builder> request = new ClientMsgProtobuf<CMsgClientUCMGetPublishedFileDetails.Builder>(EMsg.ClientUCMGetPublishedFileDetails, CMsgClientUCMGetPublishedFileDetails.class);
		request.setSourceJobID(getClient().getNextJobID());

		request.getBody().setPublishedFileId(publishedFileId);

		getClient().send(request);

		return request.getSourceJobID();
	}

	/**
	 * Enumerates the list of published files for the current logged in user.
	 * Results are returned in a {@link UserPublishedFilesCallback} from a {@link JobCallback}.
	 * @param details	The specific details of the request.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID enumerateUserPublishedFiles(EnumerationUserDetails details) {
		ClientMsgProtobuf<CMsgClientUCMEnumerateUserPublishedFiles.Builder> enumRequest = new ClientMsgProtobuf<CMsgClientUCMEnumerateUserPublishedFiles.Builder>(EMsg.ClientUCMEnumerateUserPublishedFiles, CMsgClientUCMEnumerateUserPublishedFiles.class);
		enumRequest.setSourceJobID(getClient().getNextJobID());

		enumRequest.getBody().setAppId(details.appID);
		enumRequest.getBody().setSortOrder(details.sortOrder);
		enumRequest.getBody().setStartIndex(details.startIndex);

		getClient().send(enumRequest);

		return enumRequest.getSourceJobID();
	}

	/**
	 * Enumerates the list of subscribed files for the current logged in user.
	 * Results are returned in a {@link UserSubscribedFilesCallback} from a {@link JobCallback}.
	 * @param details	The specific details of the request.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID enumerateUserSubscribedFiles(EnumerationUserDetails details) {
		ClientMsgProtobuf<CMsgClientUCMEnumerateUserSubscribedFiles.Builder> enumRequest = new ClientMsgProtobuf<CMsgClientUCMEnumerateUserSubscribedFiles.Builder>(EMsg.ClientUCMEnumerateUserSubscribedFiles, CMsgClientUCMEnumerateUserSubscribedFiles.class);
		enumRequest.setSourceJobID(getClient().getNextJobID());

		enumRequest.getBody().setAppId(details.appID);
		enumRequest.getBody().setStartIndex(details.startIndex);

		getClient().send(enumRequest);

		return enumRequest.getSourceJobID();
	}

	/**
	 * Enumerates the list of published files for the current logged in user based on user action.
	 * Results are returned in a {@link UserActionPublishedFilesCallback} from a {@link JobCallback}.
	 * @param details	The specific details of the request.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID enumeratePublishedFilesByUserAction(EnumerationUserDetails details) {
		ClientMsgProtobuf<CMsgClientUCMEnumeratePublishedFilesByUserAction.Builder> enumRequest = new ClientMsgProtobuf< CMsgClientUCMEnumeratePublishedFilesByUserAction.Builder>(EMsg.ClientUCMEnumeratePublishedFilesByUserAction, CMsgClientUCMEnumeratePublishedFilesByUserAction.class);
		enumRequest.setSourceJobID(getClient().getNextJobID());

		enumRequest.getBody().setAction(details.userAction.v());
		enumRequest.getBody().setAppId(details.appID);
		enumRequest.getBody().setStartIndex(details.startIndex);

		getClient().send(enumRequest);

		return enumRequest.getSourceJobID();
	}

	/**
	 * Enumerates the list of all published files on the Steam workshop.
	 * Results are returned in a {@link PublishedFilesCallback} from a {@link JobCallback}.
	 * @param details	The specific details of the request.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID enumeratePublishedFiles(EnumerationDetails details) {
		ClientMsgProtobuf<CMsgCREEnumeratePublishedFiles.Builder> enumRequest = new ClientMsgProtobuf<CMsgCREEnumeratePublishedFiles.Builder>(EMsg.CREEnumeratePublishedFiles, CMsgCREEnumeratePublishedFiles.class);
		enumRequest.setSourceJobID(getClient().getNextJobID());

		enumRequest.getBody().setAppId(details.appID);

		enumRequest.getBody().setQueryType(details.type.v());

		enumRequest.getBody().setStartIndex(details.startIndex);

		enumRequest.getBody().setDays(details.days);
		enumRequest.getBody().setCount(details.count);

		enumRequest.getBody().getTagsList().addAll(details.getTags());
		enumRequest.getBody().getUserTagsList().addAll(details.getUserTags());

		getClient().send(enumRequest);

		return enumRequest.getSourceJobID();
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case CREEnumeratePublishedFilesResponse:
				handleEnumPublishedFiles(packetMsg);
				break;
			case ClientUCMEnumerateUserPublishedFilesResponse:
				handleEnumUserPublishedFiles(packetMsg);
				break;
			case ClientUCMEnumerateUserSubscribedFilesResponse:
				handleEnumUserSubscribedFiles(packetMsg);
				break;
			case ClientUCMEnumeratePublishedFilesByUserActionResponse:
				handleEnumPublishedFilesByAction(packetMsg);
				break;
			case ClientUCMGetPublishedFileDetailsResponse:
				handlePublishedFileDetails(packetMsg);
				break;
		}
	}

	void handleEnumPublishedFiles(IPacketMsg packetMsg) {
		ClientMsgProtobuf<CMsgCREEnumeratePublishedFilesResponse.Builder> response = new ClientMsgProtobuf<CMsgCREEnumeratePublishedFilesResponse.Builder>(packetMsg, CMsgCREEnumeratePublishedFilesResponse.class);

		PublishedFilesCallback innerCallback = new PublishedFilesCallback(response.getBody().build());
		JobCallback<?> callback = new JobCallback<PublishedFilesCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleEnumUserPublishedFiles(IPacketMsg packetMsg) {
		ClientMsgProtobuf<CMsgClientUCMEnumerateUserPublishedFilesResponse.Builder> response = new ClientMsgProtobuf<CMsgClientUCMEnumerateUserPublishedFilesResponse.Builder>(packetMsg, CMsgClientUCMEnumerateUserPublishedFilesResponse.class);

		UserPublishedFilesCallback innerCallback = new UserPublishedFilesCallback(response.getBody().build());
		JobCallback<?> callback = new JobCallback<UserPublishedFilesCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleEnumUserSubscribedFiles(IPacketMsg packetMsg) {
		ClientMsgProtobuf<CMsgClientUCMEnumerateUserSubscribedFilesResponse.Builder> response = new ClientMsgProtobuf<CMsgClientUCMEnumerateUserSubscribedFilesResponse.Builder>(packetMsg, CMsgClientUCMEnumerateUserSubscribedFilesResponse.class);

		UserSubscribedFilesCallback innerCallback = new UserSubscribedFilesCallback(response.getBody().build());
		JobCallback<?> callback = new JobCallback<UserSubscribedFilesCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleEnumPublishedFilesByAction(IPacketMsg packetMsg) {
		ClientMsgProtobuf<CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.Builder> response = new ClientMsgProtobuf<CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.Builder>(packetMsg, CMsgClientUCMEnumeratePublishedFilesByUserActionResponse.class);

		UserActionPublishedFilesCallback innerCallback = new UserActionPublishedFilesCallback(response.getBody().build());
		JobCallback<?> callback = new JobCallback<UserActionPublishedFilesCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handlePublishedFileDetails(IPacketMsg packetMsg) {
		ClientMsgProtobuf<CMsgClientUCMGetPublishedFileDetailsResponse.Builder> details = new ClientMsgProtobuf<CMsgClientUCMGetPublishedFileDetailsResponse.Builder>(packetMsg, CMsgClientUCMGetPublishedFileDetailsResponse.class);

		PublishedFileDetailsCallback innerCallback = new PublishedFileDetailsCallback(details.getBody().build());
		JobCallback<?> callback = new JobCallback<PublishedFileDetailsCallback>(new JobID(packetMsg.getTargetJobID()), innerCallback);
		getClient().postCallback(callback);
	}
}