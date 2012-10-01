package uk.co.thomasc.steamkit.util;

import java.util.Iterator;
import java.util.Map.Entry;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors.FieldDescriptor;

public class Serializer {
	
	public static <T extends AbstractMessage> void serialize(T msg, CodedOutputStream cs) {
		Iterator<Entry<FieldDescriptor, Object>> iterator = msg.getAllFields().entrySet().iterator();
		while (iterator.hasNext()) {
			
		}
	}
	
}