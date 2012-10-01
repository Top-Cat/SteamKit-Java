package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EPaymentMethod {
	None(0),
	ActivationCode(1),
	CreditCard(2),
	Giropay(3),
	PayPal(4),
	Ideal(5),
	PaySafeCard(6),
	Sofort(7),
	GuestPass(8),
	WebMoney(9),
	HardwarePromo(16),
	ClickAndBuy(32),
	AutoGrant(64),
	Wallet(128),
	OEMTicket(256),
	Split(512),
	Complimentary(1024), ;

	private int code;

	private EPaymentMethod(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EPaymentMethod> values = new HashMap<Integer, EPaymentMethod>();

	static {
		for (final EPaymentMethod type : EPaymentMethod.values()) {
			EPaymentMethod.values.put(type.v(), type);
		}
	}

	public static EPaymentMethod f(int code) {
		return EPaymentMethod.values.get(code);
	}
}
