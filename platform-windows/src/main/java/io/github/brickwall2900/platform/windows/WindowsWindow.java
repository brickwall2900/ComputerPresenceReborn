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
