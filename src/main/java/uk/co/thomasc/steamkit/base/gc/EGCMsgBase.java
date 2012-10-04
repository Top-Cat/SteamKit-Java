package uk.co.thomasc.steamkit.base.gc;

public abstract class EGCMsgBase {
	public final static int GenericReply = 10;

	public final static int SOCreate = 21;
	public final static int SOUpdate = 22;
	public final static int SODestroy = 23;
	public final static int SOCacheSubscribed = 24;
	public final static int SOCacheUnsubscribed = 25;
	public final static int SOUpdateMultiple = 26;
	public final static int SOCacheSubscriptionCheck = 27;
	public final static int SOCacheSubscriptionRefresh = 28;

	public final static int AchievementAwarded = 51;
	public final static int ConCommand = 52;
	public final static int StartPlaying = 53;
	public final static int StopPlaying = 54;
	public final static int StartGameserver = 55;
	public final static int StopGameserver = 56;
	public final static int WGRequest = 57;
	public final static int WGResponse = 58;
	public final static int GetUserGameStatsSchema = 59;
	public final static int GetUserGameStatsSchemaResponse = 60;
	public final static int GetUserStatsDEPRECATED = 61;
	public final static int GetUserStatsResponse = 62;
	public final static int AppInfoUpdated = 63;
	public final static int ValidateSession = 64;
	public final static int ValidateSessionResponse = 65;
	public final static int LookupAccountFromInput = 66;
	public final static int SendHTTPRequest = 67;
	public final static int SendHTTPRequestResponse = 68;
	public final static int PreTestSetup = 69;
	public final static int RecordSupportAction = 70;
	public final static int GetAccountDetails = 71;
	public final static int SendInterAppMessage = 72;
	public final static int ReceiveInterAppMessage = 73;
	public final static int FindAccounts = 74;
	public final static int PostAlert = 75;
	public final static int GetLicenses = 76;
	public final static int GetUserStats = 77;
	public final static int GetCommands = 78;
	public final static int GetCommandsResponse = 79;
	public final static int AddFreeLicense = 80;
	public final static int AddFreeLicenseResponse = 81;
	public final static int GetIPLocation = 82;
	public final static int GetIPLocationResponse = 83;
	public final static int SystemStatsSchema = 84;
	public final static int GetSystemStats = 85;
	public final static int GetSystemStatsResponse = 86;

	public final static int WebAPIRegisterInterfaces = 101;
	public final static int WebAPIJobRequest = 102;
	public final static int WebAPIRegistrationRequested = 103;

	public final static int MemCachedGet = 200;
	public final static int MemCachedGetResponse = 201;
	public final static int MemCachedSet = 203;
	public final static int MemCachedDelete = 204;

