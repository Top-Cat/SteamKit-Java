package uk.co.thomasc.steamkit.util.cSharp.events;

public interface EventHandler<T> {
	void handleEvent(Object sender, T e);
}
