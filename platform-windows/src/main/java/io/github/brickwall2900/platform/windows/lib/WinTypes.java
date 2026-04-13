package io.github.brickwall2900.platform.windows.lib;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;

public final class WinTypes {
    public static final AddressLayout CALLBACK = ValueLayout.ADDRESS;

    public static final AddressLayout HANDLE = ValueLayout.ADDRESS;
    public static final AddressLayout HWND = ValueLayout.ADDRESS;
    public static final AddressLayout LPWSTR = ValueLayout.ADDRESS;
    public static final AddressLayout LPRECT = ValueLayout.ADDRESS;
    public static final ValueLayout.OfInt DWORD = ValueLayout.JAVA_INT;

    public static final FunctionDescriptor EnumWindowsProc = FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN,
            HWND,
            ValueLayout.JAVA_LONG);
}