	public final static int SetItemPosition = 1001;
	public final static int Craft = 1002;
	public final static int CraftResponse = 1003;
	public final static int Delete = 1004;
	public final static int VerifyCacheSubscription = 1005;
	public final static int NameItem = 1006;
	public final static int UnlockCrate = 1007;
	public final static int UnlockCrateResponse = 1008;
	public final static int PaintItem = 1009;
	public final static int PaintItemResponse = 1010;
	public final static int GoldenWrenchBroadcast = 1011;
	public final static int MOTDRequest = 1012;
	public final static int MOTDRequestResponse = 1013;
	public final static int AddItemToSocket = 1014;
	public final static int AddItemToSocketResponse = 1015;
	public final static int AddSocketToBaseItem = 1016;
	public final static int AddSocketToItem = 1017;
	public final static int AddSocketToItemResponse = 1018;
	public final static int NameBaseItem = 1019;
	public final static int NameBaseItemResponse = 1020;
	public final static int RemoveSocketItem = 1021;
	public final static int RemoveSocketItemResponse = 1022;
	public final static int CustomizeItemTexture = 1023;
	public final static int CustomizeItemTextureResponse = 1024;
	public final static int UseItemRequest = 1025;
	public final static int UseItemResponse = 1026;
	public final static int GiftedItems = 1027;
	public final static int SpawnItem = 1028;
	public final static int RespawnPostLoadoutChange = 1029;
	public final static int RemoveItemName = 1030;
	public final static int RemoveItemPaint = 1031;
	public final static int GiftWrapItem = 1032;
	public final static int GiftWrapItemResponse = 1033;
	public final static int DeliverGift = 1034;
	public final static int DeliverGiftResponseGiver = 1035;
	public final static int DeliverGiftResponseReceiver = 1036;
	public final static int UnwrapGiftRequest = 1037;
	public final static int UnwrapGiftResponse = 1038;
	public final static int SetItemStyle = 1039;
	public final static int UsedClaimCodeItem = 1040;
	public final static int SortItems = 1041;
	public final static int RevolvingLootList = 1042;
	public final static int LookupAccount = 1043;
	public final static int LookupAccountResponse = 1044;
	public final static int LookupAccountName = 1045;
	public final static int LookupAccountNameResponse = 1046;
	public final static int UpdateItemSchema = 1049;
	public final static int RequestInventoryRefresh = 1050;
	public final static int RemoveCustomTexture = 1051;
	public final static int RemoveCustomTextureResponse = 1052;
	public final static int RemoveItemMakersMark = 1053;
	public final static int RemoveItemMakersMarkResponse = 1054;
	public final static int RemoveUniqueCraftIndex = 1055;
	public final static int RemoveUniqueCraftIndexResponse = 1056;
	public final static int SaxxyBroadcast = 1057;
	public final static int BackpackSortFinished = 1058;
	public final static int AdjustItemEquippedState = 1059;
	public final static int RequestItemSchemaData = 1060;
	public final static int ApplyConsumableEffects = 1069;
	public final static int ConsumableExhausted = 1070;
	public final static int ShowItemsPickedUp = 1071;

	public final static int ApplyStrangePart = 1073;

	public final static int Trading_InitiateTradeRequest = 1501;
	public final static int Trading_InitiateTradeResponse = 1502;
	public final static int Trading_StartSession = 1503;
	public final static int Trading_SetItem = 1504;
	public final static int Trading_RemoveItem = 1505;
	public final static int Trading_UpdateTradeInfo = 1506;
	public final static int Trading_SetReadiness = 1507;
	public final static int Trading_ReadinessResponse = 1508;
	public final static int Trading_SessionClosed = 1509;
	public final static int Trading_CancelSession = 1510;
	public final static int Trading_TradeChatMsg = 1511;
	public final static int Trading_ConfirmOffer = 1512;
	public final static int Trading_TradeTypingChatMsg = 1513;

	public final static int ServerBrowser_FavoriteServer = 1601;
	public final static int ServerBrowser_BlacklistServer = 1602;

	public final static int CheckItemPreviewStatus = 1701;

	public final static int Dev_NewItemRequest = 2001;
	public final static int Dev_NewItemReqeustResponse = 2002;

	public final static int StoreGetUserData = 2500;
	public final static int StoreGetUserDataResponse = 2501;
	public final static int StorePurchaseInitDEPRECATED = 2502;
	public final static int StorePurchaseInitResponseDEPRECATED = 2503;
	public final static int StorePurchaseFinalize = 2504;
	public final static int StorePurchaseFinalizeResponse = 2505;
	public final static int StorePurchaseCancel = 2506;
	public final static int StorePurchaseCancelResponse = 2507;
	public final static int StorePurchaseQueryTxn = 2508;
	public final static int StorePurchaseQueryTxnResponse = 2509;
	public final static int StorePurchaseInit = 2510;
	public final static int StorePurchaseInitResponse = 2511;

	public final static int SystemMessage = 4001;
	public final static int ReplicateConVars = 4002;
	public final static int ConVarUpdated = 4003;
	public final static int ClientWelcome = 4004;
	public final static int ClientHello = 4006;
	public final static int InQueue = 4008;

	public final static int InviteToParty = 4501;
	public final static int InvitationCreated = 4502;
	public final static int PartyInviteResponse = 4503;
	public final static int KickFromParty = 4504;
	public final static int LeaveParty = 4505;

	public final static int GCError = 4509;
}
