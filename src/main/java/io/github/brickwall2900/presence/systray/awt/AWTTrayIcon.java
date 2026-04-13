/*
 * Copyright 2026 brickwall2900
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
