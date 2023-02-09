package edu.umd.lib.cidrlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import edu.umd.lib.cidrlist.formatters.IntegerNodeFormatter;

class NodeListTest {

  @Test
  void test_getParent() {
    IntegerNodeFormatter formatter = new IntegerNodeFormatter();
    int maxBits = 3;
    NodeList nodeList = new NodeList(maxBits);

    assertEquals("0/0", formatter.format(nodeList.getParent(Node.fromCidr("0/1"))));
    assertEquals("0/0", formatter.format(nodeList.getParent(Node.fromCidr("1/1"))));

    assertEquals("0/1", formatter.format(nodeList.getParent(Node.fromCidr("0/2"))));
    assertEquals("0/1", formatter.format(nodeList.getParent(Node.fromCidr("1/2"))));
    assertEquals("0/1", formatter.format(nodeList.getParent(Node.fromCidr("2/2"))));
    assertEquals("0/1", formatter.format(nodeList.getParent(Node.fromCidr("3/2"))));

    assertEquals("0/2", formatter.format(nodeList.getParent(Node.fromCidr("0/3"))));
    assertEquals("0/2", formatter.format(nodeList.getParent(Node.fromCidr("1/3"))));
    assertEquals("2/2", formatter.format(nodeList.getParent(Node.fromCidr("2/3"))));
    assertEquals("2/2", formatter.format(nodeList.getParent(Node.fromCidr("3/3"))));
    assertEquals("4/2", formatter.format(nodeList.getParent(Node.fromCidr("4/3"))));
    assertEquals("4/2", formatter.format(nodeList.getParent(Node.fromCidr("5/3"))));
    assertEquals("6/2", formatter.format(nodeList.getParent(Node.fromCidr("6/3"))));
    assertEquals("6/2", formatter.format(nodeList.getParent(Node.fromCidr("7/3"))));

    assertEquals("0/0", formatter.format(
      nodeList.getParent(
        nodeList.getParent(
          nodeList.getParent(
            Node.fromCidr("7/3")
          )
        )
      )
    ));

    assertEquals("4/1", formatter.format(nodeList.getParent(Node.fromCidr("4/2"))));
  }

  @Test
  void test_getSibling() {
    int maxBits = 3;
    NodeList nodeList = new NodeList(maxBits);

    assertEquals(new Node(0b000, 3), nodeList.getSibling(new Node(0b001, 3)));
    assertEquals(new Node(0b010, 3), nodeList.getSibling(new Node(0b011, 3)));
    assertEquals(new Node(0b100, 3), nodeList.getSibling(new Node(0b101, 3)));
    assertEquals(new Node(0b110, 3), nodeList.getSibling(new Node(0b111, 3)));
    assertEquals(new Node(0b111, 3), nodeList.getSibling(new Node(0b110, 3)));
    assertEquals(new Node(0b111, 3), nodeList.getSibling(new Node(0b110, 3)));

    assertEquals(new Node(0b110, 2), nodeList.getSibling(new Node(0b100, 2)));
    assertEquals(new Node(0b000, 1), nodeList.getSibling(new Node(0b100, 1)));


    assertEquals(new Node(0b110, 3), nodeList.getSibling(new Node(0b111, 3)));
    assertEquals(new Node(0b111, 3), nodeList.getSibling(new Node(0b110, 3)));
    assertEquals(new Node(0b110, 2), nodeList.getSibling(new Node(0b100, 2)));

    assertEquals(new Node(0b010, 2), nodeList.getSibling(new Node(0b000, 2)));

  }

  @Test
  void test_contains() {
    int maxBits = 3;
    NodeList nodeList = new NodeList(maxBits);

    assertTrue(nodeList.nodeContains(Node.fromCidr("0/0"), Node.fromCidr("6/3")));
    assertTrue(nodeList.nodeContains(Node.fromCidr("0/0"), Node.fromCidr("0/3")));

    assertTrue(nodeList.nodeContains(Node.fromCidr("123.45.67.89/24"), Node.fromCidr("123.45.67.89/32")));
  }

  @Test
  void test_equals() {
    NodeList nodeList = new NodeList(32);

    // equals() does exact match
    assertTrue(nodeList.nodeEquals(Node.fromCidr("123.56.67.89/24"), Node.fromCidr("123.56.67.89/24")));
    // equals() matches anything in same range assuming prefix is the same
    assertTrue(nodeList.nodeEquals(Node.fromCidr("123.56.67.0/24"), Node.fromCidr("123.56.67.123/24")));
    // equals() matches if prefix is not the same
    assertFalse(nodeList.nodeEquals(Node.fromCidr("123.56.67.89/24"), Node.fromCidr("123.56.67.89/25")));
    // equals() does not match if values do not match and outside range
    assertFalse(nodeList.nodeEquals(Node.fromCidr("123.56.67.89/24"), Node.fromCidr("123.56.99.89/23")));
    // equals() does not match if in range buy prefix not the same
    assertFalse(nodeList.nodeEquals(Node.fromCidr("0.0.0.0/0"), Node.fromCidr("123.56.99.89/23")));
  }
}

