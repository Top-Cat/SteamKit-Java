package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum ELicenseFlags {
	None(0),
	Renew(0x01),
	RenewalFailed(0x02),
	Pending(0x04),
	Expired(0x08),
	CancelledByUser(0x10),
	CancelledByAdmin(0x20),
	LowViolenceContent(0x40),
	;
	
	private int code;
	
	private ELicenseFlags(int code) {
		this.code = code;
	}
	
	public int v() {
		return code;
	}
	
	private static HashMap<Integer, ELicenseFlags> values = new HashMap<Integer, ELicenseFlags>();

	static {
		for (ELicenseFlags type : values()) {
			values.put(type.v(), type);
		}
	}
	
	public static ELicenseFlags f(int code) {
		return values.get(code);
	}
}