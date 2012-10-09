package uk.co.thomasc.steamkit.util.cSharp.events;

public abstract class GenericEventHandler implements EventHandler<EventArgs> {

	@Override
	public void handleEvent(Object sender, EventArgs e) {
		handleEvent(sender);
	}

	public abstract void handleEvent(Object sender);

}
