package edu.umd.lib.cidrlist.formatters;

import edu.umd.lib.cidrlist.Node;
import edu.umd.lib.cidrlist.NodeFormatter;

/**
 * Formats a Node as an integer
 */
public class IntegerNodeFormatter implements NodeFormatter {
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
    return node.getValue() + "/" + node.getPrefixLength();
  }
}
