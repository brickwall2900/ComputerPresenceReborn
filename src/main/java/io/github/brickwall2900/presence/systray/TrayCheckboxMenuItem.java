package io.github.brickwall2900.presence.systray;

public interface TrayCheckboxMenuItem extends TrayMenuItem {
    boolean isChecked();
    void setChecked(boolean state);
}
