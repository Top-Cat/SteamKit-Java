package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.google.protobuf.CodedInputStream;

import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientVACBanStatus;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired when the client receives it's VAC banned status.
 */
public final class VACStatusCallback extends CallbackMsg {
	/**
	 * Gets a list of VAC banned apps the client is banned from.
	 */
	@Getter private final List<Integer> bannedApps = new ArrayList<Integer>();

	public VACStatusCallback(MsgClientVACBanStatus msg, byte[] payload) {
		final CodedInputStream cs = CodedInputStream.newInstance(payload);

		try {
			for (int x = 0; x < msg.numBans; x++) {
				bannedApps.add(cs.readInt32());
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
