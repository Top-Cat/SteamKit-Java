package uk.co.thomasc.steamkit.steam3.handlers.steamapps.types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoResponse;
import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientAppInfoResponse.App.Section;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAppInfoSection;
import uk.co.thomasc.steamkit.types.keyvalue.KeyValue;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a single app in the info response.
 */
public final class App {
	/**
	 * Gets the status of the app.
	 */
	@Getter private final AppInfoStatus status;

	/**
	 * Gets the AppID for this app.
	 */
	@Getter private final int appID;

	/**
	 * Gets the last change number for this app.
	 */
	@Getter private int changeNumber;

	/**
	 * Gets a section data for this app.
	 */
	@Getter private final Map<EAppInfoSection, KeyValue> sections = new HashMap<EAppInfoSection, KeyValue>();

	public App(CMsgClientAppInfoResponse.App app, AppInfoStatus status) {
		this.status = status;
		appID = app.getAppId();
		changeNumber = app.getChangeNumber();

		for (final Section section : app.getSectionsList()) {
			final KeyValue kv = new KeyValue();
			final BinaryReader cs = new BinaryReader(section.getSectionKv().toByteArray());

			try {
				kv.readAsBinary(cs);

				sections.put(EAppInfoSection.f(section.getSectionId()), kv);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public App(int appid, AppInfoStatus status) {
		this.status = status;
		appID = appid;
	}
}
