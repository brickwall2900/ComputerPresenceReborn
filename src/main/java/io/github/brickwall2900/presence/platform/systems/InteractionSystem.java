package io.github.brickwall2900.presence.platform.systems;

import io.github.brickwall2900.presence.platform.data.Dimension;
import io.github.brickwall2900.presence.platform.data.Point;

public interface InteractionSystem {
    boolean hasMouseConnected();
    Point getMouseLocation();

    int getNumberOfMonitors();

    Dimension getScreenSize(int monitor);
}
