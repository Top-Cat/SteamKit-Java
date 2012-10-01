package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EServerFlags {
	None(0),
	Active(1),
	Secure(2),
	Dedicated(4),
	Linux(8),
	Passworded(16),
	Private(32), ;

	private int code;

	private EServerFlags(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
