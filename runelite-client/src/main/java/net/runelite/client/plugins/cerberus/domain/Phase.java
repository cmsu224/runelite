//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.domain;

import java.awt.Color;
import net.runelite.api.Skill;

public enum Phase {
    SPAWNING((Skill)null, 4, (Color)null),
    AUTO(Skill.ATTACK, 6, (Color)null),
    TRIPLE(Skill.FLETCHING, 6, new Color(153, 214, 255)),
    GHOSTS(Skill.PRAYER, 8, new Color(255, 255, 255)),
    LAVA(Skill.FIREMAKING, 8, new Color(255, 153, 153));

    private final Skill type;
    private final int tickDelay;
    private final Color textColor;

    public Skill getType() {
        return this.type;
    }

    public int getTickDelay() {
        return this.tickDelay;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    private Phase(Skill type, int tickDelay, Color textColor) {
        this.type = type;
        this.tickDelay = tickDelay;
        this.textColor = textColor;
    }
}
