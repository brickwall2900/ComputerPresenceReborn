package io.github.brickwall2900.presence.systray;

public interface TrayIconProvider {
    TrayIcon create(String appId);
    TrayMenu createMenu();

    TrayMenuItem createMenuItem();
    TrayCheckboxMenuItem createCheckboxMenuItem();

    default TrayMenuItem createMenuItem(String text) {
        TrayMenuItem item = createMenuItem();
        item.setText(text);
        return item;
    }

    default TrayCheckboxMenuItem createCheckboxMenuItem(String text) {
        TrayCheckboxMenuItem item = createCheckboxMenuItem();
        item.setText(text);
        return item;
    }
}
