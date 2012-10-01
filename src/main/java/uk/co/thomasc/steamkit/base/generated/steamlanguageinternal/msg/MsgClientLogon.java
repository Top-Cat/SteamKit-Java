package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.msg;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.ISteamSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class MsgClientLogon implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientLogon;
	}

	public static final int ObfuscationMask = 0xBAADF00D;
	public static final int CurrentProtocol = 65575;
	public static final int ProtocolVerMajorMask = 0xFFFF0000;
	public static final int ProtocolVerMinorMask = 0xFFFF;
	public static final short ProtocolVerMinorMinGameServers = 4;
	public static final short ProtocolVerMinorMinForSupportingEMsgMulti = 12;
	public static final short ProtocolVerMinorMinForSupportingEMsgClientEncryptPct = 14;
	public static final short ProtocolVerMinorMinForExtendedMsgHdr = 17;
	public static final short ProtocolVerMinorMinForCellId = 18;
	public static final short ProtocolVerMinorMinForSessionIDLast = 19;
	public static final short ProtocolVerMinorMinForServerAvailablityMsgs = 24;
	public static final short ProtocolVerMinorMinClients = 25;
	public static final short ProtocolVerMinorMinForOSType = 26;
	public static final short ProtocolVerMinorMinForCegApplyPESig = 27;
	public static final short ProtocolVerMinorMinForMarketingMessages2 = 27;
	public static final short ProtocolVerMinorMinForAnyProtoBufMessages = 28;
	public static final short ProtocolVerMinorMinForProtoBufLoggedOffMessage = 28;
	public static final short ProtocolVerMinorMinForProtoBufMultiMessages = 28;
	public static final short ProtocolVerMinorMinForSendingProtocolToUFS = 30;
	public static final short ProtocolVerMinorMinForMachineAuth = 33;

	public MsgClientLogon() {
	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {

	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {

	}
}
