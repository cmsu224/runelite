//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.cerberus.util.Utility;

public class Cerberus {
    private static final int TOTAL_HP = 600;
    private static final int GHOST_HP = 400;
    private static final int LAVA_HP = 200;
    private final List<Phase> attacksDone;
    private final NPC npc;
    private int phaseCount;
    private int lastAttackTick;
    private Phase lastAttackPhase;
    private Cerberus.Attack lastAttack;
    private int lastGhostYellTick;
    private long lastGhostYellTime;
    private Cerberus.Attack lastTripleAttack;
    private int hp;

    public Cerberus(@NonNull NPC npc) {
        if (npc == null) {
            throw new NullPointerException("npc is marked non-null but is null");
        } else {
            this.npc = npc;
            this.attacksDone = new ArrayList();
            this.lastAttackPhase = Phase.SPAWNING;
            this.hp = 600;
        }
    }

    public void nextPhase(Phase lastAttackPhase) {
        ++this.phaseCount;
        this.lastAttackPhase = lastAttackPhase;
    }

    public void doProjectileOrAnimation(int gameTick, Cerberus.Attack attack) {
        this.lastAttackTick = gameTick;
        this.lastAttack = attack;
    }

    public int getHp() {
        int calcualtedHp = Utility.calculateNpcHp(this.npc.getHealthRatio(), this.npc.getHealthScale(), 600);
        if (calcualtedHp != -1) {
            this.hp = calcualtedHp;
        }

        return this.hp;
    }

    public Phase getNextAttackPhase(int i, int hp) {
        int nextAttack = this.phaseCount + i;
        if (nextAttack == 0) {
            return Phase.SPAWNING;
        } else if ((nextAttack - 1) % 10 == 0) {
            return Phase.TRIPLE;
        } else if (nextAttack % 7 == 0 && hp <= 400) {
            return Phase.GHOSTS;
        } else {
            return nextAttack % 5 == 0 && hp <= 200 ? Phase.LAVA : Phase.AUTO;
        }
    }

    public List<Phase> getAttacksDone() {
        return this.attacksDone;
    }

    public NPC getNpc() {
        return this.npc;
    }

    public int getPhaseCount() {
        return this.phaseCount;
    }

    public int getLastAttackTick() {
        return this.lastAttackTick;
    }

    public Phase getLastAttackPhase() {
        return this.lastAttackPhase;
    }

    public Cerberus.Attack getLastAttack() {
        return this.lastAttack;
    }

    public int getLastGhostYellTick() {
        return this.lastGhostYellTick;
    }

    public long getLastGhostYellTime() {
        return this.lastGhostYellTime;
    }

    public Cerberus.Attack getLastTripleAttack() {
        return this.lastTripleAttack;
    }

    public void setLastGhostYellTick(int lastGhostYellTick) {
        this.lastGhostYellTick = lastGhostYellTick;
    }

    public void setLastGhostYellTime(long lastGhostYellTime) {
        this.lastGhostYellTime = lastGhostYellTime;
    }

    public void setLastTripleAttack(Cerberus.Attack lastTripleAttack) {
        this.lastTripleAttack = lastTripleAttack;
    }

    public static enum Attack {
        SPAWN((Prayer)null, 0),
        AUTO((Prayer)null, 1),
        MELEE(Prayer.PROTECT_FROM_MELEE, 1),
        RANGED(Prayer.PROTECT_FROM_MISSILES, 1),
        MAGIC(Prayer.PROTECT_FROM_MAGIC, 1),
        LAVA((Prayer)null, 0),
        GHOSTS((Prayer)null, 0),
        GHOST_MELEE(Prayer.PROTECT_FROM_MELEE, 2),
        GHOST_RANGED(Prayer.PROTECT_FROM_MISSILES, 2),
        GHOST_MAGIC(Prayer.PROTECT_FROM_MAGIC, 2);

        private final Prayer prayer;
        private final int priority;

        public Prayer getPrayer() {
            return this.prayer;
        }

        public int getPriority() {
            return this.priority;
        }

        private Attack(Prayer prayer, int priority) {
            this.prayer = prayer;
            this.priority = priority;
        }
    }
}
