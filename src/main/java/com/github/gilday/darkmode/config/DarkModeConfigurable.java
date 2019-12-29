package com.github.gilday.darkmode.config;

import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.SimpleListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.Nullable;

public class DarkModeConfigurable implements Configurable {

  private final LafManager lafManager;

  private JComboBox<LookAndFeelInfo> lightPicker;
  private JComboBox<LookAndFeelInfo> darkPicker;
  private JPanel panel;

  public DarkModeConfigurable(LafManager lafManager) {
    this.lafManager = lafManager;
  }

  @Nls(capitalization = Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Dark Mode Sync";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    final CollectionComboBoxModel<LookAndFeelInfo> model = lafManager.getLafComboBoxModel();
    final SimpleListCellRenderer<LookAndFeelInfo> renderer =
        SimpleListCellRenderer.create("", LookAndFeelInfo::getName);
    lightPicker.setModel(model);
    lightPicker.setRenderer(renderer);
    darkPicker.setModel(model);
    darkPicker.setRenderer(renderer);
    return panel;
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public void apply() throws ConfigurationException {}
}
