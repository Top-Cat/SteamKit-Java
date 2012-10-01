package uk.co.thomasc.steamkit.steam3.handlers.steamuser.callbacks;

import lombok.Getter;

import uk.co.thomasc.steamkit.base.generated.SteammessagesClientserver.CMsgClientWalletInfoUpdate;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.ECurrencyCode;
import uk.co.thomasc.steamkit.steam3.steamclient.callbackmgr.CallbackMsg;

/**
 * This callback is recieved when wallet info is recieved from the network.
 */
public final class WalletInfoCallback extends CallbackMsg {
	/**
	 * Gets a value indicating whether this instance has wallet data.
	 */
	@Getter private final boolean hasWallet;

	/**
	 * Gets the currency code for this wallet.
	 */
	@Getter private final ECurrencyCode currency;

	/**
	 * Gets the balance of the wallet, in cents.
	 */
	@Getter private final int balance;

	public WalletInfoCallback(CMsgClientWalletInfoUpdate wallet) {
		hasWallet = wallet.getHasWallet();

		currency = ECurrencyCode.f(wallet.getCurrency());
		balance = wallet.getBalance();
	}
}
