package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatRoomType {
	Friend(1),
	MUC(2),
	Lobby(3), ;

	private int code;

	private EChatRoomType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatRoomType> values = new HashMap<Integer, EChatRoomType>();

	static {
		for (final EChatRoomType type : EChatRoomType.values()) {
			EChatRoomType.values.put(type.v(), type);
		}
	}

	public static EChatRoomType f(int code) {
		return EChatRoomType.values.get(code);
	}
}
