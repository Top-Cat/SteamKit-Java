package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EAppInfoSection {
	Unknown(0),
	All(1),
	First(2),
	Common(2),
	Extended(3),
	Config(4),
	Stats(5),
	Install(6),
	Depots(7),
	VAC(8),
	DRM(9),
	UFS(10),
	OGG(11),
	ItemsUNUSED(12),
	Policies(13),
	SysReqs(14),
	Community(15), ;

	private int code;

	private EAppInfoSection(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EAppInfoSection> values = new HashMap<Integer, EAppInfoSection>();

	static {
		for (final EAppInfoSection type : EAppInfoSection.values()) {
			EAppInfoSection.values.put(type.v(), type);
		}
	}

	public static EAppInfoSection f(int code) {
		return EAppInfoSection.values.get(code);
	}
}
