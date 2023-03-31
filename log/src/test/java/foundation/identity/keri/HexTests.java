package foundation.identity.keri;


import org.junit.jupiter.api.Test;

import static foundation.identity.keri.Hex.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class HexTests {

	@Test
	 void testHex() {
		var bytes = new byte[]{0x01, 0x09, 0x0a, 0x0f, 0x10, (byte) 0xf0, (byte) 0xff};

		var hex = hex(bytes);

		assertEquals("01090a0f10f0ff", hex);
	}

	@Test
	 void testUnhex() {
		var bytes = unhex("01090a0f10f0ff");

		var expected = new byte[]{0x01, 0x09, 0x0a, 0x0f, 0x10, (byte) 0xf0, (byte) 0xff};
	}

	@Test
	 void testLongToHex() {
		assertEquals("-8000000000000000", hexNoPad(Long.MIN_VALUE));
		assertEquals("-1", hexNoPad(-1));
		assertEquals("0", hexNoPad(0));
		assertEquals("1", hexNoPad(1));
		assertEquals("2", hexNoPad(2));
		assertEquals("a", hexNoPad(10));
		assertEquals("10", hexNoPad(16));
		assertEquals("7fffffffffffffff", hexNoPad(Long.MAX_VALUE));
	}

	@Test
	 void testHexToLong() {
		assertEquals(Long.MIN_VALUE, unhexLong("-8000000000000000"));
		assertEquals(-1, unhexLong("-1"));
		assertEquals(0, unhexLong("0"));
		assertEquals(1, unhexLong("1"));
		assertEquals(1, unhexLong("1"));
		assertEquals(2, unhexLong("2"));
		assertEquals(10, unhexLong("a"));
		assertEquals(16, unhexLong("10"));
		assertEquals(Long.MAX_VALUE, unhexLong("7fffffffffffffff"));
	}

}
