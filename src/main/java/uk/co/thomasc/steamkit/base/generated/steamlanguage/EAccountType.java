package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EAccountType {
	Invalid(0),
	Individual(1),
	Multiseat(2),
	GameServer(3),
	AnonGameServer(4),
	Pending(5),
	ContentServer(6),
	Clan(7),
	Chat(8),
	ConsoleUser(9),
	AnonUser(10),
	Max(11), ;

	private int code;

	private EAccountType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EAccountType> values = new HashMap<Integer, EAccountType>();

	static {
		for (final EAccountType type : EAccountType.values()) {
			EAccountType.values.put(type.v(), type);
		}
	}

	public static EAccountType fromCode(int code) {
		return EAccountType.values.get(code);
	}
}
