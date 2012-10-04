package uk.co.thomasc.steamkit.base.gc.dota;

import uk.co.thomasc.steamkit.base.gc.EGCMsgBase;

public final class EGCMsg extends EGCMsgBase {
	public final static int JoinChatChannel = 7009;
	public final static int JoinChatChannelResponse = 7010;
	public final static int LeaveChatChannel = 7011;
	public final static int ChatMessage = 7012;
	public final static int OtherJoinedChatChannel = 7013;
	public final static int OtherLeftChatChannel = 7014;

	public final static int RequestPersonaDetails = 7021;
	public final static int PersonaDetails = 7022;

	public final static int GetNews = 7024;
	public final static int NewsResponse = 7025;

	public final static int GetRecentMatches = 7027;
	public final static int RecentMatchesResponse = 7028;

	public final static int FindSourceTVGames = 7031;
	public final static int SourceTVGamesResponse = 7032;
	public final static int StartFindingMatch = 7033;
	public final static int AbandonCurrentGame = 7035;
	public final static int StopFindingMatch = 7036;

	public final static int ForceSOCacheResend = 7037;

	public final static int PracticeLobbyCreate = 7038;
	public final static int PracticeLobbyLeave = 7040;
	public final static int PracticeLobbyLaunch = 7041;
	public final static int PracticeLobbyList = 7042;
	public final static int PracticeLobbyListResponse = 7043;
	public final static int PracticeLobbyJoin = 7044;
	public final static int SetAvatar = 7045;
	public final static int PracticeLobbySetDetails = 7046;
	public final static int PracticeLobbySetTeamSlot = 7047;

	public final static int InitialQuestionnaireResponse = 7049;

	public final static int TournamentRequest = 7051;
	public final static int TournamentResponse = 7052;

	public final static int SetTeamSlot = 7054;
	public final static int SetTeamSlotResponse = 7055;

	public final static int BroadcastNotification = 7056;
	public final static int RequestDefaultChatChannel = 7058;
	public final static int RequestDefaultChatChannelResponse = 7059;
	public final static int RequestChatChannelList = 7060;
	public final static int RequestChatChannelListResponse = 7061;
	public final static int TodayMessages = 7062;
	public final static int RequestTodayMessages = 7063;
	public final static int RequestMatches = 7064;
	public final static int RequestMatchesResponse = 7065;
	public final static int MatchmakingSearchCountRequest = 7066;
	public final static int MatchmakingSearchCountResponse = 7067;

	public final static int ReadyUp = 7070;
	public final static int KickedFromMatchmakingQueue = 7071;

	public final static int SpectateFriendGame = 7073;
	public final static int SpectateFriendGameResponse = 7074;

	public final static int ReportsRemainingRequest = 7076;
	public final static int ReportsRemainingResponse = 7077;
	public final static int SubmitPlayerReport = 7078;
	public final static int SubmitPlayerReportResponse = 7079;

	public final static int PracticeLobbyKick = 7081;

	public final static int RequestSaveGames = 7084;
	public final static int RequestSaveGamesResponse = 7086;

	public final static int WatchGame = 7091;
	public final static int WatchGameResponse = 7092;

	public final static int MatchDetailsRequest = 7095;
	public final static int MatchDetailsResponse = 7096;

	public final static int CancelWatchGame = 7097;

	public final static int ProfileRequest = 7098;
	public final static int ProfileResponse = 7099;

	public final static int Popup = 7102;
	public final static int NotifySuccessfulReport = 7103;
	public final static int ClearNotifySuccessfulReport = 7104;

	public final static int TransferTeamAdmin = 7107; // this may be totally wrong
	public final static int TransferTeamAdminResponse = 7108; // possibly DotaGenericResult?

	public final static int MatchGroupWaitTimesRequest = 7109;
	public final static int MatchGroupWaitTimesResponse = 7110;

	public final static int FriendPracticeLobbyListRequest = 7111;
	public final static int FriendPracticeLobbyListResponse = 7112;
	public final static int FriendPracticeLobbyJoinResponse = 7113;

	public final static int CreateTeam = 7115;
	public final static int CreateTeamResponse = 7116;
	public final static int DisbandTeam = 7117;
	public final static int DisbandTeamResponse = 7118;
	public final static int RequestTeamData = 7119;
	public final static int RequestTeamDataResponse = 7120;
	public final static int TeamData = 7121;
	public final static int InitiateTeamInvite = 7122;
	public final static int InitiateTeamInviteResponse = 7123;
	public final static int TeamInvite = 7124;
	public final static int TeamInviteReply = 7125;
	public final static int TeamInviteReplyResponse = 7126;
	public final static int KickTeamMember = 7128;
	public final static int KickTeamMemberResponse = 7129;
	public final static int LeaveTeam = 7130;
	public final static int LeaveTeamResponse = 7131;
	public final static int SuggestTeamMatchmaking = 7132;

	public final static int HeroFavoritesAdd = 7133;
	public final static int HeroFavoritesRemove = 7134;
	public final static int PlayerHeroesRecentRequest = 7135;
	public final static int PlayerHeroesRecent = 7136;

	public final static int SetChatChannelVerbosity = 7137;
	public final static int ChatChannelFullUpdate = 7138;

	public final static int EditTeamLogo = 7139;
	public final static int EditTeamLogoResponse = 7140;

	public final static int RequestLeaguesWithLiveGames = 7147;
	public final static int LeaguesWithLiveGamesResponse = 7148;

	public final static int MatchVote = 7152;
	public final static int MatchVoteResponse = 7153;

	public final static int RetrieveMatchVote = 7154;
	public final static int RetrieveMatchVoteResponse = 7155;

	public final static int CheckSpectatorOnly = 7162;
	public final static int CheckSpectatorOnlyResponse = 7163;
}
