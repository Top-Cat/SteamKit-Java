package uk.co.thomasc.steamkit.base.gc.tf2;

import java.util.HashMap;

public enum ECraftingRecipe {
	Unknown(0),
	SmeltClassWeapons(3),
	CombineScrap(4),
	CombineReclaimed(5),
	SmeltReclaimed(22),
	SmeltRefined(23),
	;

	private short recipe;
	
	private ECraftingRecipe(int recipe) {
		this.recipe = (short) recipe;
	}
	
	public short v() {
		return recipe;
	}
	
	private static HashMap<Short, ECraftingRecipe> values = new HashMap<Short, ECraftingRecipe>();

	static {
		for (final ECraftingRecipe type : ECraftingRecipe.values()) {
			ECraftingRecipe.values.put(type.v(), type);
		}
	}

	public static ECraftingRecipe f(short v) {
		ECraftingRecipe r = ECraftingRecipe.values.get(v);
		if (r != null) {
			return r;
		}
		return Unknown;
	}
}