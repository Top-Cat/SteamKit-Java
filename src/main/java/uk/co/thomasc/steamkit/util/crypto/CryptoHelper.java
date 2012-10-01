package uk.co.thomasc.steamkit.util.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import uk.co.thomasc.steamkit.util.classlesshasher.JenkinsHash;
import uk.co.thomasc.steamkit.util.logging.Debug;

/**
 * Provides Crypto functions used in Steam protocols
 */
public class CryptoHelper {

	private CryptoHelper() {
		throw new AssertionError();
	}

	/**
	 * Performs an SHA1 hash of an input byte array
	 */
	public static byte[] SHAHash(byte[] input) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			return md.digest(input);
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
		/// <summary>
		/// Encrypts using AES/CBC/PKCS7 an input byte array with a given key and IV
		/// </summary>
		public static byte[] AESEncrypt( byte[] input, byte[] key, byte[] iv )
		{
			using ( var aes = new RijndaelManaged() )
			{
				aes.BlockSize = 128;
				aes.KeySize = 128;

				aes.Mode = CipherMode.CBC;
				aes.Padding = PaddingMode.PKCS7;

				using ( var aesTransform = aes.CreateEncryptor( key, iv ) )
				using ( var ms = new MemoryStream() )
				using ( var cs = new CryptoStream( ms, aesTransform, CryptoStreamMode.Write ) )
				{
					cs.Write( input, 0, input.Length );
					cs.FlushFinalBlock();
					
					return ms.ToArray();
				}
			}
		}

		/// <summary>
		/// Decrypts an input byte array using AES/CBC/PKCS7 with a given key and IV
		/// </summary>
		public static byte[] AESDecrypt( byte[] input, byte[] key, byte[] iv )
		{
			using ( var aes = new RijndaelManaged() )
			{
				aes.BlockSize = 128;
				aes.KeySize = 128;

				aes.Mode = CipherMode.CBC;
				aes.Padding = PaddingMode.PKCS7;

				byte[] plainText = new byte[ input.Length ];
				int outLen = 0;

				using ( var aesTransform = aes.CreateDecryptor( key, iv ) )
				using ( var ms = new MemoryStream( input ) )
				using ( var cs = new CryptoStream( ms, aesTransform, CryptoStreamMode.Read ) )
				{
					outLen = cs.Read( plainText, 0, plainText.Length );
				}

				byte[] output = new byte[ outLen ];
				Array.Copy( plainText, 0, output, 0, output.Length );

				return output;
			}
		}*/

	/**
	 * Performs an encryption using AES/CBC/PKCS7 with an input byte array and key, with a random IV prepended using AES/ECB/None
	 */
	public static byte[] SymmetricEncrypt(byte[] input, byte[] key) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			Debug.Assert(key.length == 32);

			// encrypt iv using ECB and provided key
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));

			// generate iv
			final byte[] iv = CryptoHelper.GenerateRandomBlock(16);
			final byte[] cryptedIv = cipher.doFinal(iv);

			// encrypt input plaintext with CBC using the generated (plaintext) IV and the provided key
			cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

			final byte[] cipherText = cipher.doFinal(input);

			// final output is 16 byte ecb crypted IV + cbc crypted plaintext
			final byte[] output = new byte[cryptedIv.length + cipherText.length];
			System.arraycopy(cryptedIv, 0, output, 0, cryptedIv.length);
			System.arraycopy(cipherText, 0, output, cryptedIv.length, cipherText.length);

			return output;
		} catch (final InvalidKeyException e) {
			e.printStackTrace();
		} catch (final InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (final NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (final IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (final BadPaddingException e) {
			e.printStackTrace();
		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * Decrypts using AES/CBC/PKCS7 with an input byte array and key, using the random IV prepended using AES/ECB/None
	 */
	public static byte[] SymmetricDecrypt(byte[] input, byte[] key) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			Debug.Assert(key.length == 32);

			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");

			// first 16 bytes of input is the ECB encrypted IV
			byte[] iv = new byte[16];
			final byte[] cryptedIv = Arrays.copyOfRange(input, 0, 16);

			// the rest is ciphertext
			byte[] cipherText = new byte[input.length - cryptedIv.length];
			cipherText = Arrays.copyOfRange(input, cryptedIv.length, cryptedIv.length + cipherText.length);

			// decrypt the IV using ECB
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
			iv = cipher.doFinal(cryptedIv);

			cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

			// decrypt the remaining ciphertext in cbc with the decrypted IV
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
			return cipher.doFinal(cipherText);
		} catch (final InvalidKeyException e) {
			e.printStackTrace();
		} catch (final InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (final NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (final IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (final BadPaddingException e) {
			e.printStackTrace();
		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * Performs the Jenkins hash on an input byte array
	 */
	public static byte[] JenkinsHash(byte[] input) {
		final JenkinsHash jHash = new JenkinsHash();
		final long hash = jHash.hash(input);

		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt((int) hash);

		final byte[] array = buffer.array();
		final byte[] output = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			output[array.length - 1 - i] = array[i];
		}

		return output;
	}

	/**
	 * Performs CRC32 on an input byte array using the CrcStandard.Crc32Bit parameters
	 */
	public static byte[] CRCHash(byte[] input) {
		final CRC32 crc = new CRC32();
		crc.update(input);
		final long hash = crc.getValue();

		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt((int) hash);

		final byte[] array = buffer.array();
		final byte[] output = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			output[array.length - 1 - i] = array[i];
		}

		return output;
	}

	/**
	 * Performs an Adler32 on the given input
	 */
	public static byte[] AdlerHash(byte[] input) {
		int a = 0, b = 0;
		for (final byte element : input) {
			a = (a + element) % 65521;
			b = (b + a) % 65521;
		}
		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(a | b << 16);
		return buffer.array();
	}

	/**
	 * Generate an array of random bytes given the input length
	 */
	public static byte[] GenerateRandomBlock(int size) {
		final byte[] block = new byte[size];
		final SecureRandom random = new SecureRandom();
		random.nextBytes(block);
		return block;
	}

}
