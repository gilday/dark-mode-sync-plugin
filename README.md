# macOS Dark Mode Sync IDEA Plugin

[![Build Status](https://travis-ci.com/gilday/dark-mode-sync-plugin.svg?branch=master)](https://travis-ci.com/gilday/dark-mode-sync-plugin)

Plugin for synchronizing IDEA's theme with macOS's or Windows' Dark Mode. When OS is in
dark mode, plugin sets IDEA to Darcula, but when macOS is in light mode, sets
IDEA theme to IntelliJ.

Alternatively you can choose which theme is used using the IDEA Settings -> Dark Mode Sync.

## Building

    ./gradlew build

## Testing

    ./gradlew runIde

## How Does It Work On Mac?

The plugin polls the macOS Dark Mode configuration by executing the following
command and interpreting its output.

    defaults read -g AppleInterfaceStyle

When the plugin detects that macOS is using Dark Mode, it sets the IDEA theme to
"Darcula" or the chosen theme. When the plugin detects that macOS is not using Dark Mode, it sets
the IDEA theme to "IntelliJ" or the chosen theme.

## How Does It Work On Windows?

The plugin polls the Windows registry by executing the following command 
and interpreting its output:

    reg query HKEY_LOCAL_USER\Software\Microsoft\CurrentVersion\Theme\Personalize /v AppsUseLightTheme

When the plugin detects that Apps should use a dark theme based on the setting in Window, it sets the IDEA theme to "Darcula" or the chosen theme. When the plugin detects that apps are supposed to use a light theme, it sets the IDEA theme to "IntelliJ" or the chosen theme.
