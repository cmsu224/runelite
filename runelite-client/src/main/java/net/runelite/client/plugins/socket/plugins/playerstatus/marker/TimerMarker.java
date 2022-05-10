/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.plugins.playerstatus.marker;

import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameTimer;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.AbstractMarker;

public class TimerMarker
extends AbstractMarker {
    private GameTimer timer;
    private long startTime;

    public TimerMarker(GameTimer timer, long startTime) {
        this.timer = timer;
        this.startTime = startTime;
    }

    public GameTimer getTimer() {
        return this.timer;
    }

    public long getStartTime() {
        return this.startTime;
    }
}

