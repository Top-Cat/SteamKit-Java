package uk.co.thomasc.steamkit.steam3.handlers.steamapps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import uk.co.thomasc.steamkit.base.ClientMsg;
import uk.co.thomasc.steamkit.base.ClientMsgProtobuf;
import uk.co.thomasc.steamkit.base.IPacketMsg;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoChanges;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoRequest;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoUpdate;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGameConnectTokens;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetAppOwnershipTicket;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetAppOwnershipTicketResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetDepotDecryptionKey;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGetDepotDecryptionKeyResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLicenseList;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPackageInfoRequest;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPackageInfoResponse;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientVACBanStatus;
import uk.co.thomasc.steamkit.steam3.handlers.ClientMsgHandler;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.AppChangesCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.AppInfoCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.AppOwnershipTicketCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.DepotKeyCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.GameConnectTokensCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.LicenseListCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.PackageInfoCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks.VACStatusCallback;
import uk.co.thomasc.steamkit.steam3.handlers.steamapps.types.AppDetails;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.JobCallback;
import uk.co.thomasc.steamkit.types.JobID;
import uk.co.thomasc.steamkit.util.logging.Debug;

/**
 * This handler is used for interacting with apps and packages on the Steam network.
 */
public final class SteamApps extends ClientMsgHandler {

	/**
	 * Requests an app ownership ticket for the specified AppID.
	 * Results are returned in a {@link AppOwnershipTicketCallback} callback.
	 * @param appid	The appid to request the ownership ticket of.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getAppOwnershipTicket(int appid) {
		final ClientMsgProtobuf<CMsgClientGetAppOwnershipTicket.Builder> request = new ClientMsgProtobuf<CMsgClientGetAppOwnershipTicket.Builder>(CMsgClientGetAppOwnershipTicket.class, EMsg.ClientGetAppOwnershipTicket);
		request.setSourceJobID(getClient().getNextJobID());

		request.getBody().setAppId(appid);

		getClient().send(request);

		return request.getSourceJobID();
	}

	/**
	 * Requests app information for a single app. Use the overload for requesting information on a batch of apps.
	 * Results are returned in a {@link AppInfoCallback} callback.
	 * @param app				The app to request information for.
	 * @param supportsBatches	if set to true, the request supports batches.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getAppInfo(AppDetails app, boolean supportsBatches) {
		return getAppInfo(Arrays.asList(app), supportsBatches);
	}

	public JobID getAppInfo(AppDetails app) {
		return getAppInfo(app, false);
	}

	/**
	 * Requests app information for a single app. Use the overload for requesting information on a batch of apps.
	 * Results are returned in a {@link AppInfoCallback} callback.
	 * @param app				The app to request information for.
	 * @param supportsBatches	if set to true, the request supports batches.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getAppInfo(int app, boolean supportsBatches) {
		return getAppInfoI(Arrays.asList(app), supportsBatches);
	}

	public JobID getAppInfo(int app) {
		return getAppInfo(app, false);
	}

	/**
	 * Requests app information for a list of apps.
	 * Results are returned in a {@link AppInfoCallback} callback.
	 * @param apps				The apps to request information for.
	 * @param supportsBatches	if set to true, the request supports batches.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getAppInfoI(Collection<Integer> apps, boolean supportsBatches) {
		final List<AppDetails> list = new ArrayList<AppDetails>();
		for (final int i : apps) {
			final AppDetails ad = new AppDetails();
			ad.appID = i;
			list.add(ad);
		}
		return getAppInfo(list, supportsBatches);
	}

	public JobID getAppInfoI(Collection<Integer> apps) {
		return getAppInfoI(apps, false);
	}

	/**
	 * Requests app information for a list of apps.
	 * Results are returned in a {@link AppInfoCallback} callback.
	 * @param apps				The apps to request information for.
	 * @param supportsBatches	if set to true, the request supports batches.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getAppInfo(Collection<AppDetails> apps, boolean supportsBatches) {
		final ClientMsgProtobuf<CMsgClientAppInfoRequest.Builder> request = new ClientMsgProtobuf<CMsgClientAppInfoRequest.Builder>(CMsgClientAppInfoRequest.class, EMsg.ClientAppInfoRequest);
		request.setSourceJobID(getClient().getNextJobID());

		for (final AppDetails ad : apps) {
			final CMsgClientAppInfoRequest.App.Builder app = CMsgClientAppInfoRequest.App.newBuilder().setAppId(ad.appID).setSectionFlags(ad.sectionFlags);
			app.getSectionCRCList().addAll(ad.getSectionCRC());
			request.getBody().getAppsList().add(app.build());
		}

		request.getBody().setSupportsBatches(supportsBatches);

		getClient().send(request);

		return new JobID(request.getSessionID());
	}

	public JobID getAppInfo(Collection<AppDetails> apps) {
		return getAppInfo(apps, false);
	}

	/**
	 * Requests package information for a single package. Use the overload for requesting information on a batch of packages.
	 * Results are returned in a {@link PackageInfoCallback} callback.
	 * @param packageId		The package id to request information for.
	 * @param metaDataOnly	if set to true, request metadata only.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getPackageInfo(int packageId, boolean metaDataOnly) {
		return getPackageInfo(Arrays.asList(packageId), metaDataOnly);
	}

	public JobID getPackageInfo(int packageId) {
		return getPackageInfo(packageId, false);
	}

	/**
	 * Requests package information for a list of packages.
	 * Results are returned in a {@link PackageInfoCallback} callback.
	 * @param packageId		The packages to request information for.
	 * @param metaDataOnly	if set to true to request metadata only.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getPackageInfo(Collection<Integer> packageId, boolean metaDataOnly) {
		final ClientMsgProtobuf<CMsgClientPackageInfoRequest.Builder> request = new ClientMsgProtobuf<CMsgClientPackageInfoRequest.Builder>(CMsgClientPackageInfoRequest.class, EMsg.ClientPackageInfoRequest);

		request.setSourceJobID(getClient().getNextJobID());

		request.getBody().getPackageIdsList().addAll(packageId);
		request.getBody().setMetaDataOnly(metaDataOnly);

		getClient().send(request);

		return request.getSourceJobID();
	}

	public JobID getPackageInfo(Collection<Integer> packageId) {
		return getPackageInfo(packageId, false);
	}

	/**
	 * Requests a list of app changes since the last provided change number value.
	 * Results are returned in a {@link AppChangesCallback} callback.
	 * @param lastChangeNumber	The last change number value.
	 * @param sendChangelist	if set to true, request a change list.
	 */
	public void getAppChanges(int lastChangeNumber, boolean sendChangelist) {
		final ClientMsgProtobuf<CMsgClientAppInfoUpdate.Builder> request = new ClientMsgProtobuf<CMsgClientAppInfoUpdate.Builder>(CMsgClientAppInfoUpdate.class, EMsg.ClientAppInfoUpdate);

		request.getBody().setLastChangenumber(lastChangeNumber);
		request.getBody().setSendChangelist(sendChangelist);

		getClient().send(request);
	}

