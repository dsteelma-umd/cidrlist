package edu.umd.lib.cidrlist.formatters;

import edu.umd.lib.cidrlist.CidrUtils;
import edu.umd.lib.cidrlist.Node;
import edu.umd.lib.cidrlist.NodeFormatter;

/**
 * Formats a Node as a CIDR string.
 */
public class CidrNodeFormatter implements NodeFormatter {
  /**
   * Returns a String representation of the Node in CIDR format
   *
   * @param node the Node to format
   * @return a String representation of the Node in CIDR format
   */
  public String format(final Node node) {
    return CidrUtils.longToCidr(node.getValue(), node.getPrefixLength());
  }
}
