package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum ELicenseType {
	NoLicense(0),
	SinglePurchase(1),
	SinglePurchaseLimitedUse(2),
	RecurringCharge(3),
	RecurringChargeLimitedUse(4),
	RecurringChargeLimitedUseWithOverages(5), ;

	private int code;

	private ELicenseType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, ELicenseType> values = new HashMap<Integer, ELicenseType>();

	static {
		for (final ELicenseType type : ELicenseType.values()) {
			ELicenseType.values.put(type.v(), type);
		}
	}

	public static ELicenseType f(int code) {
		return ELicenseType.values.get(code);
	}
}
