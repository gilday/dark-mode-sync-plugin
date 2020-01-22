package com.github.gilday.darkmode;

import static com.github.gilday.darkmode.DarkModeDetector.isMacOsDarkMode;
import static com.github.gilday.darkmode.DarkModeDetector.isWindowsDarkMode;

import com.intellij.concurrency.JobScheduler;
import com.intellij.ide.actions.QuickChangeLookAndFeel;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Application component which sets IDEA's theme to Darcula when it detects that macOS is in Dark
 * Mode, and sets the theme to IntelliJ when it detects that macOS is not in Dark Mode.
 *
 * <p>Schedules a background job for polling macOS Dark Mode configuration and updating IDEA
 * configuration accordingly.
 */
public final class DarkModeSync implements Disposable {

  private final ScheduledFuture<?> scheduledFuture;

  private final LafManager lafManager;
  private final DarkModeSyncThemes themes;

  /** @param lafManager IDEA look-and-feel manager for getting and setting the current theme */
  public DarkModeSync(final LafManager lafManager) {
    themes = ServiceManager.getService(DarkModeSyncThemes.class);
    this.lafManager = lafManager;
    // Checks if OS is Windows or MacOS
    if (!(SystemInfo.isMacOSMojave || SystemInfo.isWin10OrNewer)) {
      logger.error("Plugin only supports macOS Mojave and greater or Windows 10 and greater");
      scheduledFuture = null;
      return;
    }
    ScheduledExecutorService executor = JobScheduler.getScheduler();
    scheduledFuture =
        executor.scheduleWithFixedDelay(this::updateLafIfNecessary, 0, 3, TimeUnit.SECONDS);
  }

  /** cancels the scheduled task if one exists */
  @Override
  public void dispose() {
    if (scheduledFuture != null) scheduledFuture.cancel(true);
  }

  private void updateLafIfNecessary() {
    final LookAndFeelInfo current = lafManager.getCurrentLookAndFeel();
    boolean isDarkMode;

    if (SystemInfo.isMacOSMojave) {
      isDarkMode = isMacOsDarkMode();
    } else if (SystemInfo.isWin10OrNewer) {
      isDarkMode = isWindowsDarkMode();
    } else {
      throw new IllegalStateException(
          "Plugin only works on MacOS Mojave and newer or Windows 10 and newer.");
    }
    final LookAndFeelInfo dark = themes.getDark();
    final LookAndFeelInfo light = themes.getLight();
    if (isDarkMode && !dark.equals(current)) {
      updateLaf(dark);
    } else if (!isDarkMode && !light.equals(current)) {
      updateLaf(light);
    }
  }

  private void updateLaf(final LookAndFeelInfo newLaf) {
    QuickChangeLookAndFeel.switchLafAndUpdateUI(lafManager, newLaf, true);
  }

  private static final Logger logger = Logger.getInstance(DarkModeSync.class);
}
