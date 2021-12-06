//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.inferno;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.lang3.ArrayUtils;

class InfernoNPC {
    private NPC npc;
    private InfernoNPC.Type type;
    private InfernoNPC.Attack nextAttack;
    private int ticksTillNextAttack;
    private int lastAnimation;
    private boolean lastCanAttack;
    private final Map<WorldPoint, Integer> safeSpotCache;

    InfernoNPC(NPC npc) {
        this.npc = npc;
        this.type = InfernoNPC.Type.typeFromId(npc.getId());
        this.nextAttack = this.type.getDefaultAttack();
        this.ticksTillNextAttack = 0;
        this.lastAnimation = -1;
        this.lastCanAttack = false;
        this.safeSpotCache = new HashMap();
    }

    void updateNextAttack(InfernoNPC.Attack nextAttack, int ticksTillNextAttack) {
        this.nextAttack = nextAttack;
        this.ticksTillNextAttack = ticksTillNextAttack;
    }

    private void updateNextAttack(InfernoNPC.Attack nextAttack) {
        this.nextAttack = nextAttack;
    }

    boolean canAttack(Client client, WorldPoint target) {
        if (this.safeSpotCache.containsKey(target)) {
            return (Integer)this.safeSpotCache.get(target) == 2;
        } else {
            boolean hasLos = (new WorldArea(target, 1, 1)).hasLineOfSightTo(client, this.getNpc().getWorldArea());
            boolean hasRange = this.getType().getDefaultAttack() == InfernoNPC.Attack.MELEE ? this.getNpc().getWorldArea().isInMeleeDistance(target) : this.getNpc().getWorldArea().distanceTo(target) <= this.getType().getRange();
            if (hasLos && hasRange) {
                this.safeSpotCache.put(target, 2);
            }

            return hasLos && hasRange;
        }
    }

    boolean canMoveToAttack(Client client, WorldPoint target, List<WorldPoint> obstacles) {
        if (!this.safeSpotCache.containsKey(target)) {
            List<WorldPoint> realObstacles = new ArrayList();
            Iterator var5 = obstacles.iterator();

            while(var5.hasNext()) {
                WorldPoint obstacle = (WorldPoint)var5.next();
                if (!this.getNpc().getWorldArea().toWorldPointList().contains(obstacle)) {
                    realObstacles.add(obstacle);
                }
            }

            WorldArea targetArea = new WorldArea(target, 1, 1);
            WorldArea currentWorldArea = this.getNpc().getWorldArea();
            int steps = 0;

            while(true) {
                ++steps;
                if (steps > 30) {
                    return false;
                }

                WorldArea predictedWorldArea = currentWorldArea.calculateNextTravellingPoint(client, targetArea, true, (x) -> {
                    Iterator var2 = realObstacles.iterator();

                    WorldPoint obstacle;
                    do {
                        if (!var2.hasNext()) {
                            return true;
                        }

                        obstacle = (WorldPoint)var2.next();
                    } while(!(new WorldArea(x, 1, 1)).intersectsWith(new WorldArea(obstacle, 1, 1)));

                    return false;
                });
                if (predictedWorldArea == null) {
                    this.safeSpotCache.put(target, 1);
                    return true;
                }

                if (predictedWorldArea == currentWorldArea) {
                    this.safeSpotCache.put(target, 0);
                    return false;
                }

                boolean hasLos = (new WorldArea(target, 1, 1)).hasLineOfSightTo(client, predictedWorldArea);
                boolean hasRange = this.getType().getDefaultAttack() == InfernoNPC.Attack.MELEE ? predictedWorldArea.isInMeleeDistance(target) : predictedWorldArea.distanceTo(target) <= this.getType().getRange();
                if (hasLos && hasRange) {
                    this.safeSpotCache.put(target, 1);
                    return true;
                }

                currentWorldArea = predictedWorldArea;
            }
        } else {
            return (Integer)this.safeSpotCache.get(target) == 1 || (Integer)this.safeSpotCache.get(target) == 2;
        }
    }

    private boolean couldAttackPrevTick(Client client, WorldPoint lastPlayerLocation) {
        return (new WorldArea(lastPlayerLocation, 1, 1)).hasLineOfSightTo(client, this.getNpc().getWorldArea());
    }

