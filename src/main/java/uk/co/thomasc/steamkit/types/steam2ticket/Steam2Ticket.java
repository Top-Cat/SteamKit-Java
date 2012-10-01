package uk.co.thomasc.steamkit.types.steam2ticket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import uk.co.thomasc.steamkit.util.stream.BinaryReader;

/**
 * Represents a Steam2 authentication ticket container used for downloading authenticated content from Steam2 servers.
 */
public final class Steam2Ticket {

	/**
	 * Gets the magic of the container.
	 */
	@Getter private short magic; // 0x0150, more crazy magic?

	/**
	 * Gets the length, in bytes, of the container.
	 */
	@Getter private int length;

	//private int unknown1;

	/**
	 * Gets the {@link Entry entries} within this container.
	 */
	@Getter private final List<Entry> entries = new ArrayList<Entry>();

	public Steam2Ticket(byte[] blob) {
		final BinaryReader stream = new BinaryReader(blob);

		try {
			magic = stream.readShort();

			length = stream.readInt();
			/*unknown1 = */stream.readInt();

			while (stream.getRemaining() > 0) {
				final Entry entry = new Entry();
				entry.deSerialize(stream);

				entries.add(entry);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
