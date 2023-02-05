package edu.umd.lib.cidrlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CidrUtilsTest {

  @Test
  void test_cidrPrefixLength() {
    assertEquals(0, CidrUtils.cidrPrefixLength("0/0"));
  }

}