    void gameTick(Client client, WorldPoint lastPlayerLocation, boolean finalPhase, int ticksSinceFinalPhase) {
        this.safeSpotCache.clear();
        if (this.ticksTillNextAttack > 0) {
            --this.ticksTillNextAttack;
        }

        InfernoNPC.Attack nextBlobAttack;
        if (this.getType() == InfernoNPC.Type.JAD && this.getNpc().getAnimation() != -1 && this.getNpc().getAnimation() != this.lastAnimation) {
            nextBlobAttack = InfernoNPC.Attack.attackFromId(this.getNpc().getAnimation());
            if (nextBlobAttack != null && nextBlobAttack != InfernoNPC.Attack.UNKNOWN) {
                this.updateNextAttack(nextBlobAttack, this.getType().getTicksAfterAnimation());
            }
        }

        if (this.ticksTillNextAttack <= 0) {
            switch(this.getType()) {
                case ZUK:
                    if (this.getNpc().getAnimation() == 7566) {
                        if (finalPhase) {
                            if (ticksSinceFinalPhase > 3) {
                                this.updateNextAttack(this.getType().getDefaultAttack(), 7);
                            }
                        } else {
                            this.updateNextAttack(this.getType().getDefaultAttack(), 10);
                        }
                    }
                    break;
                case JAD:
                    if (this.getNextAttack() != InfernoNPC.Attack.UNKNOWN) {
                        this.updateNextAttack(this.getType().getDefaultAttack(), 8);
                    }
                    break;
                case BLOB:
                    if (!this.lastCanAttack && this.couldAttackPrevTick(client, lastPlayerLocation)) {
                        this.updateNextAttack(InfernoNPC.Attack.UNKNOWN, 3);
                    } else if (!this.lastCanAttack && this.canAttack(client, client.getLocalPlayer().getWorldLocation())) {
                        this.updateNextAttack(InfernoNPC.Attack.UNKNOWN, 4);
                    } else if (this.getNpc().getAnimation() != -1) {
                        this.updateNextAttack(this.getType().getDefaultAttack(), this.getType().getTicksAfterAnimation());
                    }
                    break;
                case BAT:
                    if (this.canAttack(client, client.getLocalPlayer().getWorldLocation()) && this.getNpc().getAnimation() != 7577 && this.getNpc().getAnimation() != -1) {
                        this.updateNextAttack(this.getType().getDefaultAttack(), this.getType().getTicksAfterAnimation());
                    }
                    break;
                case MELEE:
                case RANGER:
                case MAGE:
                    if (this.getNpc().getAnimation() != 7597 && this.getNpc().getAnimation() != 7605 && this.getNpc().getAnimation() != 7604 && this.getNpc().getAnimation() != 7610 && this.getNpc().getAnimation() != 7612) {
                        if (this.getNpc().getAnimation() == 7600) {
                            this.updateNextAttack(this.getType().getDefaultAttack(), 12);
                        } else if (this.getNpc().getAnimation() == 7611) {
                            this.updateNextAttack(this.getType().getDefaultAttack(), 8);
                        }
                    } else {
                        this.updateNextAttack(this.getType().getDefaultAttack(), this.getType().getTicksAfterAnimation());
                    }
                    break;
                default:
                    if (this.getNpc().getAnimation() != -1) {
                        this.updateNextAttack(this.getType().getDefaultAttack(), this.getType().getTicksAfterAnimation());
                    }
            }
        }

        if (this.getType() == InfernoNPC.Type.BLOB && this.getTicksTillNextAttack() == 3 && client.getLocalPlayer().getWorldLocation().distanceTo(this.getNpc().getWorldArea()) <= InfernoNPC.Type.BLOB.getRange()) {
            nextBlobAttack = InfernoNPC.Attack.UNKNOWN;
            if (client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES)) {
                nextBlobAttack = InfernoNPC.Attack.MAGIC;
            } else if (client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC)) {
                nextBlobAttack = InfernoNPC.Attack.RANGED;
            }

