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

import io.github.brickwall2900.platform.windows.lib.Kernel32;
import io.github.brickwall2900.platform.windows.lib.User32;
import io.github.brickwall2900.platform.windows.lib.WinStructs;
import io.github.brickwall2900.presence.platform.systems.SystemInfo;

import java.lang.foreign.Arena;
import java.time.Duration;
import java.time.Instant;

public final class WindowsSystemInfo implements SystemInfo {
    @Override
    public Duration getSystemUptime() {
        return Duration.ofMillis(Kernel32.GetTickCount64());
    }

    @Override
    public Instant getSystemStartTime() {
        return Instant.ofEpochMilli(System.currentTimeMillis() - Kernel32.GetTickCount64());
    }

    @Override
    public Duration getIdleTime() {
        try (Arena arena = Arena.ofConfined()) {
            WinStructs.LASTINPUTINFO lastinputinfo = new WinStructs.LASTINPUTINFO(arena);
            lastinputinfo.size((int) lastinputinfo.segment().byteSize());
            lastinputinfo.time(0);

            if (User32.GetLastInputInfo(lastinputinfo)) {
                long millis = Kernel32.GetTickCount64() - Integer.toUnsignedLong(lastinputinfo.time());
                System.out.println(millis);
                return Duration.ofMillis(millis);
            } else {
                return Duration.ZERO;
            }
        }
    }
}
