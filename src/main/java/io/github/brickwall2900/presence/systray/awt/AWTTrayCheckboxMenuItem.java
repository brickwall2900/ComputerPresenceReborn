package io.github.brickwall2900.presence.systray.awt;

import io.github.brickwall2900.presence.systray.TrayCheckboxMenuItem;

import java.awt.*;
import java.awt.event.ItemEvent;

public class AWTTrayCheckboxMenuItem extends AWTTrayMenuItem implements TrayCheckboxMenuItem {
    public AWTTrayCheckboxMenuItem() {
        CheckboxMenuItem c = new CheckboxMenuItem();
        super(c);

        c.addItemListener(this::onItemChecked);
    }

    private void onItemChecked(ItemEvent e) {
        fireCallbacks();
    }

    @Override
    public boolean isChecked() {
        return ((CheckboxMenuItem) menuItem).getState();
    }

    @Override
    public void setChecked(boolean state) {
        ((CheckboxMenuItem) menuItem).setState(state);
    }
}
