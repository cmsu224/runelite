//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus.marker;

import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;

public class IndicatorMarker extends AbstractMarker {
    private GameIndicator indicator;

    public IndicatorMarker(GameIndicator indicator) {
        this.indicator = indicator;
    }

    public GameIndicator getIndicator() {
        return this.indicator;
    }
}
