//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.domain;

import net.runelite.client.plugins.cerberus.domain.Cerberus.Attack;

public class CerberusAttack {
    private final int tick;
    private final Attack attack;

    public int getTick() {
        return this.tick;
    }

    public Attack getAttack() {
        return this.attack;
    }

    public CerberusAttack(int tick, Attack attack) {
        this.tick = tick;
        this.attack = attack;
    }
}
