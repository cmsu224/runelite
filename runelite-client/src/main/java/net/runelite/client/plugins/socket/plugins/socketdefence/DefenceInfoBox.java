/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.infobox.InfoBox
 */
package net.runelite.client.plugins.socket.plugins.socketdefence;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketdefence.SocketDefenceConfig;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public class DefenceInfoBox
extends InfoBox {
    @Inject
    private final SocketDefenceConfig config;
    private long count;

    public DefenceInfoBox(BufferedImage image, Plugin plugin, long count, SocketDefenceConfig config) {
        super(image, plugin);
        this.count = count;
        this.config = config;
    }

    public String getText() {
        return Long.toString(this.getCount());
    }

    public Color getTextColor() {
        if (this.count == 0L) {
            return Color.GREEN;
        }
        if (this.count >= 1L && this.count <= (long)this.config.lowDef()) {
            return Color.YELLOW;
        }
        return Color.WHITE;
    }

    public String toString() {
        return "DefenceInfoBox(config=" + this.config + ", count=" + this.getCount() + ")";
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

