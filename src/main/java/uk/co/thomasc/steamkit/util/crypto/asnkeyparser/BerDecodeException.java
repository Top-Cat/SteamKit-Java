package uk.co.thomasc.steamkit.util.crypto.asnkeyparser;

import lombok.Getter;

public final class BerDecodeException extends Exception {
	private static final long serialVersionUID = -2265804415271565348L;

	@Getter private final int _position;

	public BerDecodeException() {
		_position = 0;
	}

	public BerDecodeException(String message) {
		super(message);
		_position = 0;
	}

	public BerDecodeException(String message, Exception ex) {
		super(message, ex);
		_position = 0;
	}

	public BerDecodeException(String message, int position) {
		super(message);
		_position = position;
	}

	public BerDecodeException(String message, int position, Exception ex) {
		super(message, ex);
		_position = position;
	}

	@Override
	public String getMessage() {
		final StringBuilder sb = new StringBuilder(super.getMessage());

		sb.append(String.format(" (Position %d)%s", _position, System.getProperty("line.separator")));

		return sb.toString();
	}
}
