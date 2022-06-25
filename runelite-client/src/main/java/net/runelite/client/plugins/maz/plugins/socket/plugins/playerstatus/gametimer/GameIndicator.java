/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.playerstatus.gametimer;

import java.awt.Color;

public enum GameIndicator {
    VENGEANCE_ACTIVE(561, GameTimerImageType.SPRITE, "Vengeance active"),
    SPEC_XFER(558, GameTimerImageType.SPRITE, "Energy transfer available");

    private final String description;
    private String text;
    private Color textColor;
    private final int imageId;
    private final GameTimerImageType imageType;

    private GameIndicator(int imageId, GameTimerImageType idType, String description, String text, Color textColor) {
        this.imageId = imageId;
        this.imageType = idType;
        this.description = description;
        this.text = text;
        this.textColor = textColor;
    }

    private GameIndicator(int imageId, GameTimerImageType idType, String description) {
        this(imageId, idType, description, "", null);
    }

    public String getDescription() {
        return this.description;
    }

    public String getText() {
        return this.text;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public int getImageId() {
        return this.imageId;
    }

    public GameTimerImageType getImageType() {
        return this.imageType;
    }
}

