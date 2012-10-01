package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatMemberStateChange {
	Entered(0x01),
	Left(0x02),
	Disconnected(0x04),
	Kicked(0x08),
	Banned(0x10), ;

	private byte code;

	private EChatMemberStateChange(int code) {
		this.code = (byte) code;
	}

	public byte v() {
		return code;
	}

	private static HashMap<Byte, EChatMemberStateChange> values = new HashMap<Byte, EChatMemberStateChange>();

	static {
		for (final EChatMemberStateChange type : EChatMemberStateChange.values()) {
			EChatMemberStateChange.values.put(type.v(), type);
		}
	}

	public static EChatMemberStateChange f(int code) {
		return EChatMemberStateChange.values.get(code);
	}
}
