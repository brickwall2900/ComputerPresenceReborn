package io.github.brickwall2900.presence.platform;

import io.github.brickwall2900.presence.platform.data.Dimension;
import io.github.brickwall2900.presence.platform.data.Point;
import io.github.brickwall2900.presence.platform.systems.InteractionSystem;
import io.github.brickwall2900.presence.platform.systems.ProcessSystem;
import io.github.brickwall2900.presence.platform.systems.SystemInfo;
import io.github.brickwall2900.presence.platform.systems.WindowSystem;

import java.time.Duration;
import java.time.Instant;

public interface Platform {
    SystemInfo systemInfo();
    WindowSystem windowSystem();
    ProcessSystem processes();
    InteractionSystem interaction();
}
