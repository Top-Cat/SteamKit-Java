package uk.co.thomasc.steamkit.base.gc.tf2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.thomasc.steamkit.base.gc.EGCMsgBase;
import uk.co.thomasc.steamkit.base.generated.steamlanguageinternal.IGCSerializableMessage;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

public class GCMsgCraftItemResponse implements IGCSerializableMessage {

	@Override
	public int getEMsg() {
		return EGCMsgBase.CraftResponse;
	}

	// Static size: 2
	public ECraftingRecipe recipe = ECraftingRecipe.Unknown;

	public int unknown = 0; // Usually 0x0000000
	
	public List<Long> items = new ArrayList<Long>();

	public GCMsgCraftItemResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(recipe.v());
		stream.write(unknown);
		stream.write((short) items.size());
		for (final Long item : items) {
			stream.write(item);
		}
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		recipe = ECraftingRecipe.f(stream.readShort());
		unknown = stream.readInt();
		final int itemCount = stream.readShort();
		for (int i = 0; i < itemCount; i++) {
			items.add(stream.readLong());
		}
	}
}
