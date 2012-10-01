package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum ECurrencyCode {
	Invalid(0),
	USD(1),
	GBP(2),
	EUR(3),
	CHF(4),
	RUB(5),
	PLN(6),
	BRL(7), ;

	private int code;

	private ECurrencyCode(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, ECurrencyCode> values = new HashMap<Integer, ECurrencyCode>();

	static {
		for (final ECurrencyCode type : ECurrencyCode.values()) {
			ECurrencyCode.values.put(type.v(), type);
		}
	}

	public static ECurrencyCode f(int code) {
		return ECurrencyCode.values.get(code);
	}
}
