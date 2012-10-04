package uk.co.thomasc.steamkit.util.logging;

public class Debug {

	public static void Assert(boolean condition) {
		Debug.Assert(condition, "A condition was not met!");
	}

	public static void Assert(boolean condition, String comment) {
		if (!condition) {
			System.out.println(comment);
		}
	}

}
