package io.github.brickwall2900.presence.systray.awt;

import io.github.brickwall2900.presence.systray.TrayMenu;
import io.github.brickwall2900.presence.systray.TrayMenuItem;

import java.awt.*;

public class AWTTrayMenu implements TrayMenu {
    final PopupMenu menu = new PopupMenu();

    @Override
    public void add(TrayMenuItem item) {
        menu.add(((AWTTrayMenuItem) item).menuItem);
    }

    @Override
    public void remove(TrayMenuItem item) {
        menu.remove(((AWTTrayMenuItem) item).menuItem);
    }

    @Override
    public void addSeparator() {
        menu.addSeparator();
    }
}
