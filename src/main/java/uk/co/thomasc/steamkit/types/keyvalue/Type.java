package uk.co.thomasc.steamkit.types.keyvalue;

import java.util.HashMap;

enum Type {
	None(0),
	String(1),
	Int32(2),
	Float32(3),
	Pointer(4),
	WideString(5),
	Color(6),
	UInt64(7),
	End(8), ;

	private byte code;

	private Type(int code) {
		this.code = (byte) code;
	}

	public byte v() {
		return code;
	}

	private static HashMap<Byte, Type> values = new HashMap<Byte, Type>();

	static {
		for (final Type type : Type.values()) {
			Type.values.put(type.v(), type);
		}
	}

	public static Type f(int code) {
		return Type.values.get((byte) code);
	}
}
