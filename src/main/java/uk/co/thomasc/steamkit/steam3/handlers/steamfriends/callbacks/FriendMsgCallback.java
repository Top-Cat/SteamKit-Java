package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import java.util.Arrays;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientFriendMsgIncoming;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatEntryType;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired in response to receiving a message from a friend.
 */
public final class FriendMsgCallback extends CallbackMsg {
	/**
	 * Gets or sets the sender.
	 */
	@Getter private final SteamID sender;

	/**
	 * Gets the chat entry type.
	 */
	@Getter private final EChatEntryType entryType;

	/**
	 * Gets a value indicating whether this message is from a limited account.
	 */
	@Getter private final boolean fromLimitedAccount;

	/**
	 * Gets the message.
	 */
	@Getter private String message;

	public FriendMsgCallback(CMsgClientFriendMsgIncoming msg) {
		sender = new SteamID(msg.getSteamidFrom());
		entryType = EChatEntryType.f(msg.getChatEntryType());

		fromLimitedAccount = msg.getFromLimitedAccount();

		if (msg.getMessage() != null && msg.getMessage().size() > 0) {
			message = new String(Arrays.copyOfRange(msg.getMessage().toByteArray(), 0, msg.getMessage().size() - 1));
		}
	}
}
