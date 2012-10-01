package uk.co.thomasc.steamkit.types.gameid;

import java.util.HashMap;

/**
 * Represents various types of games.
 */
public enum GameType {
	/**
	 * A Steam application.
	 */
	App(0),
	/**
	 * A game modification.
	 */
	GameMod(1),
	/**
	 * A shortcut to a program.
	 */
	Shortcut(2),
	/**
	 * A peer-to-peer file.
	 */
	P2P(3), ;

	private int code;

	private GameType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}

	private static HashMap<Integer, GameType> values = new HashMap<Integer, GameType>();

	static {
		for (final GameType type : GameType.values()) {
			GameType.values.put(type.v(), type);
		}
	}

	public static GameType fromCode(int code) {
		return GameType.values.get(code);
	}
}
