package com.github.gilday.darkmode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Queries the environment to see if macOS Dark Mode is enabled */
final class DarkModeDetector {

  private final Properties properties;
  private final Pattern versionPattern = Pattern.compile("(\\d+).(\\d+).\\d+");

  /**
   * @param properties expected to be {@link System#getProperties()}, but this exists as a seam for
   *     testing.
   */
  DarkModeDetector(Properties properties) {
    this.properties = properties;
  }

  /**
   * @return true when System property "os.name" is "Mac OS X"
   * @throws NullPointerException when required system property "os.name" is missing
   */
  boolean isMacOS() {
    final String os =
        Objects.requireNonNull(
            properties.getProperty("os.name"), "Failed to find standard JVM property os.name");
    return os.equals("Mac OS X");
  }

  /**
   * @return true when System property "os.version" reports version "10.14" or higher
   * @throws NullPointerException when required system property "os.version" is missing
   */
  boolean isMojaveOrGreater() {
    final String version =
        Objects.requireNonNull(
            properties.getProperty("os.version"),
            "Failed to find standard JVM property os.version");
    final Matcher matcher = versionPattern.matcher(version);
    if (!matcher.matches()) {
      throw new IllegalStateException(
          version + " failed to match the expected major.minor.patch version format");
    }
    final String major = matcher.group(1);
    final String minor = matcher.group(2);
    return Integer.valueOf(major) == 10 && Integer.valueOf(minor) >= 14;
  }

  /**
   * @return true if the user has enabled macOS Dark Mode
   * @throws IllegalStateException when called on not-macOS
   */
  boolean isDarkMode() {
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
}
