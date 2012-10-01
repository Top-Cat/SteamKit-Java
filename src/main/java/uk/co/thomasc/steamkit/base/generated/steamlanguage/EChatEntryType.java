package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatEntryType {
	Invalid(0),
	ChatMsg(1),
	Typing(2),
	InviteGame(3),
	Emote(4),
	LobbyGameStart(5),
	LeftConversation(6), ;

	private int code;

	private EChatEntryType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatEntryType> values = new HashMap<Integer, EChatEntryType>();

	static {
		for (final EChatEntryType type : EChatEntryType.values()) {
			EChatEntryType.values.put(type.v(), type);
		}
	}

	public static EChatEntryType f(int code) {
		return EChatEntryType.values.get(code);
	}
}
