package io.github.brickwall2900.platform.windows.callbacks;

import java.lang.foreign.MemorySegment;

@FunctionalInterface
public interface EnumWindowProc {
    boolean callback(MemorySegment hWnd, long lParam);
}
