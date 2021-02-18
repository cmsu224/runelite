//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import com.google.common.collect.Sets;
import java.util.Set;

enum Boss {
    ABYSSAL_SIRE(new Integer[]{5886, 5887, 5888, 5889, 5890, 5891, 5908}),
    CALLISTO(new Integer[]{6503, 6609}),
    CERBERUS(new Integer[]{5862, 5863, 5866}),
    CHAOS_ELEMENTAL(new Integer[]{2054, 6505}),
    CORPOREAL_BEAST(new Integer[]{319}),
    GENERAL_GRAARDOR(new Integer[]{2215, 6494}),
    GIANT_MOLE(new Integer[]{5779, 6499}),
    KALPHITE_QUEEN(new Integer[]{128, 963, 965, 4303, 4304, 6500, 6501}),
    KING_BLACK_DRAGON(new Integer[]{239, 2642, 6502}),
    KRIL_TSUROTH(new Integer[]{3129, 6495}),
    VENETENATIS(new Integer[]{6504, 6610}),
    VETION(new Integer[]{6611, 6612});

    private final Set<Integer> ids;

    private Boss(Integer... ids) {
        this.ids = Sets.newHashSet(ids);
    }

    static Boss getBoss(int id) {
        Boss[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Boss boss = var1[var3];
            if (boss.ids.contains(id)) {
                return boss;
            }
        }

        return null;
    }

    public Set<Integer> getIds() {
        return this.ids;
    }

    public String toString() {
        return "Boss(ids=" + this.getIds() + ")";
    }
}
