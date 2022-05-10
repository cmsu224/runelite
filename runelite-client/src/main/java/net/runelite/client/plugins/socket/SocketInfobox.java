/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.infobox.InfoBox
 *  net.runelite.client.ui.overlay.infobox.InfoBoxPriority
 *  net.runelite.client.util.ColorUtil
 */
package net.runelite.client.plugins.socket;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.SocketConfig;
import net.runelite.client.plugins.socket.SocketPlugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;
import net.runelite.client.util.ColorUtil;

public class SocketInfobox
extends InfoBox {
    private String status;
    private final SocketConfig config;
    private final SocketPlugin plugin;

    SocketInfobox(BufferedImage image, SocketConfig config, SocketPlugin plugin, String status) {
        super(image, (Plugin)plugin);
        this.config = config;
        this.plugin = plugin;
        this.status = status;
        this.setPriority(InfoBoxPriority.HIGH);
    }

    public String getText() {
        return "";
    }

    public Color getTextColor() {
        Color color = Color.WHITE;
        switch (this.plugin.connection.getState()) {
            case CONNECTED: {
                color = Color.GREEN;
                break;
            }
            case CONNECTING: {
                color = Color.YELLOW;
                break;
            }
            case TERMINATED: 
            case DISCONNECTED: {
                color = Color.RED;
            }
        }
        return color;
    }

    public String getTooltip() {
        Color color = this.getTextColor();
        switch (this.plugin.connection.getState()) {
            case CONNECTED: {
                this.status = ColorUtil.wrapWithColorTag((String)"Connected", (Color)color);
                break;
            }
            case CONNECTING: {
                this.status = ColorUtil.wrapWithColorTag((String)"Connecting...", (Color)color);
                break;
            }
            case TERMINATED: 
            case DISCONNECTED: {
                this.status = ColorUtil.wrapWithColorTag((String)"Disconnected", (Color)color);
            }
        }
        return this.status;
    }

    public boolean render() {
        return this.config.infobox();
    }

    public String getStatus() {
        return this.status;
    }

    public SocketConfig getConfig() {
        return this.config;
    }

    public SocketPlugin getPlugin() {
        return this.plugin;
    }
}

