/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import java.awt.image.BufferedImage;

public class SpecialIcon {
    private BufferedImage image;
    private String text;
    private long startTime;

    public SpecialIcon(BufferedImage image, String text, long startTime) {
        this.image = image;
        this.text = text;
        this.startTime = startTime;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public String getText() {
        return this.text;
    }

    public long getStartTime() {
        return this.startTime;
    }
}

