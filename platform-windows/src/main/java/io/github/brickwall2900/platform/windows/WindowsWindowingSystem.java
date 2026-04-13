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
import io.github.brickwall2900.platform.windows.lib.Win32Exception;
import io.github.brickwall2900.platform.windows.lib.WinStructs;
import io.github.brickwall2900.presence.platform.Window;
import io.github.brickwall2900.presence.platform.systems.WindowSystem;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;

public final class WindowsWindowingSystem implements WindowSystem {
    @Override
    public Window[] getAllWindows() {
        List<WindowsWindow> windows = new ArrayList<>();
        User32.EnumWindows((hwnd, lParam) -> {
            windows.add(new WindowsWindow(hwnd));
            return true;
        }, 0);
        return windows.toArray(Window[]::new);
    }

    @Override
    public Window[] getOpenWindows() {
        /*
        if (USER32.IsWindowVisible(window)) {
			RECT bounds = new RECT();
			USER32.GetWindowRect(window, bounds);
			if (bounds.left > -32000) {
				char[] buffer = new char[1024];
				USER32.GetWindowText(window, buffer, buffer.length);
				String title = Native.toString(buffer);
				windowList.add(new WindowInfo(window, bounds, title));
			}
		}
		return true;
         */
        List<WindowsWindow> windows = new ArrayList<>();
        User32.EnumWindows((hwnd, lParam) -> {
            if (User32.IsWindowVisible(hwnd)) {
                try (Arena arena = Arena.ofConfined()) {
                    WinStructs.RECT rect = new WinStructs.RECT(arena);
                    if (User32.GetWindowRect(hwnd, rect)) {
                        if (rect.left() > -32000) {
                            windows.add(new WindowsWindow(hwnd));
                        }
                    } else {
                        throw new Win32Exception(Kernel32.GetLastError());
                    }
                }
            }
            return true;
        }, 0);
        return windows.toArray(Window[]::new);
    }

    @Override
    public Window getFocusedWindow() {
        MemorySegment focusedWindow = User32.GetForegroundWindow();
        return new WindowsWindow(focusedWindow);
    }
}
