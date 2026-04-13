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

import io.github.brickwall2900.presence.systray.TrayMenuItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class AWTTrayMenuItem implements TrayMenuItem {
    private final List<Runnable> callbacks = new ArrayList<>();
    protected final MenuItem menuItem;

    public AWTTrayMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItem.addActionListener(this::onActionPerformed);
    }

    public AWTTrayMenuItem() {
        this(new MenuItem());
    }

    protected void fireCallbacks() {
        callbacks.forEach(Runnable::run);
    }

    protected void onActionPerformed(ActionEvent e) {
        fireCallbacks();
    }

    @Override
    public String getText() {
        return menuItem.getLabel();
    }

    @Override
    public void setText(String text) {
        menuItem.setLabel(text);
    }

    @Override
    public void addCallback(Runnable onSelected) {
        callbacks.add(onSelected);
    }

    @Override
    public void removeCallback(Runnable onSelected) {
        callbacks.remove(onSelected);
    }
}
