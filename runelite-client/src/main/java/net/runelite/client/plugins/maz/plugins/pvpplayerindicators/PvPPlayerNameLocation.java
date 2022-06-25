//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

public enum PvPPlayerNameLocation {
    DISABLED("Disabled"),
    ABOVE_HEAD("Above head"),
    MODEL_CENTER("Center of model"),
    MODEL_RIGHT("Right of model");

    private final String name;

    public String toString() {
        return this.name;
    }

    private PvPPlayerNameLocation(String name) {
        this.name = name;
    }
}
