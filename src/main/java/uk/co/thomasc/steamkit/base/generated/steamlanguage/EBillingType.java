package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EBillingType {
	NoCost(0),
	BillOnceOnly(1),
	BillMonthly(2),
	ProofOfPrepurchaseOnly(3),
	GuestPass(4),
	HardwarePromo(5),
	Gift(6),
	AutoGrant(7),
	OEMTicket(8),
	NumBillingTypes(9), ;

	private int code;

	private EBillingType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
