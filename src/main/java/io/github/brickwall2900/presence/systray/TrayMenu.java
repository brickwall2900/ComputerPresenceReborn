package io.github.brickwall2900.presence.systray;

public interface TrayMenu {
    void add(TrayMenuItem item);
    void remove(TrayMenuItem item);

    void addSeparator();
}
