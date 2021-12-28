//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.runelite.api.NPC;

public enum MaidenPhase {
    ONE("70", 0.7D, 10814, 8360, 10822),
    TWO("50", 0.5D, 10815, 8361, 10823),
    THREE("30", 0.3D, 10816, 8362, 10824),
    OTHER("0", 0.0D, 0, 0, 0);

    private final String phaseKey;
    private final double threshold;
    private final int sm_npc_id;
    private final int rg_npc_id;
    private final int hm_npc_id;
    private static final Map<Integer, MaidenPhase> lookupMap;

    public static MaidenPhase of(NPC npc) {
        return (MaidenPhase)lookupMap.getOrDefault(npc.getId(), OTHER);
    }

    public int calculateNewThreshold(int baseHitpoints) {
        return (int)Math.floor((double)baseHitpoints * this.threshold);
    }

    private MaidenPhase(String phaseKey, double threshold, int sm_npc_id, int rg_npc_id, int hm_npc_id) {
        this.phaseKey = phaseKey;
        this.threshold = threshold;
        this.sm_npc_id = sm_npc_id;
        this.rg_npc_id = rg_npc_id;
        this.hm_npc_id = hm_npc_id;
    }

    public String getPhaseKey() {
        return this.phaseKey;
    }

    static {
        Builder<Integer, MaidenPhase> builder = new Builder();
        MaidenPhase[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            MaidenPhase phase = var1[var3];
            if (phase != OTHER) {
                builder.put(phase.sm_npc_id, phase);
                builder.put(phase.rg_npc_id, phase);
                builder.put(phase.hm_npc_id, phase);
            }
        }

        lookupMap = builder.build();
    }
}
