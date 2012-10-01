package uk.co.thomasc.steamkit.util.logging;

public class GenericDebugListener implements IDebugListener {

	@Override
	public void writeLine(String category, String msg) {
		final String[] lines = msg.split(System.getProperty("line.separator"));
		for (final String line : lines) {
			System.out.println("[" + category + "] " + line);
		}
	}

}
