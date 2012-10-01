package uk.co.thomasc.steamkit.util.logging;

import java.util.HashSet;
import java.util.Set;

public class DebugLog {

	private static Set<IDebugListener> listeners = new HashSet<IDebugListener>();
	
	public static void addListener(IDebugListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(IDebugListener listener) {
		listeners.add(listener);
	}
	
	public static void writeLine(String category, String msg, Object... args) {
		writeLine(category, String.format(msg, args));
	}
	
	public static void writeLine(String category, String msg) {
		for (IDebugListener listener : listeners) {
			listener.writeLine(category, msg);
		}
	}
	
}