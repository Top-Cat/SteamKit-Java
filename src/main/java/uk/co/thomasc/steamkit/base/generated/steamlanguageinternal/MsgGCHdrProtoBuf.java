package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.SteammessagesBase.CMsgProtoBufHeader;
import uk.co.thomasc.steamkit.base.generated.SteammessagesBase.CMsgProtoBufHeader.Builder;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;
import uk.co.thomasc.steamkit.util.util.MsgUtil;

public class MsgGCHdrProtoBuf implements IGCSerializableHeader {

	@Override
	public void setEMsg(int msg) {
		this.msg = msg;
	}

	// Static size: 4
	public int msg = 0;
	// Static size: 4
	public int headerLength = 0;
	// Static size: 0
	public Builder proto = CMsgProtoBufHeader.newBuilder();

	public MsgGCHdrProtoBuf() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		final byte[] msProto = proto.build().toByteArray();

		headerLength = msProto.length;

		stream.write(MsgUtil.makeGCMsg(msg, true));
		stream.write(headerLength);
		stream.write(msProto);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		msg = MsgUtil.getGCMsg(stream.readInt());
		headerLength = stream.readInt();

		proto.mergeFrom(stream.readBytes(headerLength));
	}
}