            this.updateNextAttack(nextBlobAttack);
        }

        this.lastAnimation = this.getNpc().getAnimation();
        this.lastCanAttack = this.canAttack(client, client.getLocalPlayer().getWorldLocation());
    }

    NPC getNpc() {
        return this.npc;
    }

    InfernoNPC.Type getType() {
        return this.type;
    }

    InfernoNPC.Attack getNextAttack() {
        return this.nextAttack;
    }

    int getTicksTillNextAttack() {
        return this.ticksTillNextAttack;
    }

    void setTicksTillNextAttack(int ticksTillNextAttack) {
        this.ticksTillNextAttack = ticksTillNextAttack;
    }

    static enum Type {
        NIBBLER(new int[]{7691}, InfernoNPC.Attack.MELEE, 4, 99, 100),
        BAT(new int[]{7692}, InfernoNPC.Attack.RANGED, 3, 4, 7),
        BLOB(new int[]{7693}, InfernoNPC.Attack.UNKNOWN, 6, 15, 4),
        MELEE(new int[]{7697}, InfernoNPC.Attack.MELEE, 4, 1, 3),
        RANGER(new int[]{7698, 7702}, InfernoNPC.Attack.RANGED, 4, 98, 2),
        MAGE(new int[]{7699, 7703}, InfernoNPC.Attack.MAGIC, 4, 98, 1),
        JAD(new int[]{7700, 7704}, InfernoNPC.Attack.UNKNOWN, 3, 99, 0),
        HEALER_JAD(new int[]{3128, 7701, 7705}, InfernoNPC.Attack.MELEE, 4, 1, 6),
        ZUK(new int[]{7706}, InfernoNPC.Attack.UNKNOWN, 10, 99, 99),
        HEALER_ZUK(new int[]{7708}, InfernoNPC.Attack.UNKNOWN, -1, 99, 100);

        private final int[] npcIds;
        private final InfernoNPC.Attack defaultAttack;
        private final int ticksAfterAnimation;
        private final int range;
        private final int priority;

        private Type(int[] npcIds, InfernoNPC.Attack defaultAttack, int ticksAfterAnimation, int range, int priority) {
            this.npcIds = npcIds;
            this.defaultAttack = defaultAttack;
            this.ticksAfterAnimation = ticksAfterAnimation;
            this.range = range;
            this.priority = priority;
        }

        static InfernoNPC.Type typeFromId(int npcId) {
            InfernoNPC.Type[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                InfernoNPC.Type type = var1[var3];
                if (ArrayUtils.contains(type.getNpcIds(), npcId)) {
                    return type;
                }
            }

            return null;
        }

        int[] getNpcIds() {
            return this.npcIds;
        }

        InfernoNPC.Attack getDefaultAttack() {
            return this.defaultAttack;
        }

        int getTicksAfterAnimation() {
            return this.ticksAfterAnimation;
        }

        int getRange() {
            return this.range;
        }

        int getPriority() {
            return this.priority;
        }
    }

    static enum Attack {
        MELEE(Prayer.PROTECT_FROM_MELEE, Color.ORANGE, Color.RED, new int[]{7574, 7582, 7597, 7604, 7612}),
        RANGED(Prayer.PROTECT_FROM_MISSILES, Color.GREEN, new Color(0, 128, 0), new int[]{7578, 7581, 7605, 7593}),
        MAGIC(Prayer.PROTECT_FROM_MAGIC, Color.CYAN, Color.BLUE, new int[]{7583, 7610, 7592}),
        UNKNOWN((Prayer)null, Color.WHITE, Color.GRAY, new int[0]);

        private final Prayer prayer;
        private final Color normalColor;
        private final Color criticalColor;
        private final int[] animationIds;

        private Attack(Prayer prayer, Color normalColor, Color criticalColor, int[] animationIds) {
            this.prayer = prayer;
            this.normalColor = normalColor;
            this.criticalColor = criticalColor;
            this.animationIds = animationIds;
        }

        static InfernoNPC.Attack attackFromId(int animationId) {
            InfernoNPC.Attack[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                InfernoNPC.Attack attack = var1[var3];
                if (ArrayUtils.contains(attack.getAnimationIds(), animationId)) {
                    return attack;
                }
            }

            return null;
        }

        Prayer getPrayer() {
            return this.prayer;
        }

        Color getNormalColor() {
            return this.normalColor;
        }

        Color getCriticalColor() {
            return this.criticalColor;
        }

        int[] getAnimationIds() {
            return this.animationIds;
        }
    }
}
