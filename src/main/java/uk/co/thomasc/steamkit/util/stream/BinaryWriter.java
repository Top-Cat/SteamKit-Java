package uk.co.thomasc.steamkit.util.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.google.protobuf.CodedOutputStream;

public class BinaryWriter {

	CodedOutputStream writer;
	OutputStream os;
	ByteArrayOutputStream stream = null;

	public BinaryWriter(ByteArrayOutputStream stream) {
		this((OutputStream) stream);
		this.stream = stream;
	}

	public BinaryWriter(int size) {
		this(new ByteArrayOutputStream(size));
	}

	public BinaryWriter() {
		this(32);
	}

	public BinaryWriter(OutputStream outputStream) {
		os = outputStream;
		writer = CodedOutputStream.newInstance(outputStream);
	}

	public void write(short data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(data);
		writeR(buffer.array());
	}
	
	public void write(int data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(data);
		writeR(buffer.array());
	}

	public void write(long data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(data);
		writeR(buffer.array());
	}

	public byte[] toByteArray() {
		if (stream != null) {
			return stream.toByteArray();
		}
		return null;
	}

	public void writeR(byte[] data) throws IOException {
		for (int i=data.length-1;i>=0;--i) {
			write(data[i]);
		}
	}
	
	public void write(byte[] data) throws IOException {
		writer.writeRawBytes(data);
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(byte data) throws IOException {
		writer.writeRawByte(data);
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CodedOutputStream getStream() {
		return writer;
	}

	public void flush() {
		try {
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}