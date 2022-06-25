//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import java.awt.Color;

final class Decorations {
    private final int image;
    private final Color color;

    public Decorations(int image, Color color) {
        this.image = image;
        this.color = color;
    }

    public int getImage() {
        return this.image;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Decorations)) {
            return false;
        } else {
            Decorations other = (Decorations)o;
            if (this.getImage() != other.getImage()) {
                return false;
            } else {
                Object this$color = this.getColor();
                Object other$color = other.getColor();
                if (this$color == null) {
                    if (other$color != null) {
                        return false;
                    }
                } else if (!this$color.equals(other$color)) {
                    return false;
                }

                return true;
            }
        }
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getImage();
        Object $color = this.getColor();
        result = result * 59 + ($color == null ? 43 : $color.hashCode());
        return result;
    }

    public String toString() {
        int var10000 = this.getImage();
        return "PvPPlayerIndicatorsPlugin.Decorations(image=" + var10000 + ", color=" + this.getColor() + ")";
    }
}
