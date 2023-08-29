# Archived

This plugin is no longer maintained, because this functionality is now built into the IDEA platform.

# macOS Dark Mode Sync IDEA Plugin

![](https://github.com/gilday/dark-mode-sync-plugin/workflows/Build%20and%20Test/badge.svg)

Plugin for synchronizing IDEA's theme with macOS's or Windows' Dark Mode. When macOS or Windows is in
dark mode, plugin sets IDEA to the chosen theme, but when macOS or Windows is in light mode, sets
IDEA theme to the theme chosen for light mode.

## Building

    ./gradlew build

## Testing

    ./gradlew runIde

## How Does It Work on MacOS?

The plugin polls the macOS Dark Mode configuration by executing the following
command and interpreting its output.

    defaults read -g AppleInterfaceStyle

When the plugin detects that macOS is using Dark Mode, it sets the IDEA theme to
the selected theme. When the plugin detects that macOS is not using Dark Mode, it sets
the IDEA theme to the corresponding theme.

## How Does It Work on Windows?

The plugin polls the Windows Dark Mode configuration by executing the following
command and interpreting its output.

    reg query HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme

When the plugin detects that Windows is using Dark Mode, it sets the IDEA theme to
the selected theme. When the plugin detects that Windows is not using Dark Mode, it sets
the IDEA theme to corresponding theme.

## Attributions

Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from [www.flaticon.com](https://www.flaticon.com/)
