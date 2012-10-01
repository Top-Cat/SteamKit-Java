package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EFriendRelationship {
	None(0),
	Blocked(1),
	PendingInvitee(2),
	RequestRecipient(2),
	RequestInitiator(4),
	PendingInviter(4),
	Friend(3),
	Ignored(5),
	IgnoredFriend(6),
	SuggestedFriend(7), ;

	private int code;

	private EFriendRelationship(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EFriendRelationship> values = new HashMap<Integer, EFriendRelationship>();

	static {
		for (final EFriendRelationship type : EFriendRelationship.values()) {
			EFriendRelationship.values.put(type.v(), type);
		}
	}

	public static EFriendRelationship f(int code) {
		return EFriendRelationship.values.get(code);
	}
}
