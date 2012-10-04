package uk.co.thomasc.steamkit.types.steamid;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.thomasc.steamkit.base.generated.steamlanguage.EAccountType;
import uk.co.thomasc.steamkit.base.generated.steamlanguage.EUniverse;

public class SteamID {
	BitVector64 steamid;

	static Pattern SteamIDRegex = Pattern.compile("STEAM_(?<universe>[0-5]):(?<authserver>[0-1]):(?<accountid>\\d+)", Pattern.CASE_INSENSITIVE);

	/**
	 * The account instance value when representing all instanced {@link SteamID}
	 */
	public static final int AllInstances = 0;
	/**
	 * The account instance value for a desktop {@link SteamID}.
	 */
	public static final int DesktopInstance = 1;
	/**
	 * The account instance value for a console {@link SteamID}.
	 */
	public static final int ConsoleInstance = 2;
	/**
	 * The account instance for a mobile or web based {@link SteamID}.
	 */
	public static final int WebInstance = 4;

	/**
	 * Masking value used for the account id.
	 */
	public static final int AccountIDMask = 0xFFFFFFFF;
	/**
	 * Masking value used for packing chat instance flags into a {@link SteamID}.
	 */
	public static final int AccountInstanceMask = 0x000FFFFF;

	/**
	 * Represents various flags a chat {@link SteamID} may have, packed into its instance.
	 */
	public enum ChatInstanceFlags {
		/**
		 * This flag is set for clan based chat {@link SteamID}s.
		 */
		Clan(SteamID.AccountInstanceMask + 1 >> 1),
		/**
		 * This flag is set for lobby based chat {@link SteamID}s.
		 */
		Lobby(SteamID.AccountInstanceMask + 1 >> 2),
		/**
		 * This flag is set for matchmaking lobby based chat {@link SteamID}s.
		 */
		MMSLobby(SteamID.AccountInstanceMask + 1 >> 3), ;

		private int flag;

		private ChatInstanceFlags(int flag) {
			this.flag = flag;
		}

		public int v() {
			return flag;
		}

		private static HashMap<Integer, ChatInstanceFlags> values = new HashMap<Integer, ChatInstanceFlags>();
		static {
			for (final ChatInstanceFlags type : ChatInstanceFlags.values()) {
				ChatInstanceFlags.values.put(type.v(), type);
			}
		}

