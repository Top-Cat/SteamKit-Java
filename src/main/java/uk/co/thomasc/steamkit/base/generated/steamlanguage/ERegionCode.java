package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum ERegionCode {
	USEast(0x00),
	USWest(0x01),
	SouthAmerica(0x02),
	Europe(0x03),
	Asia(0x04),
	Australia(0x05),
	MiddleEast(0x06),
	Africa(0x07),
	World(0xFF), ;

	private byte code;

	private ERegionCode(int code) {
		this.code = (byte) code;
	}

	public byte v() {
		return code;
	}
}
