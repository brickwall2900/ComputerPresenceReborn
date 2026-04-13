package io.github.brickwall2900.platform.windows;

import io.github.brickwall2900.presence.platform.Platform;
import io.github.brickwall2900.presence.platform.PlatformProvider;

public final class WindowsProvider implements PlatformProvider {
    private static WindowsPlatform platform;

    @Override
    public boolean supportsCurrentEnvironment() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    @Override
    public Platform createPlatform() {
        if (platform == null) {
            return platform = new WindowsPlatform();
        }
        return platform;
    }
}
