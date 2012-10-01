package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EDepotFileFlag {
	UserConfig(1),
	VersionedUserConfig(2),
	Encrypted(4),
	ReadOnly(8),
	Hidden(16),
	Executable(32),
	Directory(64), ;

	private int code;

	private EDepotFileFlag(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
