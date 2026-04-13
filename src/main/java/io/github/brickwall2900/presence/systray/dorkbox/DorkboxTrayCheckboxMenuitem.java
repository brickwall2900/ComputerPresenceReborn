package io.github.brickwall2900.presence.systray.dorkbox;

import io.github.brickwall2900.presence.systray.TrayCheckboxMenuItem;

import javax.swing.*;

public class DorkboxTrayCheckboxMenuitem extends DorkboxTrayMenuItem implements TrayCheckboxMenuItem {

    public DorkboxTrayCheckboxMenuitem() {
        super(new JCheckBoxMenuItem());
    }

    @Override
    public boolean isChecked() {
        return menuItem.isSelected();
    }

    @Override
    public void setChecked(boolean state) {
        menuItem.setSelected(state);
    }
}
