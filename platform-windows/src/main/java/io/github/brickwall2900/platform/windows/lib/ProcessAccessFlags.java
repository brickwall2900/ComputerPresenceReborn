package io.github.brickwall2900.platform.windows.lib;

public class ProcessAccessFlags extends StandardAccessFlags {
    public static final int PROCESS_ALL_RIGHTS = 0x000F0000 | SYNCHRONIZE | 0xFFFF;
    public static final int PROCESS_CREATE_PROCESS = 0x0080;
    public static final int PROCESS_CREATE_THREAD = 0x0002;
    public static final int PROCESS_DUP_HANDLE = 0x0040;
    public static final int PROCESS_QUERY_INFORMATION = 0x0400;
    public static final int PROCESS_QUERY_LIMITED_INFORMATION = 0x1000;
    public static final int PROCESS_SET_INFORMATION = 0x0200;
    public static final int PROCESS_SET_QUOTA = 0x0100;
    public static final int PROCESS_SUSPEND_RESUME = 0x0800;
    public static final int PROCESS_TERMINATE = 0x0001;
    public static final int PROCESS_VM_OPERATION = 0x0008;
    public static final int PROCESS_VM_READ = 0x0010;
    public static final int PROCESS_VM_WRITE = 0x0020;
}
