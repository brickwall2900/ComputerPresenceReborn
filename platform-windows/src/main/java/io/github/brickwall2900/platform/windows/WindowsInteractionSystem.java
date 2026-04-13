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
