package com.github.gilday.darkmode;

import static com.github.gilday.darkmode.DarkModeDetector.isMacOsDarkMode;
import static com.github.gilday.darkmode.DarkModeDetector.isWindowsDarkMode;

import com.intellij.concurrency.JobScheduler;
import com.intellij.ide.actions.QuickChangeLookAndFeel;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.Alarm;
import com.intellij.util.Alarm.ThreadToUse;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Application component which sets IDEA's theme to Darcula when it detects that macOS is in Dark
 * Mode, and sets the theme to IntelliJ when it detects that macOS is not in Dark Mode.
 *
 * <p>Schedules a background job for polling macOS Dark Mode configuration and updating IDEA
 * configuration accordingly.
 */
public final class DarkModeSync implements Disposable {

  private ScheduledFuture<?> scheduledFuture;

  private LafManager lafManager;
  private DarkModeSyncThemes themes;
  private Alarm updateOnUIThreadAlarm;

  private static final class MyStartupActivity implements StartupActivity.DumbAware {
    @Override
    public void runActivity(@NotNull Project project) {
      getInstance(project).onStartup();
    }
  }

  public static DarkModeSync getInstance(@NotNull Project project) {
    return project.getService(DarkModeSync.class);
  }

  public void onStartup(){
    themes = ServiceManager.getService(DarkModeSyncThemes.class);
    // lafManager IDEA look-and-feel manager for getting and setting the current theme
    this.lafManager = LafManager.getInstance();
    // Checks if OS is Windows or MacOS
    if (!(SystemInfo.isMacOSMojave || SystemInfo.isWin10OrNewer)) {
      logger.error("Plugin only supports macOS Mojave and greater or Windows 10 and greater");
      scheduledFuture = null;
      updateOnUIThreadAlarm = null;
      return;
    }
    final ScheduledExecutorService executor = JobScheduler.getScheduler();
    scheduledFuture =
        executor.scheduleWithFixedDelay(this::updateLafIfNecessary, 0, 3, TimeUnit.SECONDS);
    // initialize this last because it publishes "this"
    updateOnUIThreadAlarm = new Alarm(ThreadToUse.SWING_THREAD, this);
  }

  /** cancels the scheduled task if one exists */
  @Override
  public void dispose() {
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }
    if (updateOnUIThreadAlarm != null) {
      updateOnUIThreadAlarm.cancelAllRequests();
    }
  }

  private void updateLafIfNecessary() {
    final LookAndFeelInfo current = lafManager.getCurrentLookAndFeel();
    final boolean isDarkMode;

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

  /**
   * Uses the {@link #updateOnUIThreadAlarm} to schedule a look and feel update on the Swing UI
   * thread
   */
  private void updateLaf(final LookAndFeelInfo newLaf) {
    updateOnUIThreadAlarm.addRequest(
        () -> QuickChangeLookAndFeel.switchLafAndUpdateUI(lafManager, newLaf, false), 0);
  }

  private static final Logger logger = Logger.getInstance(DarkModeSync.class);
}
