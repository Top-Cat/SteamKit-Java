package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EClanRank {
	None(0),
	Owner(1),
	Officer(2),
	Member(3), ;

	private int code;

	private EClanRank(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
