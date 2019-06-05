package com.github.gilday.darkmode;

import com.intellij.concurrency.JobScheduler;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.Disposable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.UIManager.LookAndFeelInfo;

public class DarkModeSync implements Disposable {

  private final ScheduledFuture<?> scheduledFuture;

  private final DarculaLookAndFeelInfo darculaLaf = new DarculaLookAndFeelInfo();
  private final IntelliJLookAndFeelInfo intellijLaf = new IntelliJLookAndFeelInfo();

  public DarkModeSync(final LafManager lafManager) {
    final DarkModeDetector detector = new DarkModeDetector(System.getProperties());
    if (!(detector.isMacOS() && detector.isMojaveOrGreater())) {
      throw new IllegalStateException("Plugin only supports macOS Mojave or greater");
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

  @Override
  public void dispose() {
    scheduledFuture.cancel(true);
  }
}
