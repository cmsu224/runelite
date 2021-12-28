//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta.stomp.def;

public enum BloatRotation {
    CLOCKWISE,
    COUNTER_CLOCKWISE,
    UNKNOWN;

    private BloatRotation() {
    }

    public boolean isClockwise() {
        return this == CLOCKWISE;
    }
}
