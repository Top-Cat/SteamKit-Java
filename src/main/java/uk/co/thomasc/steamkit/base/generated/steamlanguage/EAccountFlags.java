package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EAccountFlags {
	NormalUser(0),
	PersonaNameSet(1),
	Unbannable(2),
	PasswordSet(4),
	Support(8),
	Admin(16),
	Supervisor(32),
	AppEditor(64),
	HWIDSet(128),
	PersonalQASet(256),
	VacBeta(512),
	Debug(1024),
	Disabled(2048),
	LimitedUser(4096),
	LimitedUserForce(8192),
	EmailValidated(16384),
	MarketingTreatment(32768),
	OGGInviteOptOut(65536),
	ForcePasswordChange(131072),
	ForceEmailVerification(262144),
	LogonExtraSecurity(524288),
	LogonExtraSecurityDisabled(1048576), ;

	private int code;

	private EAccountFlags(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EAccountFlags> values = new HashMap<Integer, EAccountFlags>();

	static {
		for (final EAccountFlags type : EAccountFlags.values()) {
			EAccountFlags.values.put(type.v(), type);
		}
	}

	public static EAccountFlags f(int code) {
		return EAccountFlags.values.get(code);
	}
}
