package uk.co.thomasc.steamkit.types.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * The base class used for wrapping common ulong types, to introduce type safety and distinguish between common types.
 */
public abstract class UInt64Handle {

	/**
	 * The value
	 */
	@Getter @Setter protected Long value;

	/**
	 * Initializes a new instance of the {@link UInt64Handle} class.
	 */
	public UInt64Handle() {
	}

	/**
	 * Initializes a new instance of the {@link UInt64Handle} class.
	 * @param value	The value to initialize this handle to.
	 */
	protected UInt64Handle(long value) {
		this.value = value;
	}

	/**
	 * Returns a hash code for this instance.
	 */
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Determines whether the specified {@link Object} is equal to this instance.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof UInt64Handle)) {
			return false;
		}

		return ((UInt64Handle) obj).value.equals(value);
	}

	/**
	 * Returns a {@link String} that represents this instance.
	 */
	@Override
	public String toString() {
		return value.toString();
	}

}
