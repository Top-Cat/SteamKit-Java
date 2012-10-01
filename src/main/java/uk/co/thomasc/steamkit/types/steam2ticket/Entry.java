package uk.co.thomasc.steamkit.types.steam2ticket;

import java.io.IOException;

import lombok.Getter;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a single data entry within the ticket container.
 */
public final class Entry {
	/**
	 * Gets the magic.
	 */
	@Getter private short magic; // 0x0400, probably entry magic? idk

	/**
	 * Gets the index of this entry.
	 */
	@Getter private int index;

	/**
	 * Gets the data of this entry.
	 */
	@Getter private byte[] data;

	public void deSerialize(BinaryReader stream) throws IOException {
		magic = stream.readShort();

		final int length = stream.readInt();
		index = stream.readInt();

		data = stream.readBytes(length);
	}
}
