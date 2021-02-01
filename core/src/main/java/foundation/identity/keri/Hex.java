package foundation.identity.keri;

import java.math.BigInteger;
import java.util.Arrays;

public final class Hex {

  /**
   * Maps nibble values to hex characters
   */
  private static final byte[] HEX = {
      '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /**
   * Maps hex characters to nibble values
   */
  private static final byte[] NIBBLES;

  static {
    NIBBLES = new byte[128];
    Arrays.fill(NIBBLES, (byte) 0xff);
    for (var i = 0; i < HEX.length; i++) {
      NIBBLES[HEX[i]] = (byte) i;
    }
    for (var i = 'A'; i <= 'F'; i++) {
      NIBBLES[i] = NIBBLES[(i + 'a') - 'A'];
    }
  }

  private Hex() {
    throw new IllegalStateException("Do not instantiate.");
  }

  public static String hex(byte[] bytes) {
    final var hex = new StringBuilder(2 * bytes.length);

    for (final byte b : bytes) {
      hex.append((char) (HEX[(b & 0xff) >>> 4]));
      hex.append((char) (HEX[b & 0x0f]));
    }

    return hex.toString();
  }

  public static String hexNoPad(int i) {
    return Integer.toString(i, 16);
  }

  public static String hexNoPad(long l) {
    return Long.toString(l, 16);
  }

  public static String hex(BigInteger b) {
    return b.toString(16);
  }

  public static byte[] unhex(String hex) {
    if ((hex.length() % 2) != 0) {
      throw new IllegalArgumentException("hex must have an even length.");
    }

    var bytes = new byte[hex.length() / 2];

    for (var i = 0; i < bytes.length; i++) {
      var b1 = NIBBLES[hex.charAt(i * 2)];
      var b2 = NIBBLES[hex.charAt((i * 2) + 1)];

      var b = (b1 << 4) | b2;

      if (b < 0) {
        throw new RuntimeException("invalid characters encountered in Hex string");
      }

      bytes[i] = (byte) b;
    }

    return bytes;
  }

  public static int unhexInt(String hex) {
    return Integer.parseInt(hex, 16);
  }

  public static long unhexLong(String hex) {
    return Long.parseLong(hex, 16);
  }

  public static BigInteger unhexBigInteger(String hex) {
    return new BigInteger(hex, 16);
  }

}
