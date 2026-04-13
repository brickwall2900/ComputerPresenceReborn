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

import io.github.brickwall2900.platform.windows.callbacks.EnumWindowProc;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

public final class User32 {
    public static final Linker LINKER = Linker.nativeLinker();
    public static final SymbolLookup USER32 = SymbolLookup.libraryLookup("user32.dll", Arena.global());

    public static final MethodHandle _GetForegroundWindow = LINKER.downcallHandle(
            USER32.findOrThrow("GetForegroundWindow"),
            FunctionDescriptor.of(ValueLayout.ADDRESS));

    public static final MethodHandle _GetWindowTextLengthW = LINKER.downcallHandle(
            USER32.findOrThrow("GetWindowTextLengthW"),
            FunctionDescriptor.of(ValueLayout.JAVA_INT, WinTypes.HWND));

    public static final MethodHandle _GetWindowTextW = LINKER.downcallHandle(
            USER32.findOrThrow("GetWindowTextW"),
            FunctionDescriptor.of(ValueLayout.JAVA_INT, WinTypes.HWND, WinTypes.LPWSTR, ValueLayout.JAVA_INT));

    public static final MethodHandle _GetClassNameW = LINKER.downcallHandle(
            USER32.findOrThrow("GetClassNameW"),
            FunctionDescriptor.of(ValueLayout.JAVA_INT, WinTypes.HWND, WinTypes.LPWSTR, ValueLayout.JAVA_INT));

    public static final MethodHandle _GetWindowRect = LINKER.downcallHandle(
            USER32.findOrThrow("GetWindowRect"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, WinTypes.HWND, WinTypes.LPRECT));

    public static final MethodHandle _EnumWindows = LINKER.downcallHandle(
            USER32.findOrThrow("EnumWindows"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, WinTypes.CALLBACK, ValueLayout.JAVA_LONG));

    public static final MethodHandle _GetWindowThreadProcessId = LINKER.downcallHandle(
            USER32.findOrThrow("GetWindowThreadProcessId"),
            FunctionDescriptor.of(ValueLayout.JAVA_INT, WinTypes.HWND, ValueLayout.ADDRESS /* int* */));

    public static final MethodHandle _IsWindowVisible = LINKER.downcallHandle(
            USER32.findOrThrow("IsWindowVisible"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, ValueLayout.ADDRESS));

    public static final MethodHandle _GetLastInputInfo = LINKER.downcallHandle(
            USER32.findOrThrow("GetLastInputInfo"),
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN, ValueLayout.ADDRESS));

    public static MemorySegment GetForegroundWindow() {
        try {
            return (MemorySegment) _GetForegroundWindow.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int GetWindowTextLengthW(MemorySegment hwnd) {
        try {
            return (int) _GetWindowTextLengthW.invokeExact(hwnd);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String GetWindowTextW(MemorySegment hwnd) {
        try (Arena arena = Arena.ofConfined()) {
            int length = GetWindowTextLengthW(hwnd);
            MemorySegment buffer = arena.allocate((length + 1) * 2L);

            int copied = (int) _GetWindowTextW.invokeExact(
                    hwnd,
                    buffer,
                    length + 1
            );

            if (copied == 0) return "";

            return buffer.getString(0, StandardCharsets.UTF_16LE);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String GetClassNameW(MemorySegment hwnd) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment buffer = arena.allocate((256 + 1) * 2L);

            int copied = (int) _GetClassNameW.invokeExact(
                    hwnd,
                    buffer,
                    256 + 1
            );

            if (copied == 0) return "";

            return buffer.getString(0, StandardCharsets.UTF_16LE);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean GetWindowRect(MemorySegment hwnd, WinStructs.RECT lpRect) {
        try {
            return (boolean) _GetWindowRect.invokeExact(hwnd, lpRect.segment());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean EnumWindows(MethodHandle callback, long lParam) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment callbackPtr = LINKER.upcallStub(
                    callback,
                    WinTypes.EnumWindowsProc,
                    arena
            );

            return (boolean) _EnumWindows.invokeExact(callbackPtr, lParam);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /// A Java-friendlier version of {@link User32#EnumWindows(MethodHandle, long)}
    public static boolean EnumWindows(EnumWindowProc callback, long lParam) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            MethodHandle handle = lookup.findVirtual(
                    EnumWindowProc.class,
                    "callback",
                    MethodType.methodType(boolean.class, MemorySegment.class, long.class)
            ).bindTo(callback);
            return EnumWindows(handle, lParam);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find matching function", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access callback method", e);
        }
    }

    public static int GetWindowThreadProcessId(MemorySegment hwnd, int[] lpdwProcessId) {
        if (lpdwProcessId.length != 1) {
            throw new ArrayStoreException("Invalid size for lpdwProcessId");
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment lpdwProcessIdSegment = arena.allocateFrom(ValueLayout.JAVA_INT, 0);
            int threadId = (int) _GetWindowThreadProcessId.invokeExact(hwnd, lpdwProcessIdSegment);
            lpdwProcessId[0] = lpdwProcessIdSegment.get(ValueLayout.JAVA_INT, 0);

            return threadId;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean IsWindowVisible(MemorySegment hwnd) {
        try {
            return (boolean) _IsWindowVisible.invokeExact(hwnd);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean GetLastInputInfo(MemorySegment lastInputInfo) {
        try {
            return (boolean) _GetLastInputInfo.invokeExact(lastInputInfo);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /// Java-friendlier version of {@link User32#GetLastInputInfo(MemorySegment)}
    public static boolean GetLastInputInfo(WinStructs.LASTINPUTINFO lastinputinfo) {
        return GetLastInputInfo(lastinputinfo.segment());
    }
}
