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

import io.github.brickwall2900.presence.systray.*;

public final class DorkboxTrayIconProvider implements TrayIconProvider {
    public DorkboxTrayIconProvider() {
        // The SystemTray and its framework and libraries and whatnot has an auto-updater.
        // We disable it...
        System.setProperty("dorkbox.updates.Updates.ENABLE", "false");
        System.setProperty("dorkbox.Updates.ENABLE", "false");
    }

    @Override
    public TrayIcon create(String appId) {
        return new DorkboxTrayIcon(appId);
    }

    @Override
    public TrayMenu createMenu() {
        return new DorkboxTrayMenu();
    }

    @Override
    public TrayMenuItem createMenuItem() {
        return new DorkboxTrayMenuItem();
    }

    @Override
    public TrayCheckboxMenuItem createCheckboxMenuItem() {
        return new DorkboxTrayCheckboxMenuitem();
    }
}
