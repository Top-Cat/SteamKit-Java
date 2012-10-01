package uk.co.thomasc.steamkit.base.generated.steamlanguage;

public enum EFriendFlags {
	None(0),
	Blocked(1),
	FriendshipRequested(2),
	Immediate(4),
	ClanMember(8),
	GameServer(16),
	RequestingFriendship(128),
	RequestingInfo(256),
	Ignored(512),
	IgnoredFriend(1024),
	FlagAll(65535), ;

	private int code;

	private EFriendFlags(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
