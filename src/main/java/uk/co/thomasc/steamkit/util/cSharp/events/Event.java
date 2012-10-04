package uk.co.thomasc.steamkit.util.cSharp.events;

import java.util.HashSet;

public class Event<T> {
	protected final HashSet<EventHandler<T>> handlers = new HashSet<EventHandler<T>>();

	public void addEventHandler(EventHandler<T> handler) {
		handlers.add(handler);
	}

	public void removeEventHandler(EventHandler<T> handler) {
		handlers.remove(handler);
	}

	public void handleEvent(Object sender, T e) {
		for (final EventHandler<T> handler : handlers) {
			handler.handleEvent(sender, e);
		}
	}
}
