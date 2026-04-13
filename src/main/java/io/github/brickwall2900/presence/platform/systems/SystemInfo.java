package io.github.brickwall2900.presence.platform.systems;

import java.time.Duration;
import java.time.Instant;

public interface SystemInfo {
    Duration getSystemUptime();
    Instant getSystemStartTime();

    Duration getIdleTime();
}
