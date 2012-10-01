package uk.co.thomasc.steamkit.util.crypto.asnkeyparser;

import java.util.List;

class AsnParser {
	final int _initialCount;
	final List<Byte> _octets;

	public AsnParser(List<Byte> values) {
		_octets = values;

		_initialCount = _octets.size();
	}

	public int currentPosition() {
		return _initialCount - _octets.size();
	}

	public int remainingBytes() {
		return _octets.size();
	}

	int GetLength() throws BerDecodeException {
		int length = 0;

		// Checkpoint
		final int position = currentPosition();

		final byte b = GetNextOctet();

		if (b == (b & 0x7f)) {
			return b;
		}
		int i = b & 0x7f;

		if (i > 4) {
			final StringBuilder sb = new StringBuilder("Invalid Length Encoding. ");
			sb.append(String.format("Length uses %d _octets", i));
			throw new BerDecodeException(sb.toString(), position);
		}

		while (0 != i--) {
			// shift left
			length <<= 8;

			length |= GetNextOctet();
		}

		return length & 0xFF;
	}

	public byte[] Next() throws BerDecodeException {
		final int position = currentPosition();

		/*byte b = */GetNextOctet();

		final int length = GetLength();
		if (length > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", length, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		return GetOctets(length);
	}

	public byte GetNextOctet() throws BerDecodeException {
		final int position = currentPosition();

		if (0 == remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", "1", remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		final byte b = GetOctets(1)[0];

		return b;
	}

	public byte[] GetOctets(int octetCount) throws BerDecodeException {
		final int position = currentPosition();

		if (octetCount > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", octetCount, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		final byte[] values = new byte[octetCount];

		for (int i = 0; i < octetCount; i++) {
			values[i] = _octets.remove(0);
		}

		return values;
	}

	public boolean IsNextNull() {
		return 0x05 == _octets.get(0);
	}

	public int NextNull() throws BerDecodeException {
		final int position = currentPosition();

		byte b = GetNextOctet();
		if (0x05 != b) {
			final StringBuilder sb = new StringBuilder("Expected Null. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		// Next octet must be 0
		b = GetNextOctet();
		if (0x00 != b) {
			final StringBuilder sb = new StringBuilder("Null has non-zero size. ");
			sb.append(String.format("Size: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		return 0;
	}

	public boolean IsNextSequence() {
		return 0x30 == _octets.get(0);
	}

	public int nextSequence() throws BerDecodeException {
		final int position = currentPosition();

		final byte b = GetNextOctet();
		if (0x30 != b) {
			final StringBuilder sb = new StringBuilder("Expected Sequence. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		final int length = GetLength();
		if (length > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Sequence Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", length, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		return length;
	}

	public boolean IsNextOctetString() {
		return 0x04 == _octets.get(0);
	}

	public int NextOctetString() throws BerDecodeException {
		final int position = currentPosition();

		final byte b = GetNextOctet();
		if (0x04 != b) {
			final StringBuilder sb = new StringBuilder("Expected Octet String. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		final int length = GetLength();
		if (length > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Octet String Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", length, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		return length;
	}

	public boolean IsNextBitString() {
		return 0x03 == _octets.get(0);
	}

	public int NextBitString() throws BerDecodeException {
		final int position = currentPosition();

		byte b = GetNextOctet();
		if (0x03 != b) {
			final StringBuilder sb = new StringBuilder("Expected Bit String. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		int length = GetLength();

		// We need to consume unused bits, which is the first
		//   octet of the remaing values
		b = _octets.get(0);
		_octets.remove(0);
		length--;

		if (0x00 != b) {
			throw new BerDecodeException("The first octet of BitString must be 0", position);
		}

		return length;
	}

	public boolean IsNextInteger() {
		return 0x02 == _octets.get(0);
	}

	public byte[] nextInteger() throws BerDecodeException {
		final int position = currentPosition();

		final byte b = GetNextOctet();
		if (0x02 != b) {
			final StringBuilder sb = new StringBuilder("Expected Integer. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		final int length = GetLength();
		if (length > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Integer Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", length, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		return GetOctets(length);
	}

	public byte[] nextOID() throws BerDecodeException {
		final int position = currentPosition();

		final byte b = GetNextOctet();
		if (0x06 != b) {
			final StringBuilder sb = new StringBuilder("Expected Object Identifier. ");
			sb.append(String.format("Specified Identifier: %d", b));
			throw new BerDecodeException(sb.toString(), position);
		}

		final int length = GetLength();
		if (length > remainingBytes()) {
			final StringBuilder sb = new StringBuilder("Incorrect Object Identifier Size. ");
			sb.append(String.format("Specified: %d, Remaining: %d", length, remainingBytes()));
			throw new BerDecodeException(sb.toString(), position);
		}

		final byte[] values = new byte[length];

		for (int i = 0; i < length; i++) {
			values[i] = _octets.get(0);
			_octets.remove(0);
		}

		return values;
	}
}
