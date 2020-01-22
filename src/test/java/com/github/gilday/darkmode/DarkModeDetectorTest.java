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
  void isMacOsDarkModeTest() {
    // Test only runs on MacOS Mojave or newer
    assumeTrue(SystemInfo.isMacOSMojave);

    // test passes if isMacOsDarkMode completes without error
    DarkModeDetector.isMacOsDarkMode();
  }

  @Test
  void isWindowsDarkModeTest() {
    // Test only runs on Windows 10 or newer
    assumeTrue(SystemInfo.isWin10OrNewer);

    // test passes if isWindowsDarkMode completes without error
    DarkModeDetector.isWindowsDarkMode();
  }
}
