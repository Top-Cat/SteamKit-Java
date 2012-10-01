package uk.co.thomasc.steamkit.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

import com.google.protobuf.CodedOutputStream;

import uk.co.thomasc.steamkit.util.crypto.CryptoHelper;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;
import uk.co.thomasc.steamkit.util.stream.BinaryWriter;

@SuppressWarnings("unused")
public class ZipUtil {
	private static int LocalFileHeader = 0x04034b50;
	private static int CentralDirectoryHeader = 0x02014b50;
	private static int EndOfDirectoryHeader = 0x06054b50;

	private static short DeflateCompression = 8;

	private static short Version = 20;

	public static byte[] deCompress(byte[] buffer) throws IOException {
		final BinaryReader is = new BinaryReader(buffer);
		if (!ZipUtil.peekHeader(is, ZipUtil.LocalFileHeader)) {
			throw new IOException("Expecting LocalFileHeader at start of stream");
		}

		final Passable<String> fileName = new Passable<String>("");
		final Passable<Integer> decompressedSize = new Passable<Integer>(0);
		final byte[] compressedBuffer = ZipUtil.readLocalFile(is, fileName, decompressedSize);

		if (!ZipUtil.peekHeader(is, ZipUtil.CentralDirectoryHeader)) {
			throw new IOException("Expecting CentralDirectoryHeader following filename");
		}

		final Passable<String> cdrFileName = new Passable<String>("");
		final int relativeOffset = ZipUtil.readCentralDirectory(is, cdrFileName);

		if (!ZipUtil.peekHeader(is, ZipUtil.EndOfDirectoryHeader)) {
			throw new IOException("Expecting EndOfDirectoryHeader following CentralDirectoryHeader");
		}

		final int count = ZipUtil.readEndOfDirectory(is);

		return ZipUtil.inflateBuffer(compressedBuffer, decompressedSize);
	}

	public static byte[] compress(byte[] buffer) throws IOException {
		final BinaryWriter os = new BinaryWriter();

		int checkSum = 0;

		final ByteBuffer bb = ByteBuffer.wrap(CryptoHelper.CRCHash(buffer));
		checkSum = bb.getInt();

		final byte[] compressed = ZipUtil.deflateBuffer(buffer);

		final int poslocal = ZipUtil.writeHeader(os, ZipUtil.LocalFileHeader);
		ZipUtil.writeLocalFile(os, "z", checkSum, buffer.length, compressed);

		final int posCDR = ZipUtil.writeHeader(os, ZipUtil.CentralDirectoryHeader);
		final int CDRSize = ZipUtil.writeCentralDirectory(os, "z", checkSum, compressed.length, buffer.length, poslocal);

		final int posEOD = ZipUtil.writeHeader(os, ZipUtil.EndOfDirectoryHeader);
		ZipUtil.writeEndOfDirectory(os, 1, CDRSize, posCDR);

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

			writer.write(ZipUtil.Version); // versionGenerator
			writer.write(ZipUtil.Version); // versionExtract
			writer.write((short) 0); // bitflags
			writer.write(ZipUtil.DeflateCompression); // compression

			writer.write((short) 0); // modTime
			writer.write((short) 0); // createTime
			writer.write(CRC); // CRC

			writer.write(compressedSize); // compressedSize
			writer.write(decompressedSize); // decompressedSize

			final int nameByteCount = fileName.getBytes().length;
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

		return position2 - position + 4;
	}

	private static void writeLocalFile(BinaryWriter writer, String fileName, int CRC, int decompressedSize, byte[] processedBuffer) throws IOException {
		writer.write(ZipUtil.Version); // version
		writer.write((short) 0); // bitflags
		writer.write(ZipUtil.DeflateCompression); // compression

		writer.write((short) 0); // modTime
		writer.write((short) 0); // createTime
		writer.write(CRC); // CRC

		writer.write(processedBuffer.length); // compressedSize
		writer.write(decompressedSize); // decompressedSize

		final short nameByteCount = (short) fileName.getBytes().length;
		writer.write(nameByteCount); // nameLength
		writer.write((short) 0); // fieldLength

		writer.write(fileName.getBytes()); // filename
		writer.write(processedBuffer); // contents
	}

	private static boolean peekHeader(BinaryReader reader, int expecting) throws IOException {
		final int header = reader.readInt();

		return header == expecting;
	}

	private static int readEndOfDirectory(BinaryReader reader) throws IOException {
		final short diskNumber = reader.readShort();
		final short CDRDisk = reader.readShort();
		final short CDRCount = reader.readShort();
		final short CDRTotal = reader.readShort();

		final int CDRSize = reader.readInt();
		final int CDROffset = reader.readInt();

		final short commentLength = reader.readShort();
		final byte[] comment = reader.readBytes(commentLength);

		return CDRCount;
	}

	private static int readCentralDirectory(BinaryReader reader, Passable<String> fileName) throws IOException {
		final short versionGenerator = reader.readShort();
		final short versionExtract = reader.readShort();
		final short bitflags = reader.readShort();
		final short compression = reader.readShort();

		if (compression != ZipUtil.DeflateCompression) {
			throw new IOException("Invalid compression method " + compression);
		}

		final short modtime = reader.readShort();
		final short createtime = reader.readShort();
		final int crc = reader.readInt();

		final int compressedSize = reader.readInt();
		final int decompressedSize = reader.readInt();

		final short nameLength = reader.readShort();
		final short fieldLength = reader.readShort();
		final short commentLength = reader.readShort();

		final short diskNumber = reader.readShort();
		final short internalAttributes = reader.readShort();
		final int externalAttributes = reader.readInt();

		final int relativeOffset = reader.readInt();

		final byte[] name = reader.readBytes(nameLength);
		final byte[] fields = reader.readBytes(fieldLength);
		final byte[] comment = reader.readBytes(commentLength);

		fileName.setValue(new String(name));
		return relativeOffset;
	}

	private static byte[] readLocalFile(BinaryReader reader, Passable<String> fileName, Passable<Integer> decompressedSize) throws IOException {
		final short version = reader.readShort();
		final short bitflags = reader.readShort();
		final short compression = reader.readShort();

		if (compression != ZipUtil.DeflateCompression) {
			throw new IOException("Invalid compression method " + compression);
		}

		final short modtime = reader.readShort();
		final short createtime = reader.readShort();
		final int crc = reader.readInt();

		final int compressedSize = reader.readInt();
		decompressedSize.setValue(reader.readInt());

		final short nameLength = reader.readShort();
		final short fieldLength = reader.readShort();

		final byte[] name = reader.readBytes(nameLength);
		final byte[] fields = reader.readBytes(fieldLength);

		fileName.setValue(new String(name));

		return reader.readBytes(compressedSize);
	}

	private static byte[] inflateBuffer(byte[] compressedBuffer, Passable<Integer> decompressedSize) throws IOException {
		final Inflater decompressor = new Inflater(true);
		decompressor.setInput(compressedBuffer);

		final ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedBuffer.length);

		final byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				final int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (final DataFormatException e) {
			}
		}

		return bos.toByteArray();
	}

	private static byte[] deflateBuffer(byte[] uncompressedBuffer) throws IOException {
		final DeflaterOutputStream deflateStream = new DeflaterOutputStream(new ByteArrayOutputStream());

		deflateStream.write(uncompressedBuffer, 0, uncompressedBuffer.length);
		deflateStream.close();

		return uncompressedBuffer;
	}

}
