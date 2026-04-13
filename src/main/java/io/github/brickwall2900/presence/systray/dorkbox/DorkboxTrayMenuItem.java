package io.github.brickwall2900.presence.systray.dorkbox;

import io.github.brickwall2900.presence.systray.TrayMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DorkboxTrayMenuItem implements TrayMenuItem {
    private final List<Runnable> callbacks = new ArrayList<>();
    protected final JMenuItem menuItem;

    public DorkboxTrayMenuItem(JMenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItem.addActionListener(this::onActionPerformed);
    }

    public DorkboxTrayMenuItem() {
        this(new JMenuItem());
    }

    private void onActionPerformed(ActionEvent e) {
        callbacks.forEach(Runnable::run);
    }

    @Override
    public String getText() {
        return menuItem.getText();
    }

    @Override
    public void setText(String text) {
        menuItem.setText(text);
    }

    @Override
    public void addCallback(Runnable onSelected) {
        callbacks.add(onSelected);
    }

    @Override
    public void removeCallback(Runnable onSelected) {
        callbacks.remove(onSelected);
    }
}
