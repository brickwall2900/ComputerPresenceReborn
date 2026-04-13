package io.github.brickwall2900.presence.systray;

public interface TrayMenuItem {
    String getText();
    void setText(String text);

    void addCallback(Runnable onSelected);
    void removeCallback(Runnable onSelected);
}
