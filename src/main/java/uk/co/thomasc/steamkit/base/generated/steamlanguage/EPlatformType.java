package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EPlatformType {
	Unknown(0),
	Win32(1),
	Win64(2),
	Linux(3),
	OSX(4),
	PS3(5),
	Max(6), ;

	private int code;

	private EPlatformType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
