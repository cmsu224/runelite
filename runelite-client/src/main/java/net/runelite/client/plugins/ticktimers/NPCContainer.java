//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.ticktimers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import java.awt.Color;
import java.util.Objects;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;

class NPCContainer {
    private final NPC npc;
    private final int npcIndex;
    private final String npcName;
    private final ImmutableSet<Integer> animations;
    private final int attackSpeed;
    private final NPCContainer.BossMonsters monsterType;
    private int npcSize;
    private int ticksUntilAttack;
    private Actor npcInteracting;
    private NPCContainer.AttackStyle attackStyle;

    NPCContainer(NPC npc, int attackSpeed) {
        this.npc = npc;
        this.npcName = npc.getName();
        this.npcIndex = npc.getIndex();
        this.npcInteracting = npc.getInteracting();
        this.attackStyle = NPCContainer.AttackStyle.UNKNOWN;
        this.attackSpeed = attackSpeed;
        this.ticksUntilAttack = -1;
        NPCContainer.BossMonsters monster = NPCContainer.BossMonsters.of(npc.getId());
        if (monster == null) {
            throw new IllegalStateException();
        } else {
            this.monsterType = monster;
            this.animations = monster.animations;
            this.attackStyle = monster.attackStyle;

        }
    }

    NPCContainer(NPC npc, int attackSpeed, AttackStyle a, ImmutableSet<Integer> animations) {
        this.npc = npc;
        this.npcName = npc.getName();
        this.npcIndex = npc.getIndex();
        this.npcInteracting = npc.getInteracting();
        this.attackStyle = a;
        this.attackSpeed = attackSpeed;
        this.ticksUntilAttack = -1;
        this.animations = animations;
        this.monsterType = null;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.npc});
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            NPCContainer that = (NPCContainer)o;
            return Objects.equals(this.npc, that.npc);
        } else {
            return false;
        }
    }

    int getNpcIndex() {
        return this.npcIndex;
    }

    String getNpcName() {
        return this.npcName;
    }

    ImmutableSet<Integer> getAnimations() {
        return this.animations;
    }

    int getAttackSpeed() {
        return this.attackSpeed;
    }

    int getNpcSize() {
        return this.npcSize;
    }

    int getTicksUntilAttack() {
        return this.ticksUntilAttack;
    }

    Actor getNpcInteracting() {
        return this.npcInteracting;
    }

    NPCContainer.AttackStyle getAttackStyle() {
        return this.attackStyle;
    }

    NPC getNpc() {
        return this.npc;
    }

    NPCContainer.BossMonsters getMonsterType() {
        return this.monsterType;
    }

    void setTicksUntilAttack(int ticksUntilAttack) {
        this.ticksUntilAttack = ticksUntilAttack;
    }

    void setNpcInteracting(Actor npcInteracting) {
        this.npcInteracting = npcInteracting;
    }

    void setAttackStyle(NPCContainer.AttackStyle attackStyle) {
        this.attackStyle = attackStyle;
    }

    public static enum AttackStyle {
        MAGE("Mage", Color.CYAN, Prayer.PROTECT_FROM_MAGIC),
        RANGE("Range", Color.GREEN, Prayer.PROTECT_FROM_MISSILES),
        MELEE("Melee", Color.RED, Prayer.PROTECT_FROM_MELEE),
        UNKNOWN("Unknown", Color.WHITE, (Prayer)null);

        private String name;
        private Color color;
        private Prayer prayer;

        private AttackStyle(String name, Color color, Prayer prayer) {
            this.name = name;
            this.color = color;
            this.prayer = prayer;
        }

        public String getName() {
            return this.name;
        }

        public Color getColor() {
            return this.color;
        }

        public Prayer getPrayer() {
            return this.prayer;
        }
    }

    static enum BossMonsters {
        SERGEANT_STRONGSTACK(2216, NPCContainer.AttackStyle.MELEE, ImmutableSet.of(6154, 6156, 7071)),
        SERGEANT_STEELWILL(2217, NPCContainer.AttackStyle.MAGE, ImmutableSet.of(6154, 6156, 7071)),
        SERGEANT_GRIMSPIKE(2218, NPCContainer.AttackStyle.RANGE, ImmutableSet.of(6154, 6156, 7073)),
        GENERAL_GRAARDOR(2215, NPCContainer.AttackStyle.MELEE, ImmutableSet.of(7018, 7020, 7021)),
        TSTANON_KARLAK(3130, NPCContainer.AttackStyle.MELEE, ImmutableSet.of(64)),
        BALFRUG_KREEYATH(3132, NPCContainer.AttackStyle.MAGE, ImmutableSet.of(64, 4630)),
        ZAKLN_GRITCH(3131, NPCContainer.AttackStyle.RANGE, ImmutableSet.of(64, 7077)),
        KRIL_TSUTSAROTH(3129, NPCContainer.AttackStyle.UNKNOWN, ImmutableSet.of(6950, 6948)),
        STARLIGHT(2206, NPCContainer.AttackStyle.MELEE, ImmutableSet.of(6376)),
        GROWLER(2207, NPCContainer.AttackStyle.MAGE, ImmutableSet.of(7037)),
        BREE(2208, NPCContainer.AttackStyle.RANGE, ImmutableSet.of(7026)),
        COMMANDER_ZILYANA(2205, NPCContainer.AttackStyle.UNKNOWN, ImmutableSet.of(6967, 6964, 6970)),
        FLIGHT_KILISA(3165, NPCContainer.AttackStyle.MELEE, ImmutableSet.of(6957)),
        FLOCKLEADER_GEERIN(3164, NPCContainer.AttackStyle.RANGE, ImmutableSet.of(6956, 6958)),
        WINGMAN_SKREE(3163, NPCContainer.AttackStyle.MAGE, ImmutableSet.of(6955)),
        KREEARRA(3162, NPCContainer.AttackStyle.RANGE, ImmutableSet.of(6978));

        private static final ImmutableMap<Integer, NPCContainer.BossMonsters> idMap;
        private final int npcID;
        private final NPCContainer.AttackStyle attackStyle;
        private final ImmutableSet<Integer> animations;

        static NPCContainer.BossMonsters of(int npcID) {
            return (NPCContainer.BossMonsters)idMap.get(npcID);
        }

        private BossMonsters(int npcID, NPCContainer.AttackStyle attackStyle, ImmutableSet<Integer> animations) {
            this.npcID = npcID;
            this.attackStyle = attackStyle;
            this.animations = animations;
        }

        static {
            Builder<Integer, NPCContainer.BossMonsters> builder = ImmutableMap.builder();
            NPCContainer.BossMonsters[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                NPCContainer.BossMonsters monster = var1[var3];
                builder.put(monster.npcID, monster);
            }

            idMap = builder.build();
        }
    }
}
