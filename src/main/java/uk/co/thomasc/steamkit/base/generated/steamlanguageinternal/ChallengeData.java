package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class ChallengeData implements ISteamSerializable {
	public static final int CHALLENGE_MASK = 0xA426DF2B;
	// Static size: 4
	public int ChallengeValue = 0;
	// Static size: 4
	public int ServerLoad = 0;

	public ChallengeData() {
		
	}

	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(ChallengeValue);
		stream.write(ServerLoad);
	}

	public void deSerialize(BinaryReader stream) throws IOException {
		ChallengeValue = stream.readInt();
		ServerLoad = stream.readInt();
	}
}