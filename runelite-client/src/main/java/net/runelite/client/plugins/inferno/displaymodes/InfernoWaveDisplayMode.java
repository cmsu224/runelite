//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.inferno.displaymodes;

public enum InfernoWaveDisplayMode {
    CURRENT("Current wave"),
    NEXT("Next wave"),
    BOTH("Both"),
    NONE("None");

    private final String name;

    public String toString() {
        return this.name;
    }

    private InfernoWaveDisplayMode(String name) {
        this.name = name;
    }
}
