package uk.co.thomasc.steamkit.util.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EOSType;
import uk.co.thomasc.steamkit.util.crypto.CryptoHelper;

public class Utils {

	public static EOSType getOSType() {
		final String os = System.getProperty("os.name");
		switch (os) {
			case "Windows 7":
				return EOSType.Win7;
			case "Windows 2003":
				return EOSType.Win2003;
			case "Windows XP":
				return EOSType.WinXP;
			case "Windows 2000":
				return EOSType.Win200;
			case "Windows NT":
				return EOSType.WinNT;
			case "Windows ME":
				return EOSType.WinME;
			case "Windows 98":
				return EOSType.Win98;
			case "Windows 95":
				return EOSType.Win95;
		}
		if (os.startsWith("Win")) {
			return EOSType.WinUnknown;
		} else if (os.startsWith("Mac")) {
			return EOSType.MacOSUnknown;
		} else if (os.indexOf("nix") >= 0) {
			return EOSType.LinuxUnknown;
		}
		return EOSType.Unknown;
	}

	public static byte[] generateMachineID() {
		// Java can't really do much here :/
		// TODO: Make this better?

		try {
			final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			do {
				final NetworkInterface n = interfaces.nextElement();
				if (n.getHardwareAddress() != null && n.getHardwareAddress().length > 0) {
					return CryptoHelper.SHAHash(n.getHardwareAddress());
				}
			} while (NetworkInterface.getNetworkInterfaces().hasMoreElements());
		} catch (final SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

}
