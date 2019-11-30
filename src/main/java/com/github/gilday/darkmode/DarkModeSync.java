package com.github.gilday.darkmode;

import com.intellij.concurrency.JobScheduler;
import com.intellij.ide.actions.QuickChangeLookAndFeel;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;

import javax.swing.UIManager.LookAndFeelInfo;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.github.gilday.darkmode.DarkModeDetector.isDarkMode;

/**
 * Application component which sets IDEA's theme to Darcula when it detects that macOS is in Dark
 * Mode, and sets the theme to IntelliJ when it detects that macOS is not in Dark Mode.
 *
 * <p>Schedules a background job for polling macOS Dark Mode configuration and updating IDEA
 * configuration accordingly.
 */
public final class DarkModeSync implements Disposable {

  private final ScheduledFuture<?> scheduledFuture;

  private final LookAndFeelInfo darkLaf = new DarculaLookAndFeelInfo();
  private final LookAndFeelInfo lightLaf = new IntelliJLookAndFeelInfo();

  private final LafManager lafManager;

  /** @param lafManager IDEA look-and-feel manager for getting and setting the current theme */
  public DarkModeSync(final LafManager lafManager) {
    this.lafManager = lafManager;
    if (!SystemInfo.isMacOSMojave) {
      logger.error("Plugin only supports macOS Mojave or greater");
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
    final boolean isDarkMode = isDarkMode();
    if (isDarkMode && !darkLaf.equals(current)) {
      updateLaf(darkLaf);
    } else if (!isDarkMode && darkLaf.equals(current)) {
      updateLaf(lightLaf);
    }
  }

  private void updateLaf(final LookAndFeelInfo newLaf) {
    QuickChangeLookAndFeel.switchLafAndUpdateUI(lafManager, newLaf, true);
  }

  private static final Logger logger = Logger.getInstance(DarkModeSync.class);
}
