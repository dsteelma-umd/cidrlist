package edu.umd.lib.cidrlist;

/**
 * Encapsulates a single CIDR Node
 */
public final class Node {
  private long value = 0;
  private int prefixLength = 0;

  /**
   * Creates a Node with the given values
   *
   * @param value the long value for the node
   * @param prefixLength the prefix length
   */
  public Node(final long value, final int prefixLength) {
    this.value = value;
    this.prefixLength = prefixLength;
  }

  /**
   * Creates a Node from the given CIDR-formatted string
   *
   * @param cidr the CIDR-formatted String to use to create the node
   * @return a Node based on the given CIDR String
   */
  public static Node fromCidr(final String cidr) {
    long value = CidrUtils.cidrToLong(cidr);
    int prefixLength = CidrUtils.cidrPrefixLength(cidr);
    return new Node(value, prefixLength);
  }

  /**
   * @return the integer value of this node.
   */
  public long getValue() {
    return value;
  }

  /**
   * @return the prefix length of this node.
   */
  public int getPrefixLength() {
    return prefixLength;
  }

  /**
   * Two nodes are exactly equal if they have the same prefix length, and
   * their value is the same,
   *
   * @param obj the Object to check for equality
   * @return true if the given Object is a Node, and has the same prefix length
   * and value as this node.
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Node)) {
      return false;
    }
    Node n = (Node) obj;
    return (n.prefixLength == this.prefixLength) && (n.value == this.value);
  }

  @Override
  public int hashCode() {
    // CHECKSTYLE.OFF: MagicNumberCheck
    // Based on code in Effective Java, 2nd edition by Joshua Bloch
    int result = 17;
    result = 31 * result + (int) (value ^ (value >>> 32));
    result = 31 * result + prefixLength;
    // CHECKSTYLE.ON
    return result;
  }

  @Override
  public String toString() {
    return Long.toBinaryString(value) + "/" + prefixLength;
  }
}
