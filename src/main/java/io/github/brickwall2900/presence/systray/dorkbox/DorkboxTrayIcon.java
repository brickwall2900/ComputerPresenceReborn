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
