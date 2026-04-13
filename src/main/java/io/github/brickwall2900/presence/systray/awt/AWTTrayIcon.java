package io.github.brickwall2900.presence.systray.awt;

import io.github.brickwall2900.presence.systray.TrayIcon;
import io.github.brickwall2900.presence.systray.TrayMenu;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class AWTTrayIcon implements TrayIcon {
    private java.awt.TrayIcon trayIcon;

    public AWTTrayIcon() {
        trayIcon = new java.awt.TrayIcon(
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration()
                        .createCompatibleImage(1, 1));
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setImage(Image image) {
        trayIcon.setImageAutoSize(true);
        trayIcon.setImage(image);
    }

    @Override
    public void setTooltip(String tooltip) {
        trayIcon.setToolTip(tooltip);
    }

    @Override
    public void setTrayMenu(TrayMenu menu) {
        trayIcon.setPopupMenu(((AWTTrayMenu) menu).menu);
    }

    @Override
    public void destroy() {
        SystemTray.getSystemTray().remove(trayIcon);
        trayIcon = null;
    }
}
