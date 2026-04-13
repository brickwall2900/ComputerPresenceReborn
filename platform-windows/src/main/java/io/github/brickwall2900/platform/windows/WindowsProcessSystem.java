package io.github.brickwall2900.platform.windows;

import io.github.brickwall2900.presence.platform.systems.ProcessSystem;

public final class WindowsProcessSystem implements ProcessSystem {
    @Override
    public ProcessHandle[] getRunningProcesses() {
        // life is too short to call the Win32 API
        return ProcessHandle.allProcesses().toArray(ProcessHandle[]::new);
    }
}
