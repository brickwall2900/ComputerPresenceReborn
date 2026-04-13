package io.github.brickwall2900.presence.platform;

import io.github.brickwall2900.presence.platform.data.Rectangle;

public interface Window {
    String getTitle();
    String getInternalName();
    Rectangle getBounds();
    ProcessHandle getProcess();
}
