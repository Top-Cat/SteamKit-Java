package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class ChallengeData implements ISteamSerializable {
	public static final int CHALLENGE_MASK = 0xA426DF2B;
	// Static size: 4
	public int challengeValue = 0;
	// Static size: 4
	public int serverLoad = 0;

	public ChallengeData() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(challengeValue);
		stream.write(serverLoad);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		challengeValue = stream.readInt();
		serverLoad = stream.readInt();
	}
}
