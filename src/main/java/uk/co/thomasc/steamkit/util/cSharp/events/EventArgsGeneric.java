package uk.co.thomasc.steamkit.util.cSharp.events;

import lombok.Getter;

public class EventArgsGeneric<T> extends EventArgs {
	@Getter private T value;
	
	public EventArgsGeneric(T value) {
		this.value = value;
	}
}