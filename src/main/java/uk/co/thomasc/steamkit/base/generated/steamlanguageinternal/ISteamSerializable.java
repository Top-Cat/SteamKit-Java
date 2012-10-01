package uk.co.thomasc.steamkit.base.generated.steamlanguageinternal;

import java.io.IOException;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public interface ISteamSerializable {
	void serialize(BinaryWriter stream) throws IOException;

	void deSerialize(BinaryReader stream) throws IOException;
}
