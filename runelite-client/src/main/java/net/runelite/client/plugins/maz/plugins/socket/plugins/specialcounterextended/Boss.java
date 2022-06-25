/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.specialcounterextended;

import com.google.common.collect.Sets;
import java.util.Set;

enum Boss {
    ABYSSAL_SIRE(5886, 5887, 5888, 5889, 5890, 5891, 5908),
    CALLISTO(6503, 6609),
    CERBERUS(5862, 5863, 5866),
    CHAOS_ELEMENTAL(2054, 6505),
    CORPOREAL_BEAST(319),
    GENERAL_GRAARDOR(2215, 6494),
    GIANT_MOLE(5779, 6499),
    KALPHITE_QUEEN(128, 963, 965, 4303, 4304, 6500, 6501),
    KING_BLACK_DRAGON(239, 2642, 6502),
    KRIL_TSUROTH(3129, 6495),
    VENETENATIS(6504, 6610),
    VETION(6611, 6612);

    private final Set<Integer> ids;

    private Boss(Integer ... ids) {
        this.ids = Sets.newHashSet(ids);
    }

    static Boss getBoss(int id) {
        for (Boss boss : Boss.values()) {
            if (!boss.ids.contains(id)) continue;
            return boss;
        }
        return null;
    }

    public Set<Integer> getIds() {
        return this.ids;
    }

    public String toString() {
        return "Boss." + this.name() + "(ids=" + this.getIds() + ")";
    }
}

