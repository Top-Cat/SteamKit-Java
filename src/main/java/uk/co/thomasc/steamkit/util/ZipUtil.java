package uk.co.thomasc.steamkit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import uk.co.thomasc.steamkit.util.crypto.CryptoHelper;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

@SuppressWarnings("unused")
public class ZipUtil {
	private static int LocalFileHeader = 0x04034b50;
	private static int CentralDirectoryHeader = 0x02014b50;
	private static int EndOfDirectoryHeader = 0x06054b50;

	private static short DeflateCompression = 8;

	private static short Version = 20;

	public static byte[] deCompress(byte[] buffer) throws IOException {
		BinaryReader is = new BinaryReader(buffer);
		if (!peekHeader(is, LocalFileHeader)) {
			throw new IOException("Expecting LocalFileHeader at start of stream");
		}

		Passable<String> fileName = new Passable<String>("");
		Passable<Integer> decompressedSize = new Passable<Integer>(0);
		byte[] compressedBuffer = readLocalFile(is, fileName, decompressedSize);

		if (!peekHeader(is, CentralDirectoryHeader)) {
			throw new IOException( "Expecting CentralDirectoryHeader following filename" );
		}

		Passable<String> cdrFileName = new Passable<String>("");
		int relativeOffset = readCentralDirectory(is, cdrFileName);

		if (!peekHeader(is, EndOfDirectoryHeader)) {
			throw new IOException("Expecting EndOfDirectoryHeader following CentralDirectoryHeader");
		}

		int count = readEndOfDirectory(is);

		return inflateBuffer(compressedBuffer, decompressedSize);
	}

	public static byte[] compress(byte[] buffer) throws IOException {
		BinaryWriter os = new BinaryWriter();
	
		int checkSum = 0;

		ByteBuffer bb = ByteBuffer.wrap(CryptoHelper.CRCHash(buffer));
		checkSum = bb.getInt();

		byte[] compressed = deflateBuffer(buffer);

		int poslocal = writeHeader(os, LocalFileHeader);
		writeLocalFile(os, "z", checkSum, buffer.length, compressed);

		int posCDR = writeHeader(os, CentralDirectoryHeader);
		int CDRSize = writeCentralDirectory(os, "z", checkSum, compressed.length, buffer.length, poslocal);

		int posEOD = writeHeader(os, EndOfDirectoryHeader);
		writeEndOfDirectory(os, 1, CDRSize, posCDR);

		return buffer;
	}


