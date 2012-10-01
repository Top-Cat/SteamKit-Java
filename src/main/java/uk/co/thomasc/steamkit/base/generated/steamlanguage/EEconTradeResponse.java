package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EEconTradeResponse {
	Accepted(0),
	Declined(1),
	VacBannedInitiator(2),
	VacBannedTarget(3),
	TargetAlreadyTrading(4),
	Disabled(5),
	NotLoggedIn(6),
	Cancel(7),
	TooSoon(8),
	TooSoonPenalty(9),
	ConnectionFailed(10),
	InitiatorAlreadyTrading(11),
	Error(12),
	Timeout(13), ;

	private int code;

	private EEconTradeResponse(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EEconTradeResponse> values = new HashMap<Integer, EEconTradeResponse>();

	static {
		for (final EEconTradeResponse type : EEconTradeResponse.values()) {
			EEconTradeResponse.values.put(type.v(), type);
		}
	}

	public static EEconTradeResponse f(int code) {
		return EEconTradeResponse.values.get(code);
	}
}
