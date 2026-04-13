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

package io.github.brickwall2900.platform.windows;

import io.github.brickwall2900.presence.platform.data.Dimension;
import io.github.brickwall2900.presence.platform.data.Point;
import io.github.brickwall2900.presence.platform.systems.InteractionSystem;

import java.awt.*;

public final class WindowsInteractionSystem implements InteractionSystem {
    @Override
    public boolean hasMouseConnected() {
        // HAHAHAAHAHAHAHAHAHA
        return MouseInfo.getPointerInfo() != null;
    }

    @Override
    public Point getMouseLocation() {
        java.awt.Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return new Point(awtPoint.x, awtPoint.y);
    }

    @Override
    public int getNumberOfMonitors() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
    }

    @Override
    public Dimension getScreenSize(int monitor) {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice device = devices[monitor];
        DisplayMode displayMode = device.getDisplayMode();
        return new Dimension(displayMode.getWidth(), displayMode.getHeight());
    }
}
