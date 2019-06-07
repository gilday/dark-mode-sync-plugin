package com.github.gilday.darkmode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Queries the environment to see if macOS Dark Mode is enabled */
final class DarkModeDetector {

  /**
   * @return true if the user has enabled macOS Dark Mode
   * @throws IllegalStateException when called on not-macOS
   */
  static boolean isDarkMode() {
    final Process process;
    try {
      process = new ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle").start();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to execute defaults", e);
    }
    try (BufferedReader stdoutReader =
            new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderrReader =
            new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
      final String stdout = stdoutReader.readLine();
      stderrReader.readLine(); // skip first line of output
      final String stderr = stderrReader.readLine();
      if ("Dark".equals(stdout)) {
        return true;
      }
      if ("The domain/default pair of (kCFPreferencesAnyApplication, AppleInterfaceStyle) does not exist"
          .equals(stderr)) {
        return false;
      }
      throw new IllegalStateException("Unexpected AppleInterfaceStyle: " + stdout);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read response from defaults", e);
    }
  }

  private DarkModeDetector() {}
}
