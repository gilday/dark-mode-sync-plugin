package com.github.gilday.darkmode;

import com.intellij.concurrency.JobScheduler;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
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

  private final DarculaLookAndFeelInfo darculaLaf = new DarculaLookAndFeelInfo();
  private final IntelliJLookAndFeelInfo intellijLaf = new IntelliJLookAndFeelInfo();

  /** @param lafManager IDEA look-and-feel manager for getting and setting the current theme */
  public DarkModeSync(final LafManager lafManager) {
    final DarkModeDetector detector = new DarkModeDetector(System.getProperties());
    if (!(detector.isMacOS() && detector.isMojaveOrGreater())) {
      logger.error("Plugin only supports macOS Mojave or greater");
      scheduledFuture = null;
      return;
    }
    ScheduledExecutorService executor = JobScheduler.getScheduler();
    scheduledFuture =
        executor.scheduleWithFixedDelay(
            () -> {
              final LookAndFeelInfo current = lafManager.getCurrentLookAndFeel();
              final boolean isDarkMode = detector.isDarkMode();
              if (isDarkMode && !darculaLaf.equals(current)) {
                lafManager.setCurrentLookAndFeel(darculaLaf);
              } else if (!isDarkMode && darculaLaf.equals(current)) {
                lafManager.setCurrentLookAndFeel(intellijLaf);
              }
            },
            0,
            3,
            TimeUnit.SECONDS);
  }

  /** cancels the scheduled task if one exists */
  @Override
  public void dispose() {
    if (scheduledFuture != null) scheduledFuture.cancel(true);
  }

  private static final Logger logger = Logger.getInstance(DarkModeSync.class);
}
