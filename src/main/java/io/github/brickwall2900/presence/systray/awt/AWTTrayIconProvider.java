package io.github.brickwall2900.presence.systray.awt;

import io.github.brickwall2900.presence.systray.*;

public final class AWTTrayIconProvider implements TrayIconProvider {
    @Override
    public TrayIcon create(String appId) {
        return new AWTTrayIcon();
    }

    @Override
    public TrayMenu createMenu() {
        return new AWTTrayMenu();
    }

    @Override
    public TrayMenuItem createMenuItem() {
        return new AWTTrayMenuItem();
    }

    @Override
    public TrayCheckboxMenuItem createCheckboxMenuItem() {
        return new AWTTrayCheckboxMenuItem();
    }
}
