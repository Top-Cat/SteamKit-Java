package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EClanRelationship {
	None(0),
	Blocked(1),
	Invited(2),
	Member(3),
	Kicked(4),
	;
	
	private int code;
	
	private EClanRelationship(int code) {
		this.code = code;
	}
	
	public int v() {
		return code;
	}
	
	private static HashMap<Integer, EClanRelationship> values = new HashMap<Integer, EClanRelationship>();

	static {
		for (EClanRelationship type : values()) {
			values.put(type.v(), type);
		}
	}
	
	public static EClanRelationship f(int code) {
		return values.get(code);
	}
}