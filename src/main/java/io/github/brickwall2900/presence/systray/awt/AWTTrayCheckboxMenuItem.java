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
