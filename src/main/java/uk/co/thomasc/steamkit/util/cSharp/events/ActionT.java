package uk.co.thomasc.steamkit.util.cSharp.events;

public abstract class ActionT<T> extends Action {

	@Override
	public void call() {
		call(null);
	}

	public abstract void call(T obj);
}
