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

import io.github.brickwall2900.platform.windows.lib.*;
import io.github.brickwall2900.presence.platform.Window;
import io.github.brickwall2900.presence.platform.data.Rectangle;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public final class WindowsWindow implements Window {
    private final MemorySegment hwnd;

    public WindowsWindow(MemorySegment hwnd) {
        this.hwnd = hwnd;
    }

    @Override
    public String getTitle() {
        return User32.GetWindowTextW(hwnd);
    }

    @Override
    public String getInternalName() {
        return User32.GetClassNameW(hwnd);
    }

    @Override
    public Rectangle getBounds() {
        int x, y, width, height;
        try (Arena arena = Arena.ofConfined()) {
            WinStructs.RECT rect = new WinStructs.RECT(arena);
            if (User32.GetWindowRect(hwnd, rect)) {
                x = rect.left();
                y = rect.top();
                width = rect.right() - rect.left();
                height = rect.bottom() - rect.top();
            } else {
                throw new Win32Exception(Kernel32.GetLastError());
            }
        }
        return new Rectangle(x, y, width, height);
    }

    @Override
    public ProcessHandle getProcess() {
        int[] processIdOut = new int[1];
        int threadId = User32.GetWindowThreadProcessId(hwnd, processIdOut);
        int processId = processIdOut[0];

        return ProcessHandle.of(processId).orElseThrow(() -> new IllegalStateException("Process not accessible"));
    }
}
