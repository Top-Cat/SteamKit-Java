package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EClientPersonaStateFlag {
	Status(1),
	PlayerName(2),
	QueryPort(4),
	SourceID(8),
	Presence(16),
	Metadata(32),
	LastSeen(64),
	ClanInfo(128),
	GameExtraInfo(256),
	GameDataBlob(512),
	ClanTag(1024), ;

	private int code;

	private EClientPersonaStateFlag(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EClientPersonaStateFlag> values = new HashMap<Integer, EClientPersonaStateFlag>();

	static {
		for (final EClientPersonaStateFlag type : EClientPersonaStateFlag.values()) {
			EClientPersonaStateFlag.values.put(type.v(), type);
		}
	}

	public static EClientPersonaStateFlag f(int code) {
		return EClientPersonaStateFlag.values.get(code);
	}
}
