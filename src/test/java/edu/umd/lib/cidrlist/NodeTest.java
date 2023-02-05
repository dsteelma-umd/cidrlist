package edu.umd.lib.cidrlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import edu.umd.lib.cidrlist.formatters.IntegerNodeFormatter;

class NodeTest {

  @Test
  void test_getParent() {
    IntegerNodeFormatter formatter = new IntegerNodeFormatter();
    int numBits = 3;

    assertEquals("0/0", formatter.format(Node.fromCidr("0/1", numBits).getParent()));
    assertEquals("0/0", formatter.format(Node.fromCidr("1/1", numBits).getParent()));

    assertEquals("0/1", formatter.format(Node.fromCidr("0/2", numBits).getParent()));
    assertEquals("0/1", formatter.format(Node.fromCidr("1/2", numBits).getParent()));
    assertEquals("0/1", formatter.format(Node.fromCidr("2/2", numBits).getParent()));
    assertEquals("0/1", formatter.format(Node.fromCidr("3/2", numBits).getParent()));

    assertEquals("0/2", formatter.format(Node.fromCidr("0/3", numBits).getParent()));
    assertEquals("0/2", formatter.format(Node.fromCidr("1/3", numBits).getParent()));
    assertEquals("2/2", formatter.format(Node.fromCidr("2/3", numBits).getParent()));
    assertEquals("2/2", formatter.format(Node.fromCidr("3/3", numBits).getParent()));
    assertEquals("4/2", formatter.format(Node.fromCidr("4/3", numBits).getParent()));
    assertEquals("4/2", formatter.format(Node.fromCidr("5/3", numBits).getParent()));
    assertEquals("6/2", formatter.format(Node.fromCidr("6/3", numBits).getParent()));
    assertEquals("6/2", formatter.format(Node.fromCidr("7/3", numBits).getParent()));

    assertEquals("0/0", formatter.format(Node.fromCidr("7/3", numBits).getParent().getParent().getParent()));

    assertEquals("4/1", formatter.format(Node.fromCidr("4/2", numBits).getParent()));
  }

  @Test
  void test_getSibling() {
    int numBits = 3;

    assertEquals(new Node(0b000, 3, numBits), (new Node(0b001, 3, numBits)).getSibling());
    assertEquals(new Node(0b010, 3, numBits), (new Node(0b011, 3, numBits)).getSibling());
    assertEquals(new Node(0b100, 3, numBits), (new Node(0b101, 3, numBits)).getSibling());
    assertEquals(new Node(0b110, 3, numBits), (new Node(0b111, 3, numBits)).getSibling());
    assertEquals(new Node(0b111, 3, numBits), (new Node(0b110, 3, numBits)).getSibling());
    assertEquals(new Node(0b111, 3, numBits), (new Node(0b110, 3, numBits)).getSibling());

    assertEquals(new Node(0b110, 2, numBits), (new Node(0b100, 2, numBits)).getSibling());
    assertEquals(new Node(0b000, 1, numBits), (new Node(0b100, 1, numBits)).getSibling());


    assertEquals(new Node(0b110, 3, numBits), (new Node(0b111, 3, numBits)).getSibling());
    assertEquals(new Node(0b111, 3, numBits), (new Node(0b110, 3, numBits)).getSibling());
    assertEquals(new Node(0b110, 2, numBits), (new Node(0b100, 2, numBits)).getSibling());

    assertEquals(new Node(0b010, 2, numBits), (new Node(0b000, 2, numBits)).getSibling());

  }

  @Test
  void test_contains() {
    int numBits = 3;
   assertTrue(Node.fromCidr("0/0", numBits).contains(Node.fromCidr("6/3", numBits)));
   assertTrue(Node.fromCidr("0/0", numBits).contains(Node.fromCidr("0/3", numBits)));

    assertTrue(Node.fromCidr("123.45.67.89/24", 32).contains(Node.fromCidr("123.45.67.89/32", 32)));
  }

}
