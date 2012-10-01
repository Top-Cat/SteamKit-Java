package uk.co.thomasc.steamkit.steam3.steamclient.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EResult;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgChannelEncryptResult;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is received after attempting to connect to the Steam network.
 */
public final class ConnectedCallback extends CallbackMsg {
	/**
	 * Gets the result of the connection attempt.
	 */
	@Getter private final EResult result;

	public ConnectedCallback(MsgChannelEncryptResult result) {
		this(result.result);
	}

	public ConnectedCallback(EResult result) {
		this.result = result;
	}
}
