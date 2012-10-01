package uk.co.thomasc.steamkit.util.util;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EMsg;

public class MsgUtil {
	private static final int ProtoMask = 0x80000000;
	private static final int EMsgMask = ~MsgUtil.ProtoMask;

	public static EMsg getMsg(int msg) {
		return EMsg.f(msg & MsgUtil.EMsgMask);
	}

	/**
	 * Strips off the protobuf message flag and returns an EMsg.
	 * @param msg	The message number.
	 * @return The underlying EMsg.
	 */
	public static int getGCMsg(int msg) {
		return msg & MsgUtil.EMsgMask;
	}

	/**
	 * Strips off the protobuf message flag and returns an EMsg.
	 * @param msg	The message number.
	 * @return The underlying EMsg.
	 */
	public static EMsg getMsg(EMsg msg) {
		return MsgUtil.getMsg(msg.v());
	}

	/**
	 * Determines whether message is protobuf flagged.
	 * @param msg	The message.
	 * @return true if this message is protobuf flagged; otherwise, false
	 */
	public static boolean isProtoBuf(int msg) {
		return (msg & 0xffffffffL & MsgUtil.ProtoMask) > 0;
	}

	/**
	 * Determines whether message is protobuf flagged.
	 * @param msg	The message.
	 * @return true if this message is protobuf flagged; otherwise, false
	 */
	public static boolean isProtoBuf(EMsg msg) {
		return MsgUtil.isProtoBuf(msg.v());
	}

	/**
	 * Crafts an EMsg, flagging it if required.
	 * @param msg		The EMsg to flag.
	 * @param protobuf	if set to true, the message is protobuf flagged.
	 * @return A crafted EMsg, flagged if requested.
	 */
	public static int makeMsg(int msg, boolean protobuf) {
		if (protobuf) {
			return msg | MsgUtil.ProtoMask;
		}

		return msg;
	}

	public int makeMsg(int msg) {
		return MsgUtil.makeMsg(msg, false);
	}

	/**
	 * Crafts an EMsg, flagging it if required.
	 * @param msg		The EMsg to flag.
	 * @param protobuf	if set to true, the message is protobuf flagged.
	 * @return A crafted EMsg, flagged if requested.
	 */
	public static int makeGCMsg(int msg, boolean protobuf) {
		if (protobuf) {
			return msg | MsgUtil.ProtoMask;
		}

		return msg;
	}

	public static int makeGCMsg(int msg) {
		return MsgUtil.makeGCMsg(msg, false);
	}
}
