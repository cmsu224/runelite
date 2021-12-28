//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public class PurpleAttackInfobox extends InfoBox {
    private String out;
    private int real;

    public PurpleAttackInfobox(BufferedImage image, @Nonnull Plugin plugin) {
        super(image, plugin);
    }

    public String getText() {
        return this.out;
    }

    public Color getTextColor() {
        return this.real >= 20 ? Color.red : Color.white;
    }

    public String getTooltip() {
        return "Attacks after crabs spawn" + (this.out.contains("*") ? " (Scuffed)" : "");
    }

    public void set(int real, String out) {
        this.out = out;
        this.real = real;
    }
}
