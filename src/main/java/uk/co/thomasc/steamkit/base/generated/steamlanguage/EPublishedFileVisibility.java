package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EPublishedFileVisibility {
	Public(0),
	FriendsOnly(1),
	Private(2), ;

	private int code;

	private EPublishedFileVisibility(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EPublishedFileVisibility> values = new HashMap<Integer, EPublishedFileVisibility>();

	static {
		for (final EPublishedFileVisibility type : EPublishedFileVisibility.values()) {
			EPublishedFileVisibility.values.put(type.v(), type);
		}
	}

	public static EPublishedFileVisibility f(int code) {
		return EPublishedFileVisibility.values.get(code);
	}
}
