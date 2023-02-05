package edu.umd.lib.cidrlist;

/**
 * Base interface for converting a Node to a String
 */
public interface NodeFormatter {
  /**
   * Returns a String representation of the given Node
   * @param node the Node to represent
   * @return a String representation of the given Node
   */
  String format(Node node);
}
