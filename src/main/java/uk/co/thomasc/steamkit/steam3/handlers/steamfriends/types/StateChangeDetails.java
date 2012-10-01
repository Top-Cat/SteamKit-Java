package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types;

import java.io.IOException;

import lombok.Getter;

import com.google.protobuf.CodedInputStream;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatMemberStateChange;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * Represents state change information.
 */
public final class StateChangeDetails {
	/**
	 * Gets the SteamID of the chatter that was acted on.
	 */
	@Getter private SteamID chatterActedOn;

	/**
	 * Gets the state change for the acted on SteamID.
	 */
	@Getter private EChatMemberStateChange stateChange;

	/**
	 * Gets the SteamID of the chatter that acted on {@link #chatterActedOn}.
	 */
	@Getter private SteamID chatterActedBy;

	public StateChangeDetails(byte[] data) {
		final CodedInputStream is = CodedInputStream.newInstance(data);

		try {
			chatterActedOn = new SteamID(is.readInt64());
			stateChange = EChatMemberStateChange.f(is.readInt32());
			chatterActedBy = new SteamID(is.readInt64());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// TODO: for EChatMemberStateChange.Entered, the following data is a binary kv MessageObject
		// that includes permission and details that may be useful
	}
}
