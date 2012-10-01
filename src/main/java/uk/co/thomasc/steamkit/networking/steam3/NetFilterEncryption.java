package uk.co.thomasc.steamkit.networking.steam3;

import uk.co.thomasc.steamkit.util.crypto.CryptoHelper;
import uk.co.thomasc.steamkit.util.logging.Debug;

public class NetFilterEncryption {
	byte[] sessionKey;

	public NetFilterEncryption(byte[] sessionKey) {
		Debug.Assert(sessionKey.length == 32);

		this.sessionKey = sessionKey;
	}

	public byte[] processIncoming(byte[] data) {
		return CryptoHelper.SymmetricDecrypt(data, sessionKey);
	}

	public byte[] processOutgoing(byte[] ms) {
		return CryptoHelper.SymmetricEncrypt(ms, sessionKey);
	}
}
