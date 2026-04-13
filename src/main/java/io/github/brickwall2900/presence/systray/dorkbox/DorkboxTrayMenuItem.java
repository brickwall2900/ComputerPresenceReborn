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

import io.github.brickwall2900.presence.systray.TrayMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DorkboxTrayMenuItem implements TrayMenuItem {
    private final List<Runnable> callbacks = new ArrayList<>();
    protected final JMenuItem menuItem;

    public DorkboxTrayMenuItem(JMenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItem.addActionListener(this::onActionPerformed);
    }

    public DorkboxTrayMenuItem() {
        this(new JMenuItem());
    }

    private void onActionPerformed(ActionEvent e) {
        callbacks.forEach(Runnable::run);
    }

    @Override
    public String getText() {
        return menuItem.getText();
    }

    @Override
    public void setText(String text) {
        menuItem.setText(text);
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
