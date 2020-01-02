package com.github.gilday.darkmode

import com.intellij.ide.ui.LafManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.panel
import javax.swing.UIManager.LookAndFeelInfo

class DarkModeConfigurable(private val lafManager: LafManager) : BoundConfigurable("Dark Mode Sync") {

    override fun createPanel(): DialogPanel {
        val options = ServiceManager.getService(DarkModeSyncThemes::class.java)
        val lafs = lafManager.installedLookAndFeels.asList()
        val renderer = SimpleListCellRenderer.create("") { obj: LookAndFeelInfo -> obj.name }
        return panel {
            row("Light Theme:") {
                comboBox(CollectionComboBoxModel(lafs), options::light, renderer = renderer)
            }
            row("Dark Theme:") {
                comboBox(CollectionComboBoxModel(lafs), options::dark, renderer = renderer)
            }
        }
    }
}