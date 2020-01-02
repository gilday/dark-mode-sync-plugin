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
    var dark: UIManager.LookAndFeelInfo = DarculaLookAndFeelInfo()

    @Volatile
    var light: UIManager.LookAndFeelInfo = IntelliJLookAndFeelInfo()

    override fun getState(): State? {
        return State(dark.className, light.className)
    }

    override fun loadState(state: State) {
        val lafManager = LafManager.getInstance()
        dark = lafManager.installedLookAndFeels.find { it.className == state.darkClass }
                ?: DarculaLookAndFeelInfo()
        light = lafManager.installedLookAndFeels.first { it.className == state.lightClass }
                ?: IntelliJLookAndFeelInfo()
    }

    class State(
            var darkClass: String?,
            var lightClass: String?
    )
}