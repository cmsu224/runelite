//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.inferno.displaymodes;

public enum InfernoZukShieldDisplayMode {
    OFF("Off"),
    LIVE("Live (follow shield)"),
    PREDICT("Predict"),
    LIVEPLUSPREDICT("Live and Predict");

    private final String name;

    private InfernoZukShieldDisplayMode(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    String getName() {
        return this.name;
    }
}
