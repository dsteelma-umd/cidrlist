package edu.umd.lib.cidrlist.formatters;

import edu.umd.lib.cidrlist.Node;
import edu.umd.lib.cidrlist.NodeFormatter;

/**
 * Formats a Node as a zero-padded binary string with a minimum length
 */
public class BinaryNodeFormatter implements NodeFormatter {
  private int minimumLength;

  /**
   * Constructor
   * @param minimumLength the minimum number of digits to zero-pad the number to
   */
  public BinaryNodeFormatter(final int minimumLength) {
    this.minimumLength = minimumLength;
  }

  /**
   * Returns a String representation of the given Node as a binary number with
   * a prefix, zero padding the number of the left as needed to ensure the
   * minimum length.
   *
   * @param node the Node to format
   * @return a String representation of the given Node as a binary number with
   * a prefix, zero padding the number of the left as needed to ensure the
   * minimum length.
   */
  public String format(final Node node) {
    String binaryString = Long.toBinaryString(node.getValue());
    while (binaryString.length() < minimumLength) {
      binaryString = "0" + binaryString;
    }
    return binaryString + "/" + node.getPrefixLength();
  }
}
