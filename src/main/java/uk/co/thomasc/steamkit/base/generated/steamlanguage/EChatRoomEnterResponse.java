package uk.co.thomasc.steamkit.base.generated.steamlanguage;

import java.util.HashMap;

public enum EChatRoomEnterResponse {
	Success(1),
	DoesntExist(2),
	NotAllowed(3),
	Full(4),
	Error(5),
	Banned(6),
	Limited(7),
	ClanDisabled(8),
	CommunityBan(9),
	MemberBlockedYou(10),
	YouBlockedMember(11),
	NoRankingDataLobby(12),
	NoRankingDataUser(13),
	RankOutOfRange(14), ;

	private int code;

	private EChatRoomEnterResponse(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, EChatRoomEnterResponse> values = new HashMap<Integer, EChatRoomEnterResponse>();

	static {
		for (final EChatRoomEnterResponse type : EChatRoomEnterResponse.values()) {
			EChatRoomEnterResponse.values.put(type.v(), type);
		}
	}

	public static EChatRoomEnterResponse f(int code) {
		return EChatRoomEnterResponse.values.get(code);
	}
}
