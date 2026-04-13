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
