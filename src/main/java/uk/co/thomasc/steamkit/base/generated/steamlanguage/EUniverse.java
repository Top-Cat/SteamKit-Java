package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EUniverse {
	Invalid(0),
	Public(1),
	Beta(2),
	Internal(3),
	Dev(4),
	RC(5),
	Max(6), ;

	private int code;

	private EUniverse(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EUniverse> values = new HashMap<Integer, EUniverse>();

	static {
		for (final EUniverse type : EUniverse.values()) {
			EUniverse.values.put(type.v(), type);
		}
	}

	public static EUniverse f(int mask) {
		return EUniverse.values.get(mask);
	}
}
