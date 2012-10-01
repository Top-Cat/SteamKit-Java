package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatActionResult {
	Success(1),
	Error(2),
	NotPermitted(3),
	NotAllowedOnClanMember(4),
	NotAllowedOnBannedUser(5),
	NotAllowedOnChatOwner(6),
	NotAllowedOnSelf(7),
	ChatDoesntExist(8),
	ChatFull(9),
	VoiceSlotsFull(10), ;

	private int code;

	private EChatActionResult(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatActionResult> values = new HashMap<Integer, EChatActionResult>();

	static {
		for (final EChatActionResult type : EChatActionResult.values()) {
			EChatActionResult.values.put(type.v(), type);
		}
	}

	public static EChatActionResult f(int code) {
		return EChatActionResult.values.get(code);
	}
}
