package edu.umd.lib.cidrlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.umd.lib.cidrlist.formatters.IntegerNodeFormatter;

public class NodeList implements Iterable<Node> {
  private List<Node> nodeList = new ArrayList<>();
  private NodeFormatter formatter = new IntegerNodeFormatter();
  /**
   * The maximum number of bits in a node
   */
  // CHECKSTYLE.OFF: MagicNumberCheck
  private int maxBits = 32;
  // CHECKSTYLE.ON

  /**
   * Default constructor.
   */
  public NodeList() {
  }


  /**
   * Constructs a NodeList that supports Nodes with the given maximum number
   * of bits.
   *
   * @param maxBits the maximum number of bits in a node
   */
  public NodeList(final int maxBits) {
    this.maxBits = maxBits;
  }

  /**
   * Constructs a NodeList with an initial Node
   * @param node the initial node for the list
   */
  public NodeList(final Node node) {
    nodeList.add(node);
  }

  /**
   * Constructs a NodeList with an initial Node and maxBits
   *
   * @param node the initial node for the list
   * @param maxBits the maximum number of bits in a node
   */
  public NodeList(final Node node, final int maxBits) {
    this(maxBits);
    nodeList.add(node);
  }

  /**
   * Returns true is Node is exactly present in the list, false otherwise.
   * In this case "exactly" means within the range specified by the prefixLength
   * (when prefixLengths are the same)
   *
   * @param node the Node to check
   * @return true is Node is exactly present in the list, false otherwise
   */
  public boolean contains(final Node node) {
    for (Node n: nodeList) {
      if ((n.getPrefixLength() == node.getPrefixLength()) && nodeContains(n, node)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return true is the list contains Nodes that contain the given Node,
   * false otherwise
   *
   * @param node the Node to check
   * @return true is the list contains Nodes that contain the given Node,
   * false otherwise
   */
  public boolean encompasses(final Node node) {
    for (Node n: nodeList) {
      if (nodeContains(n, node)) {
        return true;
      }
    }
    return false;
  }

  // Removes the given node from the list of nodes
  private void deleteNode(final Node node) {
    nodeList.remove(node);
  }

  // Adds the given node from the list of nodes
  private void insertNode(final Node node) {
    nodeList.add(node);
  }

  private boolean removeEquivalentNode(final Node node) {
    for (Node n: nodeList) {
      if (this.nodeEquals(node, n)) {
        nodeList.remove(n);
        return true;
      }
    }
    return false;
  }

  /**
   * Removes the given Node from the range covered by the given NodeList
   * @param node the Node to remove
   * @param current the NodeList to remove the Node from.
   * @return an updated NodeList with the given Node removed.
   */
  public static NodeList subtractNode(final Node node, final NodeList current) {
    // Check to see if the given node is even in the range covered by the
    // current NodeList
    if (current.encompasses(node)) {
      // In the range, check to see if Node (or its "equivalent", i.e., the
      // value is the same within the limit of the prefixLength)
      boolean nodeRemoved = current.removeEquivalentNode(node);

      if (nodeRemoved) {
        // Equivalent node was removed from list, so return
        // current.deleteNode(node);
        return current;
      }

      if (current.hasParent(node)) {
        // Node is in range covered by list, so insert sibling, and remove
        // parent recursively
        current.insertNode(current.getSibling(node));
        Node parent = current.getParent(node);
        current.nodeList = subtractNode(parent, current).nodeList;
        return current;
      }
      return current;
    }

    // If the node isn't in the list, make sure to remove any nodes in the list
    // that are contained in the given node
    List<Node> prunedList = new ArrayList<>();
    for (Node n: current.nodeList) {
      if (!current.nodeContains(node, n)) {
        prunedList.add(n);
      }
    }

    current.nodeList = prunedList;
    return current;
  }


  /**
   * Adds the given Node to the range covered by the given NodeList
   * @param node the Node to add
   * @param current the NodeList to add the Node to.
   * @return an updated NodeList with the given Node added.
   */
  public static NodeList addNode(final Node node, final NodeList current) {
    if (current.encompasses(node)) {
      // Node is already within the range covered by current NodeList, so
      // do nothing (just return current NodeList)
      return current;
    }

    // Prune from list any nodes that are contained by the node being added
    List<Node> prunedList = new ArrayList<>();
    for (Node n: current) {
      if (!current.nodeContains(node, n)) {
        prunedList.add(n);
      }
    }

    current.nodeList = prunedList;

    // If sibling is in list, "merge" together by removing and adding parent
    if (current.hasParent(node)) {
      Node siblingNode = current.getSibling(node);
      if (current.contains(siblingNode)) {
        current.deleteNode(siblingNode);
        Node parent = current.getParent(node);
        return NodeList.addNode(parent, current);
      } else {
        current.insertNode(node);
      }
    } else {
      // We're at the top of the tree, so add the node:
      current.insertNode(node);
    }

    return current;
  }

  /**
   * Returns a String representation of this object.
   *
   * @return a String representation of this object.
   */
  @Override
  public String toString() {
    // StringBuffer sb = new StringBuffer();
    // for (Node n: nodeList) {
    //   sb.append(n.toString() + "\n");
    // }
    List<String> formattedNodes = new ArrayList<>();
    for (Node n: nodeList) {
      formattedNodes.add(formatter.format(n));
    }

    return formattedNodes.toString();
  }

  /**
   * Returns an Iterator for iterating over the list.
   *
   * @return an Iterator for iterating over the list.
   */
  @Override
  public Iterator<Node> iterator() {
    return Collections.unmodifiableList(nodeList).iterator();
  }

  /**
   * @param node the Node to return the sibling of.
   * @return the Node that is the sibling of the given Node.
   * @throws RuntimeException if this node has no parent
   */
  public Node getSibling(final Node node) {
    String binaryStrValue = Long.toBinaryString(node.getValue());
    int prefixLength = node.getPrefixLength();

    while (binaryStrValue.length() < maxBits) {
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
    Node siblingNode = new Node(siblingValue, prefixLength);
    return siblingNode;
  }

  /**
   * @param node the Node to check
   * @return true if this node has a parent, false otherwise.
   */
  public boolean hasParent(final Node node) {
    return node.getPrefixLength() > 0;
  }

  /**
   * @param node the Node to return the parent of
   * @return the parent Node of this node.
   * @throws RuntimeException if this Node has no parent
   */
  public Node getParent(final Node node) {
    if (!hasParent(node)) {
      throw new RuntimeException("Node has no parent");
    }

    String binaryStrValue = Long.toBinaryString(node.getValue());
    while (binaryStrValue.length() < maxBits) {
      binaryStrValue = "0" + binaryStrValue;
    }

    int parentPrefixLength = node.getPrefixLength() - 1;
    binaryStrValue = binaryStrValue.substring(0, parentPrefixLength);

    while (binaryStrValue.length() < maxBits) {
      binaryStrValue = binaryStrValue + "0";
    }

    long parentValue = Long.parseLong(binaryStrValue, 2);

    return new Node(parentValue, parentPrefixLength);
  }

  /**
   * Returns true if the second Node is within the range specified by the
   * first node, false otherwise.
   *
   * @param thisNode the Node to check to see if it contains the other Node
   * @param thatNode the Node to check to see if is contained
   * @return true if the second Node is within the range specified by the
   * first node, false otherwise.
   */
  public boolean nodeContains(final Node thisNode, final Node thatNode) {
    if (thatNode.getPrefixLength() < thisNode.getPrefixLength()) {
      return false;
    }

    int numShifts = maxBits - thisNode.getPrefixLength();
    if (thatNode.getPrefixLength() == thisNode.getPrefixLength()) {
      long shiftedValue = thisNode.getValue() >> numShifts;
      long testValue = thatNode.getValue() >> numShifts;
      return shiftedValue == testValue;
    }

    Node parent = getParent(thatNode);
    return nodeContains(thisNode, parent);
  }

  /**
   * Returns true if the second Node has the same value as the first Node,
   * based on the prefix length, false otherwise
   *
   * @param thisNode the first Node to check
   * @param thatNode the second Node to check
   * @return true if the second Node has the same value as the first Node,
   * based on the prefix length, false otherwise
   */
  public boolean nodeEquals(final Node thisNode, final Node thatNode) {
    if (thisNode.getPrefixLength() == thatNode.getPrefixLength()) {
      if (thatNode.getValue() == thisNode.getValue()) {
        return true;
      } else {
        int numShifts = maxBits - thisNode.getPrefixLength();
        return (thatNode.getValue() >> numShifts) == (thisNode.getValue() >> numShifts);
      }
    }
    return false;
  }
}
