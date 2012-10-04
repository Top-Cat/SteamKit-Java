package uk.co.thomasc.steamkit.base.gc.tf2;

import uk.co.thomasc.steamkit.base.gc.EGCMsgBase;

public final class EGCMsg extends EGCMsgBase {
	public final static int ReportWarKill = 5001;

	public final static int VoteKickBanPlayer = 5018;
	public final static int VoteKickBanPlayerResult = 5019;
	public final static int KickPlayer = 5020;
	public final static int StartedTraining = 5021;
	public final static int FreeTrial_ChooseMostHelpfulFriend = 5022;
	public final static int RequestTF2Friends = 5023;
	public final static int RequestTF2FriendsResponse = 5024;
	public final static int Replay_UploadedToYouTube = 5025;
	public final static int Replay_SubmitContestEntry = 5026;
	public final static int Replay_SubmitContestEntryResponse = 5027;

	public final static int Coaching_AddToCoaches = 5200;
	public final static int Coaching_AddToCoachesResponse = 5201;
	public final static int Coaching_RemoveFromCoaches = 5202;
	public final static int Coaching_RemoveFromCoachesResponse = 5203;
	public final static int Coaching_FindCoach = 5204;
	public final static int Coaching_FindCoachResponse = 5205;
	public final static int Coaching_AskCoach = 5206;
	public final static int Coaching_AskCoachResponse = 5207;
	public final static int Coaching_CoachJoinGame = 5208;
	public final static int Coaching_CoachJoining = 5209;
	public final static int Coaching_CoachJoined = 5210;
	public final static int Coaching_LikeCurrentCoach = 5211;
	public final static int Coaching_RemoveCurrentCoach = 5212;
	public final static int Coaching_AlreadyRatedCoach = 5213;

	public final static int Duel_Request = 5500;
	public final static int Duel_Response = 5501;
	public final static int Duel_Results = 5502;
	public final static int Duel_Status = 5503;

	public final static int Halloween_ReservedItem = 5600;
	public final static int Halloween_GrantItem = 5601;
	public final static int Halloween_GrantItemResponse = 5604;
	public final static int Halloween_Cheat_QueryResponse = 5605;
	public final static int Halloween_ItemClaimed = 5606;

	public final static int GameServer_LevelInfo = 5700;
	public final static int GameServer_AuthChallenge = 5701;
	public final static int GameServer_AuthChallengeResponse = 5702;
	public final static int GameServer_CreateIdentity = 5703;
	public final static int GameServer_CreateIdentityResponse = 5704;
	public final static int GameServer_List = 5705;
	public final static int GameServer_ListResponse = 5706;
	public final static int GameServer_AuthResult = 5707;
	public final static int GameServer_ResetIdentity = 5708;
	public final static int GameServer_ResetIdentityResponse = 5709;

	public final static int QP_ScoreServers = 5800;
	public final static int QP_ScoreServersResponse = 58001;

	public final static int PickupItemEligibility_Query = 6000;
	public final static int Dev_GrantWarKill = 6001;

	public final static int IncrementKillCountAttribute = 6100;
	public final static int IncrementKillCountResponse = 6101;
}