	public void getAppChanges(int lastChangeNumber) {
		getAppChanges(lastChangeNumber, true);
	}

	public void getAppChanges() {
		getAppChanges(0);
	}

	/**
	 * Request the depot decryption key for a specified DepotID.
	 * Results are returned in a {@link DepotKeyCallback} callback.
	 * 
	 * @param depotid	The DepotID to request a decryption key for.
	 * @param appid		The AppID to request the decryption key for.
	 * @return The Job ID of the request. This can be used to find the appropriate {@link JobCallback}.
	 */
	public JobID getDepotDecryptionKey(int depotid, int appid) {
		final ClientMsgProtobuf<CMsgClientGetDepotDecryptionKey.Builder> request = new ClientMsgProtobuf<CMsgClientGetDepotDecryptionKey.Builder>(CMsgClientGetDepotDecryptionKey.class, EMsg.ClientGetDepotDecryptionKey);
		request.setSourceJobID(getClient().getNextJobID());

		request.getBody().setDepotId(depotid);
		request.getBody().setAppId(appid);

		getClient().send(request);

		return request.getSourceJobID();
	}

	public JobID getDepotDecryptionKey(int depotid) {
		return getDepotDecryptionKey(depotid, 0);
	}

	public JobID getDepotDecryptionKey() {
		return getDepotDecryptionKey(0);
	}

