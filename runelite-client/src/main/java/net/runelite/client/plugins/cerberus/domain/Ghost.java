//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.domain;

import com.google.common.collect.ImmutableMap.Builder;
import java.awt.Color;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.NPC;
import net.runelite.api.Skill;

public enum Ghost {
    RANGE(5867, Skill.RANGED, Color.GREEN),
    MAGE(5868, Skill.MAGIC, Color.BLUE),
    MELEE(5869, Skill.ATTACK, Color.RED);

    private static final Map<Integer, Ghost> MAP;
    private final int npcId;
    private final Skill type;
    private final Color color;

    @Nullable
    public static Ghost fromNPC(NPC npc) {
        return (Ghost)MAP.get(npc.getId());
    }

    public int getNpcId() {
        return this.npcId;
    }

    public Skill getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    private Ghost(int npcId, Skill type, Color color) {
        this.npcId = npcId;
        this.type = type;
        this.color = color;
    }

    static {
        Builder<Integer, Ghost> builder = new Builder();
        Ghost[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Ghost ghost = var1[var3];
            builder.put(ghost.getNpcId(), ghost);
        }

        MAP = builder.build();
    }
}
