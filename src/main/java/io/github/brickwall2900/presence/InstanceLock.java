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

package io.github.brickwall2900.presence;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class InstanceLock {
    private final Path lockPath;
    private FileChannel fileChannel;
    private FileLock fileLock;
    private boolean isLocked;

    public InstanceLock(String appId) {
        lockPath = Paths.get(System.getProperty("java.io.tmpdir"), "InstanceLock_" + appId);
    }

    public boolean lock() throws LockOperationFailedException {
        try {
            fileChannel = FileChannel.open(lockPath, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            fileLock = fileChannel.tryLock();
            if (fileLock == null) {
                fileChannel.close();
                return false;
            }
        } catch (Exception e) {
            throw new LockOperationFailedException(LockOperationFailedException.LockState.LOCKING, e);
        }
        isLocked = true;
        return true;
    }

    public boolean unlock() throws LockOperationFailedException {
        try {
            if (fileLock != null) {
                fileLock.close();
            }
            fileChannel.close();
        } catch (Exception e) {
            throw new LockOperationFailedException(LockOperationFailedException.LockState.UNLOCKING, e);
        }
        isLocked = false;
        return true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public static class LockOperationFailedException extends IllegalStateException {
        public LockOperationFailedException(LockState state, Exception e) {
            super("Lock operation failed on " + state + " state", e);
        }

        public enum LockState {
            LOCKING, UNLOCKING;
        }
    }
}
