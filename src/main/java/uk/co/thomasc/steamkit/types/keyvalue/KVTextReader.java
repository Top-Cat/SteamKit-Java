package uk.co.thomasc.steamkit.types.keyvalue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import uk.co.thomasc.steamkit.util.Passable;

class KVTextReader extends BufferedReader {
	static Map<Character, Character> escapedMapping = new HashMap<Character, Character>();
	static {
		KVTextReader.escapedMapping.put('n', '\n');
		KVTextReader.escapedMapping.put('r', '\r');
		KVTextReader.escapedMapping.put('t', '\t');
		// TODO: any others?
	}

	public KVTextReader(KeyValue kv, BufferedInputStream input) throws IOException {
		super(new InputStreamReader(input));

		final Passable<Boolean> wasQuoted = new Passable<Boolean>(false);
		final Passable<Boolean> wasConditional = new Passable<Boolean>(false);

		KeyValue currentKey = kv;

		do {
			//boolean bAccepted = true;

			String s = readToken(wasQuoted, wasConditional);

			if (s == null || s.length() == 0) {
				break;
			}

			if (currentKey == null) {
				currentKey = new KeyValue(s);
			} else {
				currentKey.name = s;
			}

			s = readToken(wasQuoted, wasConditional);

			if (wasConditional.getValue()) {
				//bAccepted = (s == "[$WIN32]");

				// Now get the '{'
				s = readToken(wasQuoted, wasConditional);
			}

			if (s.startsWith("{") && !wasQuoted.getValue()) {
				// header is valid so load the file
				currentKey.recursiveLoadFromBuffer(this);
			} else {
				throw new IOException("LoadFromBuffer: missing {");
			}

			currentKey = null;
		} while (input.available() > 0);
	}

	private Character peek() throws IOException {
		mark(1);
		final int i = read();
		reset();
		return (char) i;
	}

	private void eatWhiteSpace() throws IOException {
		while (peek() != null) {
			if (!Character.isWhitespace(peek())) {
				break;
			}

			read();
		}
	}

	private boolean eatCPPComment() throws IOException {
		if (peek() != null) {
			final char next = peek();
			if (next == '/') {
				read();
				if (next == '/') {
					readLine();
					return true;
				} else {
					throw new IOException("BARE / WHAT ARE YOU DOIOIOIINODGNOIGNONGOIGNGGGGGGG");
				}
			}
		}
		return false;
	}

	public String readToken(Passable<Boolean> wasQuoted, Passable<Boolean> wasConditional) throws IOException {
		wasQuoted.setValue(false);
		wasConditional.setValue(false);

		while (true) {
			eatWhiteSpace();

			if (peek() == null) {
				return null;
			}

			if (!eatCPPComment()) {
				break;
			}
		}

		if (peek() == null) {
			return null;
		}

		char next = peek();
		if (next == '"') {
			wasQuoted.setValue(true);

			// "
			read();

			final StringBuilder sb = new StringBuilder();
			while (peek() != null) {
				if (peek() == '\\') {
					read();

					final char escapedChar = (char) read();

					if (KVTextReader.escapedMapping.containsKey(escapedChar)) {
						sb.append(KVTextReader.escapedMapping.get(escapedChar));
					} else {
						sb.append(escapedChar);
					}

					continue;
				}

				if (peek() == '"') {
					break;
				}

				sb.append((char) read());
			}

			// "
			read();

			return sb.toString();
		}

		if (next == '{' || next == '}') {
			read();
			return "" + next;
		}

		boolean bConditionalStart = false;
		final int count = 0;
		final StringBuilder ret = new StringBuilder();
		while (peek() != null) {
			next = peek();

			if (next == '"' || next == '{' || next == '}') {
				break;
			}

			if (next == '[') {
				bConditionalStart = true;
			}

			if (next == ']' && bConditionalStart) {
				wasConditional.setValue(true);
			}

			if (Character.isWhitespace(next)) {
				break;
			}

			if (count < 1023) {
				ret.append(next);
				//} else { TODO: Work out why this was here :S
				//	throw new IOException("ReadToken overflow");
			}

			read();
		}

		return ret.toString();
	}
}
