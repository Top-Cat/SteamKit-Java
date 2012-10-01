package uk.co.thomasc.steamkit.util;

import lombok.Getter;
import lombok.Setter;

public class Passable<T> {

	@Getter @Setter private T value;

	public Passable(T value) {
		this.value = value;
	}

}
