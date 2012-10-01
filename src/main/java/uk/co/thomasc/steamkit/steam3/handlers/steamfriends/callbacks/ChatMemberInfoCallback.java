package uk.co.thomasc.steamkit.steam3.handlers.steamfriends.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EChatInfoType;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg.MsgClientChatMemberInfo;
import uk.co.thomasc.steamkit.steam3.handlers.steamfriends.types.StateChangeDetails;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

/**
 * This callback is fired in response to chat member info being recieved.
 */
public final class ChatMemberInfoCallback extends CallbackMsg {
	/**
	 * Gets SteamId of the chat room.
	 */
	@Getter private final SteamID chatRoomID;

	/**
	 * Gets the info type.
	 */
	@Getter private final EChatInfoType type;

	/**
	 * Gets the state change info for {@link EChatInfoType#StateChange} member info updates.
	 */
	@Getter private StateChangeDetails stateChangeInfo;

	public ChatMemberInfoCallback(MsgClientChatMemberInfo msg, byte[] payload) {
		chatRoomID = msg.getSteamIdChat();
		type = msg.type;

		switch (type) {
			case StateChange:
				stateChangeInfo = new StateChangeDetails(payload);
				break;
		//TODO: handle more types
		}
	}
}
