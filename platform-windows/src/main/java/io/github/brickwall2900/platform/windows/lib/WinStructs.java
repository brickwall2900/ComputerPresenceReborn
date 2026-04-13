package io.github.brickwall2900.platform.windows.lib;

import java.lang.foreign.*;
import java.lang.invoke.VarHandle;

public final class WinStructs {
    public record RECT(MemorySegment segment) {
        public static final GroupLayout LAYOUT = MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("left"),
                ValueLayout.JAVA_INT.withName("top"),
                ValueLayout.JAVA_INT.withName("right"),
                ValueLayout.JAVA_INT.withName("bottom")
        );

        public static final VarHandle LEFT =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("left"));

        public static final VarHandle TOP =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("top"));

        public static final VarHandle RIGHT =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("right"));

        public static final VarHandle BOTTOM =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("bottom"));

        public RECT(Arena segment) {
            this(segment.allocate(LAYOUT));
        }

        public int left() {
            return (int) LEFT.get(segment, 0L);
        }

        public int top() {
            return (int) TOP.get(segment, 0L);
        }

        public int right() {
            return (int) RIGHT.get(segment, 0L);
        }

        public int bottom() {
            return (int) BOTTOM.get(segment, 0L);
        }

        public void left(int left) {
            LEFT.set(segment, 0L, left);
        }

        public void top(int top) {
            TOP.set(segment, 0L, top);
        }

        public void right(int right) {
            RIGHT.set(segment, 0L, right);
        }

        public void bottom(int bottom) {
            BOTTOM.set(segment, 0L, bottom);
        }
    }

    public record LASTINPUTINFO(MemorySegment segment) {
        public static final GroupLayout LAYOUT = MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("cbSize"),
                ValueLayout.JAVA_INT.withName("dwTime")
        );

        public static final VarHandle SIZE =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cbSize"));

        public static final VarHandle TIME =
                LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dwTime"));

        public LASTINPUTINFO(Arena segment) {
            this(segment.allocate(LAYOUT));
        }

        public int size() {
            return (int) SIZE.get(segment, 0L);
        }

        public int time() {
            return (int) TIME.get(segment, 0L);
        }

        public void size(int size) {
            SIZE.set(segment, 0L, size);
        }

        public void time(int time) {
            TIME.set(segment, 0L, time);
        }
    }
}
