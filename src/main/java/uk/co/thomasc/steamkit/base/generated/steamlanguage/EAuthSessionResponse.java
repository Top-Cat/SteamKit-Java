package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EAuthSessionResponse {
	OK(0),
	UserNotConnectedToSteam(1),
	NoLicenseOrExpired(2),
	VACBanned(3),
	LoggedInElseWhere(4),
	VACCheckTimedOut(5),
	AuthTicketCanceled(6),
	AuthTicketInvalidAlreadyUsed(7),
	AuthTicketInvalid(8), ;

	private int code;

	private EAuthSessionResponse(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EAuthSessionResponse> values = new HashMap<Integer, EAuthSessionResponse>();

	static {
		for (final EAuthSessionResponse type : EAuthSessionResponse.values()) {
			EAuthSessionResponse.values.put(type.v(), type);
		}
	}

	public static EAuthSessionResponse f(int code) {
		return EAuthSessionResponse.values.get(code);
	}
}
