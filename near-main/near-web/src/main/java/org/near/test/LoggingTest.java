package org.near.test;

import org.near.push.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
  public final  static Logger LOGGER = LoggerFactory.getLogger(LoggingTest.class);
  public static void main(String[] args) {
    LOGGER.debug("=================================");
    Test.show();
  }
}