	private static int writeHeader(BinaryWriter writer, int header) throws IOException {
		Field f;
		int position = 0;
		try {
			f = CodedOutputStream.class.getField("position");
			
			f.setAccessible(true);
			try {
				position = f.getInt(writer);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			writer.write(header);
		} catch (NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}

		return position;
	}

	private static void writeEndOfDirectory(BinaryWriter writer, int count, int CDRSize, int CDROffset) throws IOException {
		writer.write((short) 0); // diskNumber
		writer.write((short) 0); // CDRDisk
		writer.write((short) count); // CDRCount
		writer.write((short) 1); // CDRTotal

		writer.write(CDRSize); // CDRSize
		writer.write(CDROffset); // CDROffset

		writer.write((short) 0); // commentLength
	}

	private static int writeCentralDirectory(BinaryWriter writer, String fileName, int CRC, int compressedSize, int decompressedSize, int localHeaderOffset) throws IOException {
		Field f;
		int position = 0;
		int position2 = 0;
		try {
			f = CodedOutputStream.class.getField("position");
			
			f.setAccessible(true);
			try {
				position = f.getInt(writer);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}

			writer.write(Version); // versionGenerator
			writer.write(Version); // versionExtract
			writer.write((short) 0); // bitflags
			writer.write(DeflateCompression); // compression
	
			writer.write((short) 0); // modTime
			writer.write((short) 0); // createTime
			writer.write(CRC); // CRC
	
			writer.write(compressedSize); // compressedSize
			writer.write(decompressedSize); // decompressedSize
	
			int nameByteCount = fileName.getBytes().length;
			writer.write(nameByteCount); // nameLength
			writer.write((short) 0); // fieldLength
			writer.write((short) 0); // commentLength
	
			writer.write((short) 0); // diskNumber
			writer.write((short) 1); // internalAttributes
			writer.write((short) 32); // externalAttributes
	
			writer.write(localHeaderOffset); // relativeOffset
	
			writer.write(fileName.getBytes()); // filename
	
			try {
				position2 = f.getInt(writer);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException | SecurityException e2) {
			e2.printStackTrace();
		}

		return (position2 - position) + 4;
	}

	private static void writeLocalFile(BinaryWriter writer, String fileName, int CRC, int decompressedSize, byte[] processedBuffer) throws IOException {
		writer.write(Version); // version
		writer.write((short) 0); // bitflags
		writer.write(DeflateCompression); // compression

		writer.write((short) 0); // modTime
		writer.write((short) 0); // createTime
		writer.write(CRC); // CRC

		writer.write(processedBuffer.length); // compressedSize
		writer.write(decompressedSize); // decompressedSize

		short nameByteCount = (short) fileName.getBytes().length;
		writer.write(nameByteCount); // nameLength
		writer.write((short) 0); // fieldLength

		writer.write(fileName.getBytes()); // filename
		writer.write(processedBuffer); // contents
	}


	private static boolean peekHeader(BinaryReader reader, int expecting) throws IOException {
		int header = reader.readInt();

		return header == expecting;
	}

	private static int readEndOfDirectory(BinaryReader reader) throws IOException {
		short diskNumber = reader.readShort();
		short CDRDisk = reader.readShort();
		short CDRCount = reader.readShort();
		short CDRTotal = reader.readShort();

		int CDRSize = reader.readInt();
		int CDROffset = reader.readInt();

		short commentLength = reader.readShort();
		byte[] comment = reader.readBytes(commentLength);

		return CDRCount;
	}

	private static int readCentralDirectory(BinaryReader reader, Passable<String> fileName) throws IOException {
		short versionGenerator = reader.readShort();
		short versionExtract = reader.readShort();
		short bitflags = reader.readShort();
		short compression = reader.readShort();

		if (compression != DeflateCompression) {
			throw new IOException("Invalid compression method " + compression);
		}

		short modtime = reader.readShort();
		short createtime = reader.readShort();
		int crc = reader.readInt();

		int compressedSize = reader.readInt();
		int decompressedSize = reader.readInt();

		short nameLength = reader.readShort();
		short fieldLength = reader.readShort();
		short commentLength = reader.readShort();

		short diskNumber = reader.readShort();
		short internalAttributes = reader.readShort();
		int externalAttributes = reader.readInt();

		int relativeOffset = reader.readInt();

		byte[] name = reader.readBytes(nameLength);
		byte[] fields = reader.readBytes(fieldLength);
		byte[] comment = reader.readBytes(commentLength);

		fileName.setValue(new String(name));
		return relativeOffset;
	}

	private static byte[] readLocalFile(BinaryReader reader, Passable<String> fileName, Passable<Integer> decompressedSize) throws IOException {
		short version = reader.readShort();
		short bitflags = reader.readShort();
		short compression = reader.readShort();

		if (compression != DeflateCompression) {
			throw new IOException( "Invalid compression method " + compression );
		}

		short modtime = reader.readShort();
		short createtime = reader.readShort();
		int crc = reader.readInt();

		int compressedSize = reader.readInt();
		decompressedSize.setValue(reader.readInt());

		short nameLength = reader.readShort();
		short fieldLength = reader.readShort();

		byte[] name = reader.readBytes(nameLength);
		byte[] fields = reader.readBytes(fieldLength);

		fileName.setValue(new String(name));

		return reader.readBytes(compressedSize);
	}


	private static byte[] inflateBuffer(byte[] compressedBuffer, Passable<Integer> decompressedSize) throws IOException {
		Inflater decompressor = new Inflater(true);
		decompressor.setInput(compressedBuffer);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedBuffer.length);

		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (DataFormatException e) {
			}
		}

		return bos.toByteArray();
	}

	private static byte[] deflateBuffer(byte[] uncompressedBuffer) throws IOException {
		DeflaterOutputStream deflateStream = new DeflaterOutputStream(new ByteArrayOutputStream());

		deflateStream.write(uncompressedBuffer, 0, uncompressedBuffer.length);
		deflateStream.close();

		return uncompressedBuffer;
	}

}