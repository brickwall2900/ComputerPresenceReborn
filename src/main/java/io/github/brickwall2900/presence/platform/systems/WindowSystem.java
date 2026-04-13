package io.github.brickwall2900.presence.platform.systems;

import io.github.brickwall2900.presence.platform.Window;

public interface WindowSystem {
    Window[] getAllWindows();
    Window[] getOpenWindows();
    Window getFocusedWindow();
}
