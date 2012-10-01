package uk.co.thomasc.steamkit.steam3.handlers.steamapps.types;

import java.io.IOException;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientPackageInfoResponse;

import uk.co.thomasc.steamkit.types.keyvalue.KeyValue;
import uk.co.thomasc.steamkit.util.stream.BinaryReader;

import lombok.Getter;

/**
 * Represents a single package in this response.
 */
public final class Package {
	/**
	 * Gets the status of this package.
	 */
	@Getter private PackageStatus status;

	/**
	 * Gets the PackageID for this package.
	 */
	@Getter private int packageID;

	/**
	 * Gets the last change number for this package.
	 */
	@Getter private int changeNumber;

	/**
	 * Gets a hash of the package data for caching purposes.
	 */
	@Getter private byte[] hash;

	/**
	 * Gets the data for this package.
	 */
	@Getter private KeyValue data;

	public Package(CMsgClientPackageInfoResponse.Package pack, PackageStatus status) {
		this.status = status;

		packageID = pack.getPackageId();
		changeNumber = pack.getChangeNumber();
		hash = pack.getSha().toByteArray();

		data = new KeyValue();

		BinaryReader is = new BinaryReader(pack.getBuffer().toByteArray());
		try {
			is.readInt(); // unknown uint at the beginning of the buffer
			data.readAsBinary(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Package(int packageId, PackageStatus status) {
		this.status = status;
		packageID = packageId;
	}
}