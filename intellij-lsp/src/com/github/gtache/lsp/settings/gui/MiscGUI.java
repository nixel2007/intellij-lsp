package com.github.gtache.lsp.settings.gui;

import com.github.gtache.lsp.settings.LSPState;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class MiscGUI implements LSPGUI {
    private final LSPState state;
    private JLabel useInplaceRenameLabel;
    private JCheckBox useInplaceRenameCheckbox;
    private JPanel rootPanel;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    public MiscGUI() {
        state = LSPState.getInstance();
        useInplaceRenameCheckbox.setSelected(state.isUseInplaceRename());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return state.isUseInplaceRename() != useInplaceRenameCheckbox.isSelected();
    }

    @Override
    public void reset() {
        useInplaceRenameCheckbox.setSelected(state.isUseInplaceRename());
    }

    @Override
    public void apply() {
        state.setUseInplaceRename(useInplaceRenameCheckbox.isSelected());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        rootPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        useInplaceRenameLabel = new JLabel();
        useInplaceRenameLabel.setText("Use inplace rename (expect bugs, and type slowly)");
        rootPanel.add(useInplaceRenameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useInplaceRenameCheckbox = new JCheckBox();
        useInplaceRenameCheckbox.setText("");
        rootPanel.add(useInplaceRenameCheckbox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        rootPanel.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}