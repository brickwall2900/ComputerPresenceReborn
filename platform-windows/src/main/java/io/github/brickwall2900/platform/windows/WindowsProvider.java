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
