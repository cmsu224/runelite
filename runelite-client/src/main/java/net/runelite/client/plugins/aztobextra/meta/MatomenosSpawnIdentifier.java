//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import com.google.common.collect.ArrayListMultimap;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum MatomenosSpawnIdentifier {
    N1("N1", 216689714834258L),
    N2("N2", 216689719029074L),
    N3("N3", 216689723223890L),
    N4_WALL("N4 (1)", 216689727418706L),
    N4("N4 (2)", 216689727385934L),
    S1("S1", 216689714473770L),
    S2("S2", 216689718668586L),
    S3("S3", 216689722863402L),
    S4_WALL("S4 (1)", 216689727058218L),
    S4("S4 (2)", 216689727090990L);

    private final String key;
    private final long hash;
    private static final ArrayListMultimap<String, Integer> identifierMultiMap = ArrayListMultimap.create();

    private MatomenosSpawnIdentifier(String key, long hash) {
        this.key = key;
        this.hash = hash;
    }

    @Nullable
    public static Pair<String, Boolean> of(Client client, @Nullable NPC npc) {
        if (npc == null) {
            return null;
        } else {
            WorldPoint wp = WorldPoint.fromLocal(client, npc.getLocalLocation());
            int stack = (wp.getRegionX() & 63) << 7 | (wp.getRegionY() & 63) << 1;
            Iterator var4 = identifierMultiMap.keys().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                Iterator var6 = identifierMultiMap.get(key).iterator();

                while(var6.hasNext()) {
                    int mapStack = (Integer)var6.next();
                    if ((mapStack & 8190 ^ stack) == 0) {
                        return new ImmutablePair(key, (mapStack & 1) != 0);
                    }
                }
            }

            return null;
        }
    }

    static {
        MatomenosSpawnIdentifier[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            MatomenosSpawnIdentifier id = var0[var2];
            identifierMultiMap.put(id.key, (int)(id.hash >> 13 & 8191L));
            identifierMultiMap.put(id.key, (int)(id.hash & 8191L));
        }

    }
}
