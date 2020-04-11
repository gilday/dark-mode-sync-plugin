package com.github.gilday.darkmode

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import javax.swing.UIManager

@State(name = "DarkModeSync", storages = [Storage("dark-mode-sync.xml", roamingType = RoamingType.PER_OS)])
class DarkModeSyncThemes : PersistentStateComponent<DarkModeSyncThemes.State> {

    @Volatile
    var dark: UIManager.LookAndFeelInfo = DEFAULT_DARK_THEME

    @Volatile
    var light: UIManager.LookAndFeelInfo = DEFAULT_LIGHT_THEME

    override fun getState(): State? {
        return State(dark.name, dark.className, light.name, light.className)
    }

    override fun loadState(state: State) {
        val lafManager = LafManager.getInstance()
        val lafs = lafManager.installedLookAndFeels
        dark = lafs.find { it.name == state.darkName && it.className == state.darkClassName }
                ?: DEFAULT_DARK_THEME
        light = lafs.find { it.name == state.lightName && it.className == state.lightClassName }
                ?: DEFAULT_LIGHT_THEME
    }

    data class State(
            var darkName: String? = DEFAULT_DARK_THEME.name,
            var darkClassName: String? = DEFAULT_DARK_THEME.className,
            var lightName: String? = DEFAULT_LIGHT_THEME.name,
            var lightClassName: String? = DEFAULT_LIGHT_THEME.className
    )

    companion object {
        val DEFAULT_DARK_THEME: UIManager.LookAndFeelInfo = DarculaLookAndFeelInfo()
        val DEFAULT_LIGHT_THEME: UIManager.LookAndFeelInfo = IntelliJLookAndFeelInfo()
    }
}