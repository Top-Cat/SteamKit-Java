package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EServerType {
	Invalid(-1),
	First(0),
	Shell(0),
	GM(1),
	BUM(2),
	AM(3),
	BS(4),
	VS(5),
	ATS(6),
	CM(7),
	FBS(8),
	FG(9),
	SS(10),
	DRMS(11),
	HubOBSOLETE(12),
	Console(13),
	ASBOBSOLETE(14),
	Client(15),
	BootstrapOBSOLETE(16),
	DP(17),
	WG(18),
	SM(19),
	UFS(21),
	Util(23),
	DSS(24),
	P2PRelayOBSOLETE(25),
	AppInformation(26),
	Spare(27),
	FTS(28),
	EPM(29),
	PS(30),
	IS(31),
	CCS(32),
	DFS(33),
	LBS(34),
	MDS(35),
	CS(36),
	GC(37),
	NS(38),
	OGS(39),
	WebAPI(40),
	UDS(41),
	MMS(42),
	GMS(43),
	KGS(44),
	UCM(45),
	RM(46),
	FS(47),
	Econ(48),
	Backpack(49), ;

	private int code;

	private EServerType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EServerType> values = new HashMap<Integer, EServerType>();

	static {
		for (final EServerType type : EServerType.values()) {
			EServerType.values.put(type.v(), type);
		}
	}

	public static EServerType f(int code) {
		return EServerType.values.get(code);
	}
}
