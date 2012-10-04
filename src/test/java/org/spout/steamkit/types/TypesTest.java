package org.spout.steamkit.types;

import org.junit.Assert;
import org.junit.Test;

import uk.co.thomasc.steamkit.types.steamid.SteamID;

public class TypesTest {

	@Test
	public void testSteamID() {
		final SteamID test = new SteamID(76561198072111979L);
		Assert.assertEquals("STEAM_0:1:55923125", test.toString());
	}

}
