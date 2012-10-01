package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.google.protobuf.ByteString;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGameConnectTokens;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is fired when the client receives a list of game connect tokens.
 */
public final class GameConnectTokensCallback extends CallbackMsg {
	/**
	 * Gets a count of tokens to keep.
	 */
	@Getter private final int tokensToKeep;

	/**
	 * Gets the list of tokens.
	 */
	@Getter private final List<byte[]> tokens = new ArrayList<byte[]>();

	public GameConnectTokensCallback(CMsgClientGameConnectTokens msg) {
		tokensToKeep = msg.getMaxTokensToKeep();
		for (final ByteString s : msg.getTokensList()) {
			tokens.add(s.toByteArray());
		}
	}
}
