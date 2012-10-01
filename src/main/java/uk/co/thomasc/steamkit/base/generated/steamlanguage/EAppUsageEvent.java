package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EAppUsageEvent {
	GameLaunch(1),
	GameLaunchTrial(2),
	Media(3),
	PreloadStart(4),
	PreloadFinish(5),
	MarketingMessageView(6),
	InGameAdViewed(7),
	GameLaunchFreeWeekend(8), ;

	private int code;

	private EAppUsageEvent(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
