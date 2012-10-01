package uk.co.thomasc.steamkit.types.keyvalue;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.thomasc.steamkit.util.Passable;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

import lombok.Getter;

/**
 * Represents a recursive string key to arbitrary value container.
 */
public class KeyValue {
	/**
	 * Initializes a new instance of the {@link KeyValue} class.
	 * @param name	The optional name of the root key.
	 * @param value	The optional value assigned to the root key.
	 */
	public KeyValue(String name, String value) {
		this.name = name;
		this.value = value;

		children = new ArrayList<KeyValue>();
	}
	
	public KeyValue(String name) {
		this(null, null);
	}
	
	public KeyValue() {
		this(null);
	}

	/**
	 * Represents an invalid {@link KeyValue} given when a searched for child does not exist.
	 */
	public final static KeyValue Invalid = new KeyValue();

	/**
	 * Gets or sets the name of this instance.
	 */
	public String name;

	/**
	 * Gets or sets the value of this instance.
	 */
	public String value;

	/**
	 * Gets the children of this instance.
	 */
	@Getter private List<KeyValue> children;

	/**
	 * Gets the child {@link KeyValue} with the specified key.
	 * If no child with this key exists, {@link #Invalid} is returned.
	 */
	public KeyValue get(String key) {
		for (KeyValue child : children) {
			if (child.name.equalsIgnoreCase(key)) {
				return child;
			}
		}
		return Invalid;
	}

	/**
	 * Returns the value of this instance as a string.
	 * @return The value of this instance as a string.
	 */
	public String asString() {
		return this.value;
	}

