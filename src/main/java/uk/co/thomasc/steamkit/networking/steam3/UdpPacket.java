package uk.co.thomasc.steamkit.networking.steam3;

import java.io.IOException;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUdpPacketType;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.UdpHeader;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

class UdpPacket {
	public final static int MAX_PAYLOAD = 0x4DC;

	@Getter private UdpHeader header;
	@Getter private BinaryReader payload;

	//public MemoryStream Payload { get; private set; }

	/**
	 * Gets a value indicating whether this instance is valid.
	 * @return true if this instance is valid; otherwise, false.
	 */
	public boolean isValid() {
		return header.magic == UdpHeader.MAGIC && header.payloadSize <= UdpPacket.MAX_PAYLOAD && payload != null;
	}

	/**
	 * Initializes a new instance of the {@link UdpPacket} class with
	 * information from the memory stream.
	 * 
	 * Header is populated from the MemoryStream
	 * @param ms	The stream containing the packet and it's payload data.
	 */
	public UdpPacket(BinaryReader ms) {
		header = new UdpHeader();

		try {
			header.deSerialize(ms);
		} catch (final IOException e) {
			return;
		}

		if (header.magic != UdpHeader.MAGIC) {
			return;
		}

		setPayload(ms);
	}

	/**
	 * Initializes a new instance of the {@link UdpPacket} class, with
	 * no payload.
	 * 
	 * Header must be populated manually.
	 * @param type	The type.
	 */
	public UdpPacket(EUdpPacketType type) {
		header = new UdpHeader();
		payload = new BinaryReader(new byte[0]);

		header.packetType = type;
	}

	/**
	 * Initializes a new instance of the {@link UdpPacket} class, of the
	 * specified type containing the specified payload.
	 * 
	 * Header must be populated manually.
	 * @param type		The type.
	 * @param reader	The payload.
	 */
	public UdpPacket(EUdpPacketType type, BinaryReader reader) {
		this(type);
		setPayload(reader);
	}

	/**
	 * Initializes a new instance of the {@link UdpPacket} class, of the
	 * specified type containing the first 'length' bytes of specified payload.
	 * 
	 * Header must be populated manually.
	 * @param type		The type.
	 * @param payload	The payload.
	 * @param length	The length.
	 */
	public UdpPacket(EUdpPacketType type, BinaryReader payload, int length) {
		this(type);
		setPayload(payload, length);
	}

	/**
	 * Sets the payload
	 * @param ms	The payload to copy.
	 */
	public void setPayload(BinaryReader ms) {
		setPayload(ms, ms.getRemaining());
	}

	/**
	 * Sets the payload.
	 * @param ms		The payload.
	 * @param length	The length.
	 */
	public void setPayload(BinaryReader ms, int length) {
		if (length > UdpPacket.MAX_PAYLOAD) {
			throw new IllegalArgumentException("Payload length exceeds 0x4DC maximum");
		}

		try {
			final byte[] buf = ms.readBytes(length);

			payload = new BinaryReader(buf);
			header.payloadSize = (short) buf.length;
			header.msgSize = buf.length;
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Serializes the UdpPacket.
	 * @return The serialized packet.
	 */
	public byte[] getData() {
		final BinaryWriter ms = new BinaryWriter();

		try {
			header.serialize(ms);

			ms.write(payload.readBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return ms.toByteArray();
	}

}
