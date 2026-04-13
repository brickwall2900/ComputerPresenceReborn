package io.github.brickwall2900.presence.systray.dorkbox;

import io.github.brickwall2900.presence.systray.TrayMenu;
import io.github.brickwall2900.presence.systray.TrayMenuItem;

import javax.swing.*;

public class DorkboxTrayMenu implements TrayMenu {
    final JMenu menu;

    public DorkboxTrayMenu() {
        menu = new JMenu();
    }

    @Override
    public void add(TrayMenuItem item) {
        menu.add(((DorkboxTrayMenuItem) item).menuItem);
    }

    @Override
    public void remove(TrayMenuItem item) {
        menu.remove(((DorkboxTrayMenuItem) item).menuItem);
    }

    @Override
    public void addSeparator() {
        menu.addSeparator();
    }
}
