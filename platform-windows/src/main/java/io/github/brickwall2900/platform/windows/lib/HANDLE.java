package io.github.brickwall2900.platform.windows.lib;

import java.lang.foreign.MemorySegment;

/// Java-friendly representation of the HANDLE datatype in Win32
public record HANDLE(MemorySegment segment) implements AutoCloseable {
    /// Closes this HANDLE via {@link Kernel32#CloseHandle(MemorySegment)}
    @Override
    public void close() throws Exception {
        Kernel32.CloseHandle(segment);
    }
}