	/**
	 * Handles a client message. This should not be called directly.
	 */
	@Override
	public void handleMsg(IPacketMsg packetMsg) {
		switch (packetMsg.getMsgType()) {
			case ClientLicenseList:
				handleLicenseList(packetMsg);
				break;
			case ClientGameConnectTokens:
				handleGameConnectTokens(packetMsg);
				break;
			case ClientVACBanStatus:
				handleVACBanStatus(packetMsg);
				break;
			case ClientGetAppOwnershipTicketResponse:
				handleAppOwnershipTicketResponse(packetMsg);
				break;
			case ClientAppInfoResponse:
				handleAppInfoResponse(packetMsg);
				break;
			case ClientPackageInfoResponse:
				handlePackageInfoResponse(packetMsg);
				break;
			case ClientAppInfoChanges:
				handleAppInfoChanges(packetMsg);
				break;
			case ClientGetDepotDecryptionKeyResponse:
				handleDepotKeyResponse(packetMsg);
				break;
		}
	}

	void handleAppOwnershipTicketResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientGetAppOwnershipTicketResponse.Builder> ticketResponse = new ClientMsgProtobuf<CMsgClientGetAppOwnershipTicketResponse.Builder>(CMsgClientGetAppOwnershipTicketResponse.class, packetMsg);

		final AppOwnershipTicketCallback innerCallback = new AppOwnershipTicketCallback(ticketResponse.getBody().build());
		final JobCallback<?> callback = new JobCallback<AppOwnershipTicketCallback>(ticketResponse.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleAppInfoResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientAppInfoResponse.Builder> infoResponse = new ClientMsgProtobuf<CMsgClientAppInfoResponse.Builder>(CMsgClientAppInfoResponse.class, packetMsg);

		final AppInfoCallback innerCallback = new AppInfoCallback(infoResponse.getBody().build());
		final JobCallback<?> callback = new JobCallback<AppInfoCallback>(infoResponse.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handlePackageInfoResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientPackageInfoResponse.Builder> response = new ClientMsgProtobuf<CMsgClientPackageInfoResponse.Builder>(CMsgClientPackageInfoResponse.class, packetMsg);

		final PackageInfoCallback innerCallback = new PackageInfoCallback(response.getBody().build());
		final JobCallback<?> callback = new JobCallback<PackageInfoCallback>(response.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleAppInfoChanges(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientAppInfoChanges.Builder> changes = new ClientMsgProtobuf<CMsgClientAppInfoChanges.Builder>(CMsgClientAppInfoChanges.class, packetMsg);

		final AppChangesCallback callback = new AppChangesCallback(changes.getBody().build());
		getClient().postCallback(callback);
	}

	void handleDepotKeyResponse(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientGetDepotDecryptionKeyResponse.Builder> keyResponse = new ClientMsgProtobuf<CMsgClientGetDepotDecryptionKeyResponse.Builder>(CMsgClientGetDepotDecryptionKeyResponse.class, packetMsg);

		final DepotKeyCallback innerCallback = new DepotKeyCallback(keyResponse.getBody().build());
		final JobCallback<?> callback = new JobCallback<DepotKeyCallback>(keyResponse.getTargetJobID(), innerCallback);
		getClient().postCallback(callback);
	}

	void handleGameConnectTokens(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientGameConnectTokens.Builder> gcTokens = new ClientMsgProtobuf<CMsgClientGameConnectTokens.Builder>(CMsgClientGameConnectTokens.class, packetMsg);

		final GameConnectTokensCallback callback = new GameConnectTokensCallback(gcTokens.getBody().build());
		getClient().postCallback(callback);
	}

	void handleLicenseList(IPacketMsg packetMsg) {
		final ClientMsgProtobuf<CMsgClientLicenseList.Builder> licenseList = new ClientMsgProtobuf<CMsgClientLicenseList.Builder>(CMsgClientLicenseList.class, packetMsg);

		final LicenseListCallback callback = new LicenseListCallback(licenseList.getBody().build());
		getClient().postCallback(callback);
	}

	void handleVACBanStatus(IPacketMsg packetMsg) {
		Debug.Assert(!packetMsg.isProto());

		final ClientMsg<MsgClientVACBanStatus> vacStatus = new ClientMsg<MsgClientVACBanStatus>(packetMsg, MsgClientVACBanStatus.class);

		final VACStatusCallback callback = new VACStatusCallback(vacStatus.getBody(), vacStatus.getOutputStream().toByteArray());
		getClient().postCallback(callback);
	}
}
