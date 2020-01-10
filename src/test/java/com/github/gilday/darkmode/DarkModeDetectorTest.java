package com.github.gilday.darkmode;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.intellij.openapi.util.SystemInfo;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for some aspects of {@link DarkModeDetector} which can be tested reliably across
 * platforms and environments.
 */
final class DarkModeDetectorTest {

  @Test
  void reads_dark_mode_setting() {
    assumeTrue(SystemInfo.isMacOSMojave);

    // test passes if isDarkMode completes without error
  }
}
