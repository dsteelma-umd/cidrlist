package edu.umd.lib.cidrlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
  private ByteArrayOutputStream out;
  private ByteArrayOutputStream err;
  private ByteArrayInputStream in;

  private String[] args;

  @BeforeEach
  void setUp() {
    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));

    args = new String[0];

  }

  @Test
  void testApp_exit() throws IOException {
    String testInput = "\n";
    in = new ByteArrayInputStream(testInput.getBytes());
    System.setIn(in);

    App.main(args);
    String expectedOutput = "$ Exited\n";
    assertEquals(expectedOutput, out.toString());
  }

  @Test
  void testApp_sampleRun() throws IOException {
    String testInput = "add 123.45.67.89/32\n"
                       + "add 123.45.67.90/32\n"
                       + "add 10.0.0.0/24\n"
                       + "subtract 10.1.2.3/31\n"
                       + "show\n"
                       + "reset\n"
                       + "add 0.0.0.0/0\n"
                       + "show\n"
                       + "\n";
    in = new ByteArrayInputStream(testInput.getBytes());
    System.setIn(in);

    App.main(args);
    String expectedOutput =
    "$ $ $ $ $ ---------\n"
    + "Current Node List\n"
    + "---------\n"
    + "123.45.67.89/32\n"
    + "123.45.67.90/32\n"
    + "10.0.0.0/24\n"
    + "---------\n"
    + "\n"
    + "\n"
    + "$ $ $ ---------\n"
    + "Current Node List\n"
    + "---------\n"
    + "0.0.0.0/0\n"
    + "---------\n"
    + "\n"
    + "\n"
    + "$ Exited\n";
    // $ add 123.45.67.89/32
    // $ add 123.45.67.90/32
    // $ add 10.0.0.0/24
    // $ subtract 10.1.2.3/31
    // $ show
    // ---------
    // Current Node List
    // ---------
    // 123.45.67.89/32
    // 123.45.67.90/32
    // 10.0.0.0/24
    // ---------


    // $ reset
    // $ add 0.0.0.0/0
    // $ show
    // ---------
    // Current Node List
    // ---------
    // 0.0.0.0/0
    // ---------


    // $
    // Exited
    assertEquals(expectedOutput, out.toString());
  }
}