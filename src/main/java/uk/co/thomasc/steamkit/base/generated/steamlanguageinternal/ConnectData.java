package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class ConnectData implements ISteamSerializable {
	public static final int CHALLENGE_MASK = ChallengeData.CHALLENGE_MASK;
	// Static size: 4
	public int ChallengeValue = 0;

	public ConnectData() {
		ChallengeValue = 0;
	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(ChallengeValue);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		ChallengeValue = stream.readInt();
	}
}
