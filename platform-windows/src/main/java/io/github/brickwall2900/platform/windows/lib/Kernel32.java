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

package io.github.brickwall2900.platform.windows.lib;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;

public final class Kernel32 {
    public static final Linker LINKER = Linker.nativeLinker();
    public static final SymbolLookup KERNEL32 = SymbolLookup.libraryLookup("kernel32.dll", Arena.global());

    public static final MethodHandle _GetTickCount64 = LINKER.downcallHandle(
            KERNEL32.findOrThrow("GetTickCount64"),
            FunctionDescriptor.of(ValueLayout.JAVA_LONG));

    public static final MethodHandle _GetLastError = LINKER.downcallHandle(
            KERNEL32.findOrThrow("GetLastError"),
            FunctionDescriptor.of(ValueLayout.JAVA_INT));

    public static final MethodHandle _OpenProcess = LINKER.downcallHandle(
            KERNEL32.findOrThrow("OpenProcess"),
            FunctionDescriptor.of(WinTypes.HANDLE, ValueLayout.JAVA_INT, ValueLayout.JAVA_BOOLEAN, ValueLayout.JAVA_INT));

    public static final MethodHandle _CloseHandle = LINKER.downcallHandle(
            KERNEL32.findOrThrow("CloseHandle"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, WinTypes.HANDLE));

    public static final MethodHandle _QueryFullProcessImageNameW = LINKER.downcallHandle(
            KERNEL32.findOrThrow("QueryFullProcessImageNameW"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN,
                    WinTypes.HANDLE,
                    ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

//    public static final MethodHandle _FormatMessage = LINKER.downcallHandle(
//            KERNEL32.findOrThrow("FormatMessage"),
//            FunctionDescriptor.of(ValueLayout.JAVA_INT,
//                    ValueLayout.JAVA_INT,
//                    ValueLayout.ADDRESS,
//                    ValueLayout.JAVA_INT,
//                    ValueLayout.JAVA_INT,
//                    ValueLayout.ADDRESS,
//                    ValueLayout.JAVA_INT,
//                    ValueLayout.ADDRESS
//            )
//    );

    public static int GetLastError() {
        try {
            return (int) _GetLastError.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long GetTickCount64() {
        try {
            return (long) _GetTickCount64.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId) {
        try {
            return (MemorySegment) _OpenProcess.invokeExact(dwDesiredAccess, bInheritHandle, dwProcessId);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean CloseHandle(MemorySegment handle) {
        try {
            return (boolean) _CloseHandle.invokeExact(handle);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /// Java-friendly version of {@link Kernel32#CloseHandle(MemorySegment)}
    public static boolean CloseHandle(HANDLE handle) {
        return CloseHandle(handle.segment());
    }

    public static String QueryFullProcessImageNameW(MemorySegment hProcess, int dwFlags) {
        try (Arena arena = Arena.ofConfined()) {
            int initialSize = 256 + 1;
            MemorySegment lpdwSize = arena.allocateFrom(ValueLayout.JAVA_INT, initialSize);
            MemorySegment lpExeName = arena.allocate(initialSize * 2);
            boolean successful = (boolean) _QueryFullProcessImageNameW.invokeExact(hProcess, dwFlags, lpExeName, lpdwSize);

            int error = GetLastError();
            if (error != 0) {
                throw new Win32Exception(error);
            }

            int copied = lpdwSize.get(ValueLayout.JAVA_INT, 0);
            if (copied == 0) {
                return "";
            }

            return lpExeName.getString(0, StandardCharsets.UTF_16LE);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    /*
    WCHAR   wszMsgBuff[512];  // Buffer for text.

    DWORD   dwChars;  // Number of chars returned.

    // Try to get the message from the system errors.
    dwChars = FormatMessage( FORMAT_MESSAGE_FROM_SYSTEM |
                             FORMAT_MESSAGE_IGNORE_INSERTS,
                             NULL,
                             dwErr,
                             0,
                             wszMsgBuff,
                             512,
                             NULL );
     */
//    public static int FormatMessage() {
//
//    }
}
