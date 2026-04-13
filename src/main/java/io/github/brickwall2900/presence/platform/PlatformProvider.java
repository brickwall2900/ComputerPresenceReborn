package io.github.brickwall2900.presence.platform;

import java.util.ServiceLoader;

public interface PlatformProvider {
    static Platform getInstance() {
        ServiceLoader<PlatformProvider> serviceLoader = ServiceLoader.load(PlatformProvider.class);
        for (PlatformProvider platform : serviceLoader) {
            if (platform.supportsCurrentEnvironment()) {
                return platform.createPlatform();
            }
        }

        throw new IllegalStateException("No platform is supported!");
    }

    boolean supportsCurrentEnvironment();
    Platform createPlatform();
}
