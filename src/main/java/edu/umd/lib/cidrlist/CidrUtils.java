package edu.umd.lib.cidrlist;

import java.util.ArrayList;
import java.util.List;

public final class CidrUtils {
  private CidrUtils() {
  }

  /**
   * Converts the given long value and prefix length to a CIDR-formatted string.
   *
   * @param value the long value of the CIDR address
   * @param prefixLength the CIDR prefix length
   * @return a CIDR-formatted String representing the given value and prefix
   * length.
   */
  public static String longToCidr(final long value, final int prefixLength) {
    final int padBinaryLengthTo = 32;

    String binaryString = Long.toBinaryString(value);

    while (binaryString.length() < padBinaryLengthTo) {
      binaryString = "0" + binaryString;
    }

    // Split into quads
    // CHECKSTYLE.OFF: MagicNumberCheck
    String quad1 = binaryString.substring(0, 8);
    String quad2 = binaryString.substring(8, 16);
    String quad3 = binaryString.substring(16, 24);
    String quad4 = binaryString.substring(24, 32);
    // CHECKSTYLE.ON

    int quadInt1 = Integer.parseInt(quad1, 2);
    int quadInt2 = Integer.parseInt(quad2, 2);
    int quadInt3 = Integer.parseInt(quad3, 2);
    int quadInt4 = Integer.parseInt(quad4, 2);

    return quadInt1 + "." + quadInt2 + "." + quadInt3 + "." + quadInt4 + "/" + prefixLength;
  }

  /**
   * Returns the long value of the given CIDR-formatted string
   *
   * @param cidr the CIDR-formatted string to return the long value of.
   * @return the long value of the given CIDR-formatted string
   */
  public static long cidrToLong(final String cidr) {
    String cidrWithoutPrefix = cidr.replaceAll("/.*", "");
    String[] quads = cidrWithoutPrefix.split("\\.");

    final int quadShift = 8;
    final int radix = 10;
    long cidrInt = 0;
    for (int i = 0; i < quads.length; i++) {
      cidrInt = (cidrInt << quadShift) + Integer.parseInt(quads[i], radix);
    }

    return cidrInt;
  }

  /**
   * Returns the prefix length of the given CIDR-formatted string
   *
   * @param cidr the CIDR-formatted string to return the prefix length of.
   * @return the prefix length of the given CIDR-formatted string
   */
  public static int cidrPrefixLength(final String cidr) {
    String[] elements = cidr.split("/");
    return Integer.parseInt(elements[1]);
  }

  /**
   * Returns the entries in the given NodeList, formatted using the given
   * NodeFormatter.
   *
   * @param nodeList the NodeList to return the entries of
   * @param nodeFormatter the NodeFormatter to use to format the entries
   * @return the entries in the given NodeList, formatted using the given
   * NodeFormatter.
   */
  public static List<String> asFormattedList(final NodeList nodeList, final NodeFormatter nodeFormatter) {
    List<String> result = new ArrayList<>();

    for (Node node: nodeList) {
      result.add(nodeFormatter.format(node));
    }
    return result;
  }
}
