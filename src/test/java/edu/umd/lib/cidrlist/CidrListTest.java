package edu.umd.lib.cidrlist;

import org.junit.jupiter.api.Test;

import edu.umd.lib.cidrlist.formatters.CidrNodeFormatter;
import edu.umd.lib.cidrlist.formatters.IntegerNodeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class CidrListTest {
  @Test
  void nodeSubtractionTest() {
    NodeFormatter formatter = new IntegerNodeFormatter();
    NodeList nodeList;

    int maxBits = 3;

    nodeList = new NodeList(Node.fromCidr("0/0"), maxBits);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("0/0"));
    nodeList = NodeList.subtractNode(Node.fromCidr("0/1"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/1"));
    nodeList = NodeList.subtractNode(Node.fromCidr("4/1"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), is(empty()));

    nodeList = new NodeList(Node.fromCidr("0/0"), maxBits);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("0/0"));
    nodeList = NodeList.subtractNode(Node.fromCidr("4/1"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("0/1"));
    nodeList = NodeList.subtractNode(Node.fromCidr("0/1"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), is(empty()));

    nodeList = new NodeList(Node.fromCidr("2/2"), maxBits);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("2/2"));
    nodeList = NodeList.subtractNode(Node.fromCidr("3/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("2/3"));
    nodeList = NodeList.subtractNode(Node.fromCidr("2/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), is(empty()));


    nodeList = new NodeList(Node.fromCidr("0/0"), maxBits);
    nodeList = NodeList.subtractNode(Node.fromCidr("7/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/3", "4/2", "0/1"));


    nodeList = new NodeList(Node.fromCidr("0/0"), maxBits);
    nodeList = NodeList.subtractNode(Node.fromCidr("7/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/3", "4/2", "0/1"));
    nodeList = NodeList.subtractNode(Node.fromCidr("6/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/2", "0/1"));
    nodeList = NodeList.subtractNode(Node.fromCidr("5/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/3", "0/1"));
    nodeList = NodeList.subtractNode(Node.fromCidr("4/3"), nodeList);
    nodeList = NodeList.subtractNode(Node.fromCidr("3/3"), nodeList);
    nodeList = NodeList.subtractNode(Node.fromCidr("2/3"), nodeList);
    nodeList = NodeList.subtractNode(Node.fromCidr("0/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), contains("1/3"));

    CidrNodeFormatter cidrFormatter = new CidrNodeFormatter();
    nodeList = new NodeList(Node.fromCidr("123.45.67.89/32"), 32);
    assertThat(CidrUtils.asFormattedList(nodeList, cidrFormatter), contains("123.45.67.89/32"));
    nodeList = NodeList.subtractNode(Node.fromCidr("123.45.67.89/24"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, cidrFormatter), is(empty()));

    nodeList = new NodeList(32);
    nodeList = NodeList.addNode(Node.fromCidr("123.45.67.88/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("14.24.36.12/24"), nodeList);
    nodeList = NodeList.subtractNode(Node.fromCidr("14.24.36.12/32"), nodeList);
    assertThat(nodeList.encompasses(Node.fromCidr("14.24.36.12/32")), is(false));
  }

  @Test
  void nodeAdditionTest() {
    NodeFormatter formatter = new IntegerNodeFormatter();
    NodeList nodeList;

    int maxBits = 3;

    nodeList = new NodeList(maxBits);
    nodeList = NodeList.addNode(Node.fromCidr("7/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("7/3"));
    nodeList = NodeList.addNode(Node.fromCidr("6/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/2"));
    nodeList = NodeList.addNode(Node.fromCidr("0/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/2", "0/3"));
    nodeList = NodeList.addNode(Node.fromCidr("0/2"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/2", "0/2"));
    nodeList = NodeList.addNode(Node.fromCidr("0/1"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/2", "0/1"));
    nodeList = NodeList.addNode(Node.fromCidr("0/0"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("0/0"));

    nodeList = new NodeList(maxBits);
    nodeList = NodeList.addNode(Node.fromCidr("7/3"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("6/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("6/2"));
    nodeList = NodeList.addNode(Node.fromCidr("5/3"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("4/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/1"));
    nodeList = NodeList.addNode(Node.fromCidr("3/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/1", "3/3"));
    nodeList = NodeList.addNode(Node.fromCidr("2/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("4/1", "2/2"));
    nodeList = NodeList.addNode(Node.fromCidr("1/3"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("0/3"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("0/0"));
  }

  @Test
  void cidrAddressTests() {
    NodeFormatter formatter = new CidrNodeFormatter();
    NodeList nodeList;
    nodeList = new NodeList();

    NodeList.addNode(Node.fromCidr("192.168.4.56/32"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder("192.168.4.56/32"));
    NodeList.addNode(Node.fromCidr("204.45.21.45/24"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder(
        "192.168.4.56/32",
        "204.45.21.45/24"
    ));
    NodeList.addNode(Node.fromCidr("0.0.0.0/32"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder(
        "192.168.4.56/32",
        "204.45.21.45/24",
        "0.0.0.0/32"
    ));
    NodeList.addNode(Node.fromCidr("0.0.0.0/0"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder(
        "0.0.0.0/0"
    ));

    nodeList = new NodeList();
    nodeList = NodeList.addNode(Node.fromCidr("123.45.67.88/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("14.24.36.12/24"), nodeList);
    nodeList = NodeList.subtractNode(Node.fromCidr("14.24.36.12/32"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder(
      "123.45.67.88/32",
       "14.24.36.13/32",
       "14.24.36.14/31",
       "14.24.36.8/30",
       "14.24.36.0/29",
       "14.24.36.16/28",
       "14.24.36.32/27",
       "14.24.36.64/26",
       "14.24.36.128/25"
    ));

    nodeList = new NodeList();
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.0/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.1/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.7/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.3/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.4/32"), nodeList);
    nodeList = NodeList.addNode(Node.fromCidr("192.168.1.2/32"), nodeList);
    assertThat(CidrUtils.asFormattedList(nodeList, formatter), containsInAnyOrder(
      "192.168.1.0/30",
      "192.168.1.4/32",
      "192.168.1.7/32"
    ));
  }
}
