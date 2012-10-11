package uk.co.thomasc.steamkit.types.steamid;

import lombok.Getter;
import lombok.Setter;

public class BitVector64 {
	@Getter @Setter private Long data;

	public BitVector64() {
	}

	public BitVector64(long value) {
		data = value;
	}

	public long getMask(short bitoffset, int valuemask) {
		return data >> bitoffset & valuemask;
	}

	public void setMask(short bitoffset, long valuemask, long value) {
		data = (data & ~(valuemask << bitoffset)) | ((value & valuemask) << bitoffset);
	}
}