		public static ChatInstanceFlags f(int code) {
			return ChatInstanceFlags.values.get(code);
		}
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class.
	 */
	public SteamID() {
		this(0);
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class.
	 * @param unAccountID	The account ID.
	 * @param eUniverse		The universe.
	 * @param eAccountType	The account type.
	 */
	public SteamID(int unAccountID, EUniverse eUniverse, EAccountType eAccountType) {
		this();
		set(unAccountID, eUniverse, eAccountType);
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class.
	 * @param unAccountID	The account ID.
	 * @param unInstance	The instance.
	 * @param eUniverse		The universe.
	 * @param eAccountType	The account type.
	 */
	public SteamID(int unAccountID, int unInstance, EUniverse eUniverse, EAccountType eAccountType) {
		this();
		instancedSet(unAccountID, unInstance, eUniverse, eAccountType);
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class.
	 * @param id	The 64bit integer to assign this SteamID from.
	 */
	public SteamID(long id) {
		steamid = new BitVector64(id);
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class from a rendered form.
	 * This constructor assumes the rendered SteamID is in the public universe.
	 * @param steamId	A "STEAM_" rendered form of the SteamID.
	 */
	public SteamID(String steamId) {
		this(steamId, EUniverse.Public);
	}

	/**
	 * Initializes a new instance of the {@link SteamID} class from a rendered form and universe.
	 * @param steamId	A "STEAM_" rendered form of the SteamID.
	 * @param eUniverse	The universe the SteamID belongs to.
	 */
	public SteamID(String steamId, EUniverse eUniverse) {
		setFromString(steamId, eUniverse);
	}

	/**
	 * Sets the various components of this SteamID instance.
	 * @param unAccountID	The account ID.
	 * @param eUniverse		The universe.
	 * @param eAccountType	The account type.
	 */
	public void set(int unAccountID, EUniverse eUniverse, EAccountType eAccountType) {
		setAccountID(unAccountID);
		setAccountUniverse(eUniverse);
		setAccountType(eAccountType);

		if (eAccountType == EAccountType.Clan) {
			setAccountInstance(0);
		} else {
			setAccountInstance(SteamID.DesktopInstance);
		}
	}

	/**
	 * Sets the various components of this SteamID instance.
	 * @param unAccountID	The account ID.
	 * @param unInstance	The instance.
	 * @param eUniverse		The universe.
	 * @param eAccountType	The account type.
	 */
	public void instancedSet(int unAccountID, int unInstance, EUniverse eUniverse, EAccountType eAccountType) {
		setAccountID(unAccountID);
		setAccountUniverse(eUniverse);
		setAccountType(eAccountType);
		setAccountInstance(unInstance);
	}

	/**
	 * Sets the various components of this SteamID from a rendered form and universe.
	 * @param steamId	A "STEAM_" rendered form of the SteamID.
	 * @param eUniverse	The universe the SteamID belongs to.
	 * @return	True if this instance was successfully assigned, or false if the given string was in an invalid format.
	 */
	public boolean setFromString(String steamId, EUniverse eUniverse) {
		if (steamId == null || steamId.isEmpty()) {
			return false;
		}

		final Matcher m = SteamID.SteamIDRegex.matcher(steamId);

		if (!m.matches()) {
			return false;
		}

		final int accId = Integer.parseInt(m.group("accountid"));
		final int authServer = Integer.parseInt(m.group("authserver"));

		setAccountUniverse(eUniverse);
		setAccountInstance(1);
		setAccountType(EAccountType.Individual);
		setAccountID(accId << 1 | authServer);

		return true;
	}

	/**
	 * Sets the various components of this SteamID from a 64bit integer form.
	 * @param ulSteamID	The 64bit integer to assign this SteamID from.
	 */
	public void setFromUInt64(long ulSteamID) {
		steamid.setData(ulSteamID);
	}

	/**
	 * Converts this SteamID into it's 64bit integer form.
	 * @return A 64bit integer representing this SteamID.
	 */
	public long convertToLong() {
		return steamid.getData();
	}

	/**
	 * Returns a static account key used for grouping accounts with differing instances.
	 * @return A 64bit static account key.
	 */
	public long getStaticAccountKey() {
		return ((long) getAccountUniverse().v() << 56) + ((long) getAccountType().v() << 52) + getAccountID();
	}

	/**
	 * Gets a value indicating whether this instance is a blank anonymous account
	 * @return true if this instance is a blank anon account; otherwise, false.
	 */
	public boolean isBlankAnonAccount() {
		return getAccountID() == 0 && isAnonAccount() && getAccountInstance() == 0;
	}

	/**
	 * Gets a value indicating whether this instance is a game server account.
	 * @return true if this instance is a game server account; otherwise, false
	 */
	public boolean isGameServerAccount() {
		return getAccountType() == EAccountType.GameServer || getAccountType() == EAccountType.AnonGameServer;
	}

	/**
	 * Gets a value indicating whether this instance is a persistent game server account.
	 * @return true if this instance is a persistent game server account; otherwise, false
	 */
	public boolean isPersistentGameServerAccount() {
		return getAccountType() == EAccountType.GameServer;
	}

	/**
	 * Gets a value indicating whether this instance is an anonymous game server account.
	 * @return true if this instance is an anon game server account; otherwise, false.
	 */
	public boolean isAnonGameServerAccount() {
		return getAccountType() == EAccountType.AnonGameServer;
	}

	/**
	 * Gets a value indicating whether this instance is a content server account.
	 * @return true if this instance is a content server account; otherwise, false.
	 */
	public boolean isContentServerAccount() {
		return getAccountType() == EAccountType.ContentServer;
	}

	/**
	 * Gets a value indicating whether this instance is a clan account.
	 * @return true if this instance is a clan account; otherwise, false.
	 */
	public boolean isClanAccount() {
		return getAccountType() == EAccountType.Clan;
	}

	/**
	 * Gets a value indicating whether this instance is a chat account.
	 * @return true if this instance is a chat account; otherwise, false.
	 */
	public boolean isChatAccount() {
		return getAccountType() == EAccountType.Chat;
	}

	/**
	 * Gets a value indicating whether this instance is a lobby.
	 * @return true if this instance is a lobby; otherwise, false.
	 */
	public boolean IsLobby() {
		return getAccountType() == EAccountType.Chat && (getAccountInstance() & ChatInstanceFlags.Lobby.v()) > 0;
	}

	/**
	 * Gets a value indicating whether this instance is an individual account.
	 * @return true if this instance is an individual account; otherwise, false.
	 */
	public boolean isIndividualAccount() {
		return getAccountType() == EAccountType.Individual || getAccountType() == EAccountType.ConsoleUser;
	}

	/**
	 * Gets a value indicating whether this instance is an anonymous account.
	 * @return true if this instance is an anon account; otherwise, false.
	 */
	public boolean isAnonAccount() {
		return getAccountType() == EAccountType.AnonUser || getAccountType() == EAccountType.AnonGameServer;
	}

	/**
	 * Gets a value indicating whether this instance is an anonymous user account.
	 * @return true if this instance is an anon user account; otherwise, false.
	 */
	public boolean isAnonUserAccount() {
		return getAccountType() == EAccountType.AnonUser;
	}

	/**
	 * Gets a value indicating whether this instance is a console user account.
	 * @return true if this instance is a console user account; otherwise, false.
	 */
	public boolean isConsoleUserAccount() {
		return getAccountType() == EAccountType.ConsoleUser;
	}

	/**
	 * Gets a value indicating whether this instance is valid.
	 * @return true if this instance is valid; otherwise, false.
	 */
	public boolean IsValid() {
		if (getAccountType().v() <= EAccountType.Invalid.v() || getAccountType().v() >= EAccountType.Max.v()) {
			return false;
		}

		if (getAccountUniverse().v() <= EUniverse.Invalid.v() || getAccountUniverse().v() >= EUniverse.Max.v()) {
			return false;
		}

		if (getAccountType() == EAccountType.Individual) {
			if (getAccountID() == 0 || getAccountInstance() > SteamID.WebInstance) {
				return false;
			}
		}

		if (getAccountType() == EAccountType.Clan) {
			if (getAccountID() == 0 || getAccountInstance() != 0) {
				return false;
			}
		}

		if (getAccountType() == EAccountType.GameServer) {
			if (getAccountID() == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the account id.
	 * @return The account id.
	 */
	public long getAccountID() {
		return steamid.getMask((short) 0, 0xFFFFFFFF);
	}

	/**
	 * Sets the account id.
	 * @param value	The account id.
	 */
	public void setAccountID(long value) {
		steamid.setMask((short) 0, 0xFFFFFFFF, value);
	}

	/**
	 * Gets the account instance.
	 * @return The account instance.
	 */
	public long getAccountInstance() {
		return steamid.getMask((short) 32, 0xFFFFF);
	}

	/**
	 * Sets the account instance.
	 * @param value	The account instance.
	 */
	public void setAccountInstance(long value) {
		steamid.setMask((short) 32, 0xFFFFF, value);
	}

	public void setAccountInstance(ChatInstanceFlags clan) {
		setAccountInstance(clan.v());
	}

	/**
	 * Gets the account type.
	 * @return The account type.
	 */
	public EAccountType getAccountType() {
		return EAccountType.fromCode((int) steamid.getMask((short) 52, 0xF));
	}

	/**
	 * Sets the account type.
	 * @param value	The account type.
	 */
	public void setAccountType(EAccountType value) {
		steamid.setMask((short) 52, 0xF, value.v());
	}

	/**
	 * Gets the account universe.
	 * @return The account universe.
	 */
	public EUniverse getAccountUniverse() {
		return EUniverse.f((int) steamid.getMask((short) 56, 0xFF));
	}

	/**
	 * Sets the account universe.
	 * @param value	The account universe.
	 */
	public void setAccountUniverse(EUniverse value) {
		steamid.setMask((short) 56, 0xFF, value.v());
	}

	/**
	 * Renders this instance into it's "STEAM_" represenation.
	 * @return A string "STEAM_" representation of this SteamID.
	 */
	public String render() {
		switch (getAccountType()) {
			case Invalid:
			case Individual:
				if (getAccountUniverse().v() <= EUniverse.Public.v()) {
					return String.format("STEAM_0:%d:%d", getAccountID() & 1, (int) getAccountID() >> 1);
				} else {
					return String.format("STEAM_%d:%d:%d", getAccountID() & 1, (int) getAccountID() >> 1, getAccountUniverse().v());
				}
			default:
				return super.toString();
		}
	}

	/**
	 * Returns a {@link String} that represents this instance.
	 */
	@Override
	public String toString() {
		return render();
	}

	/**
	 * Determines whether the specified {@link Object} is equal to this instance
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof SteamID)) {
			return false;
		}

		final SteamID sid = (SteamID) obj;

		return steamid.getData().equals(sid.steamid.getData());
	}

	@Override
	public SteamID clone() {
		return new SteamID(steamid.getData());
	}

	/**
	 * Returns a hash code for this instance.
	 */
	@Override
	public int hashCode() {
		return steamid.getData().hashCode();
	}

}
