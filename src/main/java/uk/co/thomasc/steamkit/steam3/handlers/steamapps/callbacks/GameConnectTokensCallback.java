package uk.co.thomasc.steamkit.steam3.handlers.steamapps.callbacks;

import java.util.ArrayList;
import java.util.List;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientGameConnectTokens;

import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

import com.google.protobuf.ByteString;

import lombok.Getter;

/**
 * This callback is fired when the client receives a list of game connect tokens.
 */
public final class GameConnectTokensCallback extends CallbackMsg {
	/**
	 * Gets a count of tokens to keep.
	 */
	@Getter private int tokensToKeep;

	/**
	 * Gets the list of tokens.
	 */
	@Getter private List<byte[]> tokens = new ArrayList<byte[]>();

	public GameConnectTokensCallback(CMsgClientGameConnectTokens msg) {
		tokensToKeep = msg.getMaxTokensToKeep();
		for (ByteString s : msg.getTokensList()) {
			tokens.add(s.toByteArray());
		}
	}
}