package io.github.brickwall2900.platform.windows;

import io.github.brickwall2900.presence.platform.Platform;
import io.github.brickwall2900.presence.platform.systems.InteractionSystem;
import io.github.brickwall2900.presence.platform.systems.ProcessSystem;
import io.github.brickwall2900.presence.platform.systems.SystemInfo;
import io.github.brickwall2900.presence.platform.systems.WindowSystem;

public final class WindowsPlatform implements Platform {
    private final WindowsSystemInfo systemInfo;
    private final WindowsWindowingSystem windowSystem;
    private final WindowsProcessSystem processSystem;
    private final WindowsInteractionSystem interactionSystem;

    public WindowsPlatform() {
        systemInfo = new WindowsSystemInfo();
        windowSystem = new WindowsWindowingSystem();
        processSystem = new WindowsProcessSystem();
        interactionSystem = new WindowsInteractionSystem();
    }

    @Override
    public SystemInfo systemInfo() {
        return systemInfo;
    }

    @Override
    public WindowSystem windowSystem() {
        return windowSystem;
    }

    @Override
    public ProcessSystem processes() {
        return processSystem;
    }

    @Override
    public InteractionSystem interaction() {
        return interactionSystem;
    }
}
