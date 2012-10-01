package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EUdpPacketType {
	Invalid(0),
	ChallengeReq(1),
	Challenge(2),
	Connect(3),
	Accept(4),
	Disconnect(5),
	Data(6),
	Datagram(7),
	Max(8), ;

	private byte code;

	private EUdpPacketType(int code) {
		this.code = (byte) code;
	}

	public byte v() {
		return code;
	}

	private static HashMap<Byte, EUdpPacketType> values = new HashMap<Byte, EUdpPacketType>();

	static {
		for (final EUdpPacketType type : EUdpPacketType.values()) {
			EUdpPacketType.values.put(type.v(), type);
		}
	}

	public static EUdpPacketType fromCode(byte code) {
		return EUdpPacketType.values.get(code);
	}
}
