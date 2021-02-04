//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.spellbook;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public enum Spellbook {
    STANDARD(0, "standard"),
    ANCIENT(1, "ancient"),
    LUNAR(2, "lunar"),
    ARCEUUS(3, "arceuus");

    private final int id;
    private final String configKey;
    private static final ImmutableMap<Integer, Spellbook> map;

    public static Spellbook getByID(int id) {
        return (Spellbook)map.get(id);
    }

    private Spellbook(int id, String configKey) {
        this.id = id;
        this.configKey = configKey;
    }

    int getId() {
        return this.id;
    }

    String getConfigKey() {
        return this.configKey;
    }

    static {
        Builder<Integer, Spellbook> builder = new Builder();
        Spellbook[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Spellbook s = var1[var3];
            builder.put(s.id, s);
        }

        map = builder.build();
    }
}
