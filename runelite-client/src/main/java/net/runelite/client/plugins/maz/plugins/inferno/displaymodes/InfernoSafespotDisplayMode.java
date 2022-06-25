//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.inferno.displaymodes;

public enum InfernoSafespotDisplayMode {
    OFF("Off"),
    INDIVIDUAL_TILES("Individual tiles"),
    AREA("Area (lower fps)");

    private final String name;

    private InfernoSafespotDisplayMode(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    String getName() {
        return this.name;
    }
}
