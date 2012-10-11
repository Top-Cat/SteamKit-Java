package org.spout.steamkit.types;

import org.junit.Assert;
import org.junit.Test;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountType;
import uk.co.thomasc.steamkit.types.steamid.SteamID;

public class TypesTest {

	@Test
	public void testSteamID() {
		final SteamID test = new SteamID(76561198072111979L);
		Assert.assertEquals("STEAM_0:1:55923125", test.toString());
	}
	
	@Test
	public void testSteamID2() {
		final SteamID chatId = new SteamID(103582791433585561L);
		
		chatId.setAccountInstance(SteamID.ChatInstanceFlags.Clan);
		Assert.assertEquals(105834591247270809L, chatId.convertToLong());
		
		chatId.setAccountType(EAccountType.Chat);
		Assert.assertEquals(110338190874641305L, chatId.convertToLong());
	}

}
