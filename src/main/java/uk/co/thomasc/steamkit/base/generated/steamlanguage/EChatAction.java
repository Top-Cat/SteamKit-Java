package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatAction {
	InviteChat(1),
	Kick(2),
	Ban(3),
	UnBan(4),
	StartVoiceSpeak(5),
	EndVoiceSpeak(6),
	LockChat(7),
	UnlockChat(8),
	CloseChat(9),
	SetJoinable(10),
	SetUnjoinable(11),
	SetOwner(12),
	SetInvisibleToFriends(13),
	SetVisibleToFriends(14),
	SetModerated(15),
	SetUnmoderated(16), ;

	private int code;

	private EChatAction(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatAction> values = new HashMap<Integer, EChatAction>();

	static {
		for (final EChatAction type : EChatAction.values()) {
			EChatAction.values.put(type.v(), type);
		}
	}

	public static EChatAction f(int code) {
		return EChatAction.values.get(code);
	}
}
