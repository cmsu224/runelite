//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.util;

import java.awt.Color;
import java.awt.Font;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.cerberus.CerberusConfig.InfoBoxComponentSize;
import net.runelite.client.plugins.cerberus.domain.Phase;
import net.runelite.client.ui.FontManager;

public final class Utility {
    private static final Color COLOR_DEFAULT = new Color(70, 61, 50, 225);
    private static final Color COLOR_GHOSTS = new Color(255, 255, 255, 225);
    private static final Color COLOR_TRIPLE_ATTACK = new Color(0, 15, 255, 225);
    private static final Color COLOR_LAVA = new Color(82, 0, 0, 225);

    public static int calculateNpcHp(int ratio, int health, int maxHp) {
        if (ratio >= 0 && health > 0 && maxHp != -1) {
            int exactHealth = 0;
            if (ratio > 0) {
                int minHealth = 1;
                int maxHealth;
                if (health > 1) {
                    if (ratio > 1) {
                        minHealth = (maxHp * (ratio - 1) + health - 2) / (health - 1);
                    }

                    maxHealth = (maxHp * ratio - 1) / (health - 1);
                    if (maxHealth > maxHp) {
                        maxHealth = maxHp;
                    }
                } else {
                    maxHealth = maxHp;
                }

                exactHealth = (minHealth + maxHealth + 1) / 2;
            }

            return exactHealth;
        } else {
            return -1;
        }
    }

    public static Font getFontFromInfoboxComponentSize(InfoBoxComponentSize size) {
        Font font;
        switch(size) {
            case LARGE:
            case MEDIUM:
            default:
                font = FontManager.getRunescapeFont();
                break;
            case SMALL:
                font = FontManager.getRunescapeSmallFont();
        }

        return font;
    }

    public static Color getColorFromPhase(Phase phase) {
        Color color;
        switch(phase) {
            case TRIPLE:
                color = COLOR_TRIPLE_ATTACK;
                break;
            case LAVA:
                color = COLOR_LAVA;
                break;
            case GHOSTS:
                color = COLOR_GHOSTS;
                break;
            case AUTO:
            default:
                color = COLOR_DEFAULT;
        }

        return color;
    }

    public static Color getColorFromPrayer(Prayer prayer) {
        switch(prayer) {
            case PROTECT_FROM_MAGIC:
                return Color.BLUE;
            case PROTECT_FROM_MISSILES:
                return Color.GREEN;
            case PROTECT_FROM_MELEE:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

    private Utility() {
    }
}
