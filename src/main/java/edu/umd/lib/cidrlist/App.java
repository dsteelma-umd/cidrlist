package edu.umd.lib.cidrlist;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import edu.umd.lib.cidrlist.formatters.CidrNodeFormatter;

/**
 * $ java -cp target/cidrlist-1.0-SNAPSHOT.jar edu.umd.lib.cidrlist.App
 *
 */
public final class App {
  private App() {
  }

  /**
   * Application entry point
   * @param args the arguments on the commange line
   * @throws IOException if an I/O exception occurs
   */
  public static void main(final String[] args) throws IOException {
    CidrNodeFormatter formatter = new CidrNodeFormatter();
    System.out.print("$ ");
    Scanner scanner = new Scanner(new InputStreamReader(System.in));
    String input = scanner.nextLine();
    NodeList nodeList = new NodeList();

    // Number of bits used by CIDR
    final int numBits = 32;

    while (!"".equals(input)) {
      String[] inputs = input.trim().split(" ");
      if ("add".equals(inputs[0])) {
        String cidrAddress = inputs[1].trim();
        nodeList = NodeList.addNode(Node.fromCidr(cidrAddress, numBits), nodeList);
      } else if ("subtract".equals(inputs[0])) {
        String cidrAddress = inputs[1].trim();
        nodeList = NodeList.subtractNode(Node.fromCidr(cidrAddress, numBits), nodeList);
      } else if ("show".equals(inputs[0])) {
        System.out.println("---------");
        System.out.println("Current Node List");
        System.out.println("---------");
        for (Node n: nodeList) {
          System.out.println(formatter.format(n));
        }
        System.out.println("---------");
        System.out.println();
        System.out.println();
      } else if ("reset".equals(inputs[0])) {
        nodeList = new NodeList();
      } else if ("".equals(input)) {
        System.out.println("Exiting");
        System.exit(0);
      } else {
        System.out.println("Bad input");
      }
      System.out.print("$ ");
      input = scanner.nextLine();

    }
    System.out.println("Exited");
  }
}