	/**
	 * Attempts to convert and return the value of this instance as an integer.
	 * If the conversion is invalid, the default value is returned.
	 * @param defaultValue	The default value to return if the conversion is invalid.
	 * @return The value of this instance as an integer.
	 */
	public int asInteger(int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public int asInteger() {
		return asInteger(0);
	}

	/**
	 * Attempts to convert and return the value of this instance as a long.
	 * If the conversion is invalid, the default value is returned.
	 * 
	 * @param defaultValue	The default value to return if the conversion is invalid.
	 * @return The value of this instance as an long.
	 */
	public long asLong(long defaultValue) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public long asLong() {
		return asLong(0);
	}

	/**
	 * Attempts to convert and return the value of this instance as a float.
	 * If the conversion is invalid, the default value is returned.
	 * @param defaultValue	The default value to return if the conversion is invalid.
	 * @return The value of this instance as an float.
	 */
	public float asFloat(float defaultValue) {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public float asFloat() {
		return asFloat(0);
	}

	/**
	 * Attempts to convert and return the value of this instance as a boolean.
	 * If the conversion is invalid, the default value is returned.
	 * @return The value of this instance as an boolean.
	 */
	public boolean asBoolean() {
		return Boolean.parseBoolean(value);
	}

	/**
	 * Returns a {@link String} that represents this instance.
	 */
	@Override
	public String toString() {
		return String.format("%s = %s", this.name, this.value);
	}

	/**
	 * Attempts to load the given filename as a text {@link KeyValue}.
	 * This method will swallow any exceptions that occur when reading, use {@link #readFileAsText(String)} if you wish to handle exceptions.
	 * @param path	The path to the file to load.
	 * @return a {@link KeyValue} instance if the load was successful, or null on failure.
	 */
	public static KeyValue loadAsText(String path) {
		return loadFromFile(path, false);
	}

	/**
	 * Attempts to load the given filename as a binary {@link KeyValue}.
	 * This method will swallow any exceptions that occur when reading, use {@link #readAsBinary(BinaryReader)} if you wish to handle exceptions.
	 * @param path	The path to the file to load.
	 * @return a {@link KeyValue} instance if the load was successful, or null on failure.
	 */
	public static KeyValue loadAsBinary(String path) {
		return loadFromFile(path, true);
	}


	static KeyValue loadFromFile(String path, boolean asBinary) {
		File file = new File(path);
		
		if (file.exists() == false) {
			return null;
		}

		try {
			FileInputStream fstream = new FileInputStream(file);
			KeyValue kv = new KeyValue();
			
			if (asBinary) {
				kv.readAsBinary(new BinaryReader(fstream));
			} else {
				kv.readAsText(fstream);
			}
			
			return kv;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Attempts to create an instance of {@link KeyValue} from the given input text.
	 * This method will swallow any exceptions that occur when reading, use {@link #readAsText(InputStream)} if you wish to handle exceptions.
	 * @param input	The input text to load.
	 * @return a {@link KeyValue} instance if the load was successful, or null on failure.
	 */
	public static KeyValue loadFromString(String input) {
		ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
		
		KeyValue kv = new KeyValue();
		try {
			if (kv.readAsText(stream)) {
				return kv;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Populate this instance from the given {@link InputStream} as a text {@link KeyValue}.
	 * @param input	The input {@link InputStream} to read from.
	 * @return true if the read was successful; otherwise, false.
	 * @throws IOException 
	 */
	public boolean readAsText(InputStream input) throws IOException {
		this.children = new ArrayList<KeyValue>();

		/*KVTextReader kvr = */new KVTextReader(this, new BufferedInputStream(input));

		return true;
	}

	/**
	 * Opens and reads the given filename as text.
	 * @param filename	The file to open and read.
	 * @return true if the read was successful; otherwise, false.
	 * @throws {@link FileNotFoundException} , {@link IOException} 
	 */
	public boolean readFileAsText(String filename) throws FileNotFoundException, IOException {
		FileInputStream stream = new FileInputStream(new File(filename));
		return readAsText(stream);
	}

	public void recursiveLoadFromBuffer(KVTextReader kvr) throws IOException {
		Passable<Boolean> wasQuoted = new Passable<Boolean>(false);
		Passable<Boolean> wasConditional = new Passable<Boolean>(false);

		while (true) {
			//boolean bAccepted = true;

			// get the key name
			String name = kvr.readToken(wasQuoted, wasConditional);

			if (name == null || name.length() == 0) {
				throw new IOException("RecursiveLoadFromBuffer: got EOF or empty keyname");
			}

			if (name.startsWith("}") && !wasQuoted.getValue()) { // top level closed, stop reading
				break;
			}

			KeyValue dat = new KeyValue(name);
			this.children.add(dat);

			// get the value
			String value = kvr.readToken(wasQuoted, wasConditional);

			if (wasConditional.getValue() && value != null) {
				//bAccepted = (value == "[$WIN32]");
				value = kvr.readToken(wasQuoted, wasConditional);
			}

			if (value == null) {
				throw new IOException("RecursiveLoadFromBuffer:  got NULL key");
			}

			if (value.startsWith("}") && !wasQuoted.getValue()) {
				throw new IOException("RecursiveLoadFromBuffer:  got } in key");
			}

			if (value.startsWith("{") && !wasQuoted.getValue()) {
				dat.recursiveLoadFromBuffer(kvr);
			} else {
				if (wasConditional.getValue()) {
					throw new IOException("RecursiveLoadFromBuffer:  got conditional between key and value");
				}

				dat.value = value;
				// blahconditionalsdontcare
			}
		}
	}

	/**
	 * Saves this instance to file.
	 * @param path		The file path to save to.
	 * @param asBinary	If set to true, saves this instance as binary.
	 */
	public void saveToFile(String path, boolean asBinary) {
		if (asBinary) {
			throw new UnsupportedOperationException();
		}

		File file = new File(path);
		try {
			file.createNewFile();
			recursiveSaveToFile(new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recursiveSaveToFile(FileOutputStream f) throws IOException {
		recursiveSaveToFile(f, 0);
	}

	private void recursiveSaveToFile(FileOutputStream f, int indentLevel ) throws IOException {
		// write header
		writeIndents(f, indentLevel);
		writeString(f, name, true);
		writeString(f, "\n");
		writeIndents(f, indentLevel);
		writeString(f, "{\n");

		// loop through all our keys writing them to disk
		for (KeyValue child : children) {
			if (child.value == null) {
				child.recursiveSaveToFile(f, indentLevel + 1);
			} else {
				writeIndents(f, indentLevel + 1);
				writeString(f, child.name, true);
				writeString(f, "\t\t");
				writeString(f, child.asString(), true);
				writeString(f, "\n");
			}
		}

		writeIndents(f, indentLevel);
		writeString(f, "}\n");
	}

	private void writeIndents(FileOutputStream f, int indentLevel) throws IOException {
		for (int i=0;i<indentLevel;i++) {
			writeString(f, "\t");
		}
	}

	private static void writeString(FileOutputStream f, String str, boolean quote) throws IOException {
		byte[] bytes = ((quote ? "\"" : "").getBytes() + str.replace("\"", "\\\"") + (quote ? "\"" : "")).getBytes();
		f.write(bytes, 0, bytes.length);
	}

	private static void writeString(FileOutputStream f, String str) throws IOException {
		writeString(f, str, false);
	}
	
	public static String readNullTermString(InputStream in) throws IOException {
		int rb, i = 0;
		byte[] res = new byte[1024];
		while ((rb = in.read()) != 0 && rb != -1) {
			res[i++] = (byte) rb;
		}
		return new String(res, 0, i);
	}

	/**
	 * Populate this instance from the given {@link BinaryReader} as a binary {@link KeyValue}.
	 * @param input	The input {@link BinaryReader} to read from.
	 * @return true if the read was successful; otherwise, false.
	 * @throws IOException 
	 */
	public boolean readAsBinary(BinaryReader input) throws IOException {
		while (true) {
			Type type = Type.f(input.readByte());

			if (type == Type.End) {
				break;
			}

			KeyValue current = new KeyValue();
			current.name = input.readString();

			try {
				switch (type) {
					case None:
						current.readAsBinary(input);
						break;
					case String:
						current.value = input.readString();
						break;
					case WideString:
						throw new IOException("wstring is unsupported");
					case Int32:
					case Color:
					case Pointer:
						current.value = String.valueOf(input.readInt());
						break;
					case UInt64:
						current.value = String.valueOf(input.readLong());
						break;
					case Float32:
						current.value = String.valueOf(input.readFloat());
						break;
					default:
						throw new IOException("Unknown KV type encountered.");
				}
			} catch (IOException e) {
				throw new IOException(String.format("An exception ocurred while reading KV '%s'", current.name), e);
			}

			this.children.add(current);
		}

		return input.isAtEnd();
	}
}