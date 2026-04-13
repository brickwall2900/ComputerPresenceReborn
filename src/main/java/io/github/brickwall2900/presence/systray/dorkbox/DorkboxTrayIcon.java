package io.github.brickwall2900.presence.systray.dorkbox;

import dorkbox.systemTray.SystemTray;
import io.github.brickwall2900.presence.systray.TrayIcon;
import io.github.brickwall2900.presence.systray.TrayMenu;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class DorkboxTrayIcon implements TrayIcon {
    private final SystemTray systemTray;

    public DorkboxTrayIcon(String appId) {
        systemTray = SystemTray.get(appId);
    }

    @Override
    public void setImage(Image image) {
        systemTray.setImage(image);
    }

    @Override
    public void setTooltip(String tooltip) {
        systemTray.setTooltip(tooltip);
    }

    @Override
    public void setTrayMenu(TrayMenu menu) {
        systemTray.setMenu(((DorkboxTrayMenu) menu).menu);
    }

    @Override
    public void destroy() {
        systemTray.shutdown();
    }
}
