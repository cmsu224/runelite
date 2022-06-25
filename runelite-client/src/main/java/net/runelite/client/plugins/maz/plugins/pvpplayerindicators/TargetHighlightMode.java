//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

public enum TargetHighlightMode {
    OFF("Off"),
    HULL("Hull"),
    TILE("Tile"),
    TRUE_LOCATION("True Location");

    private final String name;

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    private TargetHighlightMode(String name) {
        this.name = name;
    }
}
