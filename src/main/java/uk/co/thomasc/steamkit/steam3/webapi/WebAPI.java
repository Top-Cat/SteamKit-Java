package uk.co.thomasc.steamkit.steam3.webapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.co.thomasc.steamkit.types.keyvalue.KeyValue;
import uk.co.thomasc.steamkit.util.WebHelpers;

/**
 * Utility class for interacting with the Steam Web API.
 */
public final class WebAPI {

	String iface;
	String apiKey;

	final String API_ROOT = "api.steampowered.com";

	public WebAPI(String iface, String apiKey) {
		this.iface = iface;
		this.apiKey = apiKey;
	}

	/**
	 * Manually calls the specified Web API function with the provided details.
	 * @param func		The function name to call.
	 * @param version	The version of the function to call.
	 * @param args		A dictionary of string key value pairs representing arguments to be passed to the API.
	 * @param method	The http request method. Either "POST" or "GET".
	 * @param secure	if set to true this method will be called through the secure API.
	 * @return A {@link KeyValue} object representing the results of the Web API call.
	 * @throws IOException A network error occurred when performing the request.
	 */
	public KeyValue call(String func, int version, Map<String, String> args, String method, boolean secure) throws IOException {
		if (func == null) {
			throw new IllegalArgumentException("func");
		}

		if (args == null) {
			args = new HashMap<String, String>();
		}

		if (method == null) {
			throw new IllegalArgumentException("method");
		}

		final StringBuilder urlBuilder = new StringBuilder();
		StringBuilder paramBuilder = new StringBuilder();

		urlBuilder.append(secure ? "https://" : "http://");
		urlBuilder.append(API_ROOT);
		urlBuilder.append(String.format("/%s/%s/v%d", iface, func, version));

		final boolean isGet = method.equalsIgnoreCase("GET");

		if (isGet) {
			// if we're doing a GET request, we'll build the params onto the url
			paramBuilder = urlBuilder;
			paramBuilder.append("/?"); // start our GET params
		}

		args.put("format", "vdf");

		if (apiKey != null && apiKey.length() > 0) {
			args.put("key", apiKey);
		}

		// append any args
		final Iterator<Entry<String, String>> it = args.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<String, String> entry = it.next();
			paramBuilder.append(String.format("%s=%s&", WebHelpers.UrlEncode(entry.getKey()), entry.getValue()));
		}

		byte[] data = null;

		HttpURLConnection conn;
		if (isGet) {
			conn = (HttpURLConnection) new URL(urlBuilder.toString()).openConnection();
		} else {
			conn = (HttpURLConnection) new URL(urlBuilder.toString()).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", Integer.toString(paramBuilder.toString().getBytes().length));
			final OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
			os.write(paramBuilder.toString());
			os.flush();
		}

		final InputStream is = conn.getInputStream();
		final byte[] tmp = new byte[2048];
		int nRead;
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		while ((nRead = is.read(tmp, 0, tmp.length)) != -1) {
			buffer.write(tmp, 0, nRead);
		}
		data = buffer.toByteArray();

		final KeyValue kv = new KeyValue();

		kv.readAsText(new ByteArrayInputStream(data));

		return kv;
	}

	public KeyValue call(String func, int version, Map<String, String> args, String method) throws IOException {
		return call(func, version, args, method, false);
	}

	public KeyValue call(String func, int version, Map<String, String> args) throws IOException {
		return call(func, version, args, "GET");
	}

	public KeyValue call(String func, int version) throws IOException {
		return call(func, version, null);
	}

	public KeyValue call(String func) throws IOException {
		return call(func, 1);
	}

	public KeyValue authenticateUser(String steamid, String sessionKey, String encryptedLoginKey, String method) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("steamid", steamid);
		map.put("sessionkey", sessionKey);
		map.put("encrypted_loginkey", encryptedLoginKey);
		map.put("method", method);
		return tryInvokeMember("AuthenticateUser", map);
	}

	private KeyValue tryInvokeMember(String functionName, Map<String, String> apiArgs) {
		String requestMethod = "GET";
		if (apiArgs.containsKey("method")) {
			requestMethod = apiArgs.remove("method");
		}
		boolean secure = false;
		if (apiArgs.containsKey("secure")) {
			secure = Boolean.valueOf(apiArgs.remove("method"));
		}

		final int version = 1; // assume version 1 unless specified

		try {
			return call(functionName, version, apiArgs, requestMethod, secure);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
