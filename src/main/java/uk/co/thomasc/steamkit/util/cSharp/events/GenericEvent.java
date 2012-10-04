package uk.co.thomasc.steamkit.util.cSharp.events;

public class GenericEvent extends Event<EventArgs> {

	@Override
	public void handleEvent(Object sender, EventArgs e) {
		handleEvent(sender);
	}

	public void handleEvent(Object sender) {
		for (final EventHandler<EventArgs> handler : handlers) {
			handler.handleEvent(sender, null);
		}
	}

}
