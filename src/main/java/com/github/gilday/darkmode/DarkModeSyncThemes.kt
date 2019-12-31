package com.github.gilday.darkmode

import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo
import javax.swing.UIManager


class DarkModeSyncThemes {
    var dark: UIManager.LookAndFeelInfo = DarculaLookAndFeelInfo()
    var light: UIManager.LookAndFeelInfo = IntelliJLookAndFeelInfo()
}