package uk.co.thomasc.steamkit.util.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import uk.co.thomasc.steamkit.util.crypto.asnkeyparser.AsnKeyParser;
import uk.co.thomasc.steamkit.util.crypto.asnkeyparser.BerDecodeException;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACrypto {
	Cipher cipher;
	RSAPublicKey RSAkey;

	public RSACrypto(byte[] key) {
		try {
			List<Byte> list = new ArrayList<Byte>();
			for (byte b : key) {
				list.add(b);
			}
			AsnKeyParser keyParser = new AsnKeyParser(list);
			BigInteger[] keys = keyParser.parseRSAPublicKey();
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(keys[0], keys[1]);
			
			KeyFactory factory = KeyFactory.getInstance("RSA");
			RSAkey = (RSAPublicKey) factory.generatePublic(publicKeySpec);
			
			Security.addProvider(new BouncyCastleProvider());
			cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, RSAkey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (BerDecodeException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}


	public byte[] encrypt(byte[] input) {
		try {
			return cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}