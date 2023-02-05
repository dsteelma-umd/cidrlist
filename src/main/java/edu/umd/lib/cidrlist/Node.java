package edu.umd.lib.cidrlist;

/**
 * Encapsulates a single CIDR Node
 */
public final class Node {
  private long value = 0;
  private int prefixLength = 0;
  private int minBits;

  /**
   * Creates a Node with the given values
   *
   * @param value the long value for the node
   * @param prefixLength the prefix length
   * @param numBits the total number of bits used by all nodes
   */
  public Node(final long value, final int prefixLength, final int numBits) {
    this.value = value;
    this.prefixLength = prefixLength;
    this.minBits = numBits;
  }

  /**
   * Creates a Node from the given CIDR-formatted string
   *
   * @param cidr the CIDR-formatted String to use to create the node
   * @param numBits the total nunber of bits for all nodes
   * @return a Node based on the given CIDR String
   */
  public static Node fromCidr(final String cidr, final int numBits) {
    long value = CidrUtils.cidrToLong(cidr);
    int prefixLength = CidrUtils.cidrPrefixLength(cidr);
    return new Node(value, prefixLength, numBits);
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
   * @return true if this node has a parent, false otherwise.
   */
  public boolean hasParent() {
    return prefixLength > 0;
  }

  /**
   * @return the parent Node of this node.
   * @throws RuntimeException if this Node has no parent
   */
  public Node getParent() {
    if (!hasParent()) {
      throw new RuntimeException("Node has no parent");
    }

    String binaryStrValue = Long.toBinaryString(value);
    while (binaryStrValue.length() < minBits) {
      binaryStrValue = "0" + binaryStrValue;
    }

    int parentPrefixLength = prefixLength - 1;
    binaryStrValue = binaryStrValue.substring(0, parentPrefixLength);

    while (binaryStrValue.length() < minBits) {
      binaryStrValue = binaryStrValue + "0";
    }

    long parentValue = Long.parseLong(binaryStrValue, 2);

    return new Node(parentValue, parentPrefixLength, minBits);
  }

  /**
   * Returns true if the given Node is within the range specified by this node,
   * false otherwise.
   *
   * @param node the node to check.
   * @return true if the given Node is within the range specified by this node,
   * false otherwise.
   */
  public boolean contains(final Node node) {
    if (node.prefixLength < this.prefixLength) {
      return false;
    }

    if (node.prefixLength == this.prefixLength) {
      long shiftedValue = this.value >> (this.minBits - prefixLength);
      long testValue = node.value >> (this.minBits - prefixLength);
      return shiftedValue == testValue;
    }

    Node parent = node.getParent();
    return contains(parent);
  }

  /**
   * @return the Node that is a sibling of this node.
   * @throws RuntimeException if this node has no parent
   */
  public Node getSibling() {
    String binaryStrValue = Long.toBinaryString(value);
    while (binaryStrValue.length() < minBits) {
      binaryStrValue = "0" + binaryStrValue;
    }

    int digitToReplace = prefixLength - 1;
    char[] binaryDigits = binaryStrValue.toCharArray();
    if (binaryDigits[digitToReplace] == '0') {
      binaryDigits[digitToReplace] = '1';
    } else {
      binaryDigits[digitToReplace] = '0';
    }

    String siblingBinaryStrValue = new String(binaryDigits);
    long siblingValue = Long.parseLong(siblingBinaryStrValue, 2);
    Node siblingNode = new Node(siblingValue, prefixLength, minBits);
    return siblingNode;
  }

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
    result = 31 * result + minBits;
    // CHECKSTYLE.ON
    return result;
  }

  @Override
  public String toString() {
    return Long.toBinaryString(value) + "/" + prefixLength;
  }
}
