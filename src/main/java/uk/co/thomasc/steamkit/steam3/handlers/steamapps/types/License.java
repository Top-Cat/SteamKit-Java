package uk.co.thomasc.steamkit.steam3.handlers.steamapps.types;

import java.util.Date;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientLicenseList;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.ELicenseFlags;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.ELicenseType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EPaymentMethod;

/**
 * Represents a granted license (steam3 subscription) for one or more games.
 */
public final class License {
	/**
	 * Gets the package ID used to identify the license.
	 */
	@Getter private int packageID;

	/**
	 * Gets the last change number for this license.
	 */
	@Getter private int lastChangeNumber;

	/**
	 * Gets the time the license was created.
	 */
	@Getter private Date timeCreated;

	/**
	 * Gets the next process time for the license.
	 */
	@Getter private Date timeNextProcess;

	/**
	 * Gets the minute limit of the license.
	 */
	@Getter private int minuteLimit;

	/**
	 * Gets the minutes used of the license.
	 */
	@Getter private int minutesUsed;

	/**
	 * Gets the payment method used when the license was created.
	 */
	@Getter private EPaymentMethod paymentMethod;

	/**
	 * Gets the license flags.
	 */
	@Getter private ELicenseFlags licenseFlags;

	/**
	 * Gets the two letter country code where the license was purchased.
	 */
	@Getter private String purchaseCountryCode;

	/**
	 * Gets the type of the license.
	 */
	@Getter private ELicenseType licenseType;

	/**
	 * Gets the territory code of the license.
	 */
	@Getter private int territoryCode;


	public License(CMsgClientLicenseList.License license) {
		this.packageID = license.getPackageId();

		this.lastChangeNumber = license.getChangeNumber();

		this.timeCreated = new Date(license.getTimeCreated());
		this.timeNextProcess = new Date(license.getTimeNextProcess());

		this.minuteLimit = license.getMinuteLimit();
		this.minutesUsed = license.getMinutesUsed();

		this.paymentMethod = EPaymentMethod.f(license.getPaymentMethod());
		this.licenseFlags = ELicenseFlags.f(license.getFlags());

		this.purchaseCountryCode = license.getPurchaseCountryCode();

		this.licenseType = ELicenseType.f(license.getLicenseType());

		this.territoryCode = license.getTerritoryCode();
	}
}