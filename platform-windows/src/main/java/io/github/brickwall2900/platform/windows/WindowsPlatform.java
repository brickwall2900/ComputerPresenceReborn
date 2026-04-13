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
