package io.github.brickwall2900.platform.windows.lib;

public class Win32Exception extends RuntimeException {
    private final int code;

    public Win32Exception(int code) {
        super("Windows failed with an error code of " + Integer.toHexString(code).toUpperCase());
        this.code = code;
    }
}
