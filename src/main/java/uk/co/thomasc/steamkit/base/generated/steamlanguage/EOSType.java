package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EOSType {
	Unknown(-1),
	PS3(-300),
	MacOSUnknown(-102),
	MacOS104(-101),
	MacOS105(-100),
	MacOS1058(-99),
	MacOS106(-95),
	MacOS1063(-94),
	MacOS1064_slgu(-93),
	MacOS1067(-92),
	MacOS107(-90),
	LinuxUnknown(-203),
	Linux22(-202),
	Linux24(-201),
	Linux26(-200),
	WinUnknown(0),
	Win311(1),
	Win95(2),
	Win98(3),
	WinME(4),
	WinNT(5),
	Win200(6),
	WinXP(7),
	Win2003(8),
	WinVista(9),
	Win7(10),
	Win2008(11),
	WinMAX(12),
	Max(23), ;

	private int code;

	private EOSType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
