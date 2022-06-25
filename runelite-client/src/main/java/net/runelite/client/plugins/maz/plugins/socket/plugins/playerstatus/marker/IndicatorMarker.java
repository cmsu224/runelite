/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.playerstatus.marker;

import net.runelite.client.plugins.maz.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;

public class IndicatorMarker
extends AbstractMarker {
    private GameIndicator indicator;

    public IndicatorMarker(GameIndicator indicator) {
        this.indicator = indicator;
    }

    public GameIndicator getIndicator() {
        return this.indicator;
    }
}

