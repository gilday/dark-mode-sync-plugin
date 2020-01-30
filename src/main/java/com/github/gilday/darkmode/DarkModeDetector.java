package com.github.gilday.darkmode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Queries the environment to see if Dark Mode is enabled */
final class DarkModeDetector {

  /**
   * @return true if the user has enabled Dark Mode
   * @throws IllegalStateException when plugin fails to read system dark mode setting. 
   */
  static boolean isMacOsDarkMode() {
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

  static Boolean isWindowsDarkMode() {
    final Process process;

    try {
      process =
          Runtime.getRuntime()
              .exec(
                  "reg query "
                      + "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize"
                      + " /v "
                      + "AppsUseLightTheme");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to execute Windows registry query!", e);
    }

    try (BufferedReader stdoutReader =
        new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      stdoutReader.readLine();
      stdoutReader.readLine();
      final String stdout = stdoutReader.readLine();
      if (stdout.endsWith("0")) {
        return true;
      } else if (stdout.endsWith("1")) {
        return false;
      }
      throw new IllegalStateException(
          "Unexpected Registry Value for AppsUseLightThemes: " + stdout);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read response from Registry query!", e);
    }
  }

  private DarkModeDetector() {}
}
