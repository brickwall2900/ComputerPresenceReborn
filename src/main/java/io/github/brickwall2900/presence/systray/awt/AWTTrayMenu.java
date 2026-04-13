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

import io.github.brickwall2900.presence.systray.TrayMenu;
import io.github.brickwall2900.presence.systray.TrayMenuItem;

import java.awt.*;

public class AWTTrayMenu implements TrayMenu {
    final PopupMenu menu = new PopupMenu();

    @Override
    public void add(TrayMenuItem item) {
        menu.add(((AWTTrayMenuItem) item).menuItem);
    }

    @Override
    public void remove(TrayMenuItem item) {
        menu.remove(((AWTTrayMenuItem) item).menuItem);
    }

    @Override
    public void addSeparator() {
        menu.addSeparator();
    }
}
