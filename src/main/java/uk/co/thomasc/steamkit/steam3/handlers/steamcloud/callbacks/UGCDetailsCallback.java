package uk.co.thomasc.steamkit.steam3.handlers.steamcloud.callbacks;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientUFSGetUGCDetailsResponse;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.steam3.handlers.steamcloud.SteamCloud;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

import lombok.Getter;

/**
 * This callback is recieved in response to calling {@link SteamCloud#requestUGCDetails(long)}.
 */
public final class UGCDetailsCallback extends CallbackMsg {
	/**
	 * Gets the result of the request.
	 */
	@Getter private EResult result;

	/**
	 * Gets the App ID the UGC is for.
	 */
	@Getter private int appID;

	/**
	 * Gets the App ID the UGC is for.
	 */
	@Getter private SteamID creator;

	/**
	 * Gets the URL that the content is located at.
	 */
	@Getter private String URL;

	/**
	 * Gets the name of the file.
	 */
	@Getter private String fileName;

	/**
	 * Gets the size of the file.
	 */
	@Getter private int fileSize;

	public UGCDetailsCallback(CMsgClientUFSGetUGCDetailsResponse msg) {
		result = EResult.f(msg.getEresult());

		appID = msg.getAppId();
		creator = new SteamID(msg.getSteamidCreator());

		URL = msg.getUrl();

		fileName = msg.getFilename();
		fileSize = msg.getFileSize();
	}
}