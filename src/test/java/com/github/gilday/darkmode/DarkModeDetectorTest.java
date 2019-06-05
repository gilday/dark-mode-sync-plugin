package com.github.gilday.darkmode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for some aspects of {@link DarkModeDetector} which can be tested reliably across
 * platforms and environments.
 */
final class DarkModeDetectorTest {

  @CsvSource({"Mac OS X,true", "Linux,false"})
  @ParameterizedTest
  void reads_os_name(final String os, final boolean expected) {
    final Properties properties = new Properties();
    properties.setProperty("os.name", os);
    final DarkModeDetector detector = new DarkModeDetector(properties);
    assertEquals(detector.isMacOS(), expected);
  }

  @CsvSource({"10.13.6,false", "10.14.5,true", "10.15.1,true"})
  @ParameterizedTest
  void reads_os_version(final String version, final boolean expected) {
    final Properties properties = new Properties();
    properties.setProperty("os.version", version);
    final DarkModeDetector detector = new DarkModeDetector(properties);
    assertEquals(detector.isMojaveOrGreater(), expected);
  }

  @Test
  void reads_dark_mode_setting() {
    final DarkModeDetector detector = new DarkModeDetector(System.getProperties());
    assumeTrue(detector.isMacOS() && detector.isMojaveOrGreater());

    // test passes if isDarkMode completes without error
    detector.isDarkMode();
  }
}
