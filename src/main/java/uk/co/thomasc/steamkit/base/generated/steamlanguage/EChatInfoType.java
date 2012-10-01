package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatInfoType {
	StateChange(1),
	InfoUpdate(2),
	MemberLimitChange(3), ;

	private int code;

	private EChatInfoType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatInfoType> values = new HashMap<Integer, EChatInfoType>();

	static {
		for (final EChatInfoType type : EChatInfoType.values()) {
			EChatInfoType.values.put(type.v(), type);
		}
	}

	public static EChatInfoType f(int code) {
		return EChatInfoType.values.get(code);
	}
}
