package edu.umd.lib.cidrlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NodeList implements Iterable<Node> {
  private List<Node> nodeList = new ArrayList<>();

  /**
   * Default constructor.
   */
  public NodeList() {
  }

  /**
   * Constructs a NodeList with an initial Node
   * @param node the initial node for the list
   */
  public NodeList(final Node node) {
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
      if ((n.getPrefixLength() == node.getPrefixLength()) && n.contains(node)) {
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
      if (n.contains(node)) {
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
      // In the range, check to see if Node is in list as itself
      if (current.contains(node)) {
        // Node is in the list, so just remove
        current.deleteNode(node);
        return current;
      } else if (node.hasParent()) {
        // Node is in range covered by list, so insert sibling, ands remove
        // parent recursively
        current.insertNode(node.getSibling());
        Node parent = node.getParent();
        subtractNode(parent, current);
        return current;
      }
    }

    // If the node isn't in the list, make sure to remove any nodes in the list
    // that are contained in the given node
    List<Node> prunedList = new ArrayList<>();
    for (Node n: current.nodeList) {
      if (!node.contains(n)) {
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
      if (!node.contains(n)) {
        prunedList.add(n);
      }
    }

    current.nodeList = prunedList;

    // If sibling is in list, "merge" together by removing and adding parent
    if (node.hasParent()) {
      Node siblingNode = node.getSibling();
      if (current.contains(siblingNode)) {
        current.deleteNode(siblingNode);
        Node parent = node.getParent();
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
    StringBuffer sb = new StringBuffer();
    for (Node n: nodeList) {
      sb.append(n.toString() + "\n");
    }
    return sb.toString();
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
}
