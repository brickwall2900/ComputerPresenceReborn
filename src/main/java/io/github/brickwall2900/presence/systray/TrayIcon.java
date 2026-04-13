package io.github.brickwall2900.presence.systray;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface TrayIcon {
    void setImage(Image image);
    void setTooltip(String tooltip);
    void setTrayMenu(TrayMenu menu);

    void destroy();
}
