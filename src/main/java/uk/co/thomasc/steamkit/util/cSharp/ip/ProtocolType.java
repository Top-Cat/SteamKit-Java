package uk.co.thomasc.steamkit.util.cSharp.ip;

public enum ProtocolType {
	// Summary:
	//     Unknown protocol.
	Unknown(-1),
	//
	// Summary:
	//     IPv6 Hop by Hop Options header.
	IPv6HopByHopOptions(0),
	//
	// Summary:
	//     Unspecified protocol.
	Unspecified(0),
	//
	// Summary:
	//     Internet Protocol.
	IP(0),
	//
	// Summary:
	//     Internet Control Message Protocol.
	Icmp(1),
	//
	// Summary:
	//     Internet Group Management Protocol.
	Igmp(2),
	//
	// Summary:
	//     Gateway To Gateway Protocol.
	Ggp(3),
	//
	// Summary:
	//     Internet Protocol version 4.
	IPv4(4),
	//
	// Summary:
	//     Transmission Control Protocol.
	Tcp(6),
	//
	// Summary:
	//     PARC Universal Packet Protocol.
	Pup(12),
	//
	// Summary:
	//     User Datagram Protocol.
	Udp(17),
	//
	// Summary:
	//     Internet Datagram Protocol.
	Idp(22),
	//
	// Summary:
	//     Internet Protocol version 6 (IPv6).
	IPv6(41),
	//
	// Summary:
	//     IPv6 Routing header.
	IPv6RoutingHeader(43),
	//
	// Summary:
	//     IPv6 Fragment header.
	IPv6FragmentHeader(44),
	//
	// Summary:
	//     IPv6 Encapsulating Security Payload header.
	IPSecEncapsulatingSecurityPayload(50),
	//
	// Summary:
	//     IPv6 Authentication header. For details, see RFC 2292 section 2.2.1, available
	//     at http://www.ietf.org.
	IPSecAuthenticationHeader(51),
	//
	// Summary:
	//     Internet Control Message Protocol for IPv6.
	IcmpV6(58),
	//
	// Summary:
	//     IPv6 No next header.
	IPv6NoNextHeader(59),
	//
	// Summary:
	//     IPv6 Destination Options header.
	IPv6DestinationOptions(60),
	//
	// Summary:
	//     Net Disk Protocol (unofficial).
	ND(77),
	//
	// Summary:
	//     Raw IP packet protocol.
	Raw(255),
	//
	// Summary:
	//     Internet Packet Exchange Protocol.
	Ipx(1000),
	//
	// Summary:
	//     Sequenced Packet Exchange protocol.
	Spx(1256),
	//
	// Summary:
	//     Sequenced Packet Exchange version 2 protocol.
	SpxII(1257), ;

	private int code;

	private ProtocolType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
