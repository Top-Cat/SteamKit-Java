package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EWorkshopFileAction {
	Played(0),
	Completed(1), ;

	private int code;

	private EWorkshopFileAction(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
