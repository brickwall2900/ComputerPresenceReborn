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
