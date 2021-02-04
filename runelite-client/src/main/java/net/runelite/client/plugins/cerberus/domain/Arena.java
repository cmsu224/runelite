//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.domain;

import javax.annotation.Nullable;
import net.runelite.api.coords.WorldPoint;

public enum Arena {
    WEST(1231, 1249, 1243, 1257),
    NORTH(1295, 1313, 1307, 1321),
    EAST(1359, 1377, 1243, 1257);

    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;

    @Nullable
    public static Arena getArena(WorldPoint worldPoint) {
        Arena[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Arena arena = var1[var3];
            if (worldPoint.getX() >= arena.getX1() && worldPoint.getX() <= arena.getX2() && worldPoint.getY() >= arena.getY1() && worldPoint.getY() <= arena.getY2()) {
                return arena;
            }
        }

        return null;
    }

    public WorldPoint getGhostTile(int ghostIndex) {
        return ghostIndex <= 2 && ghostIndex >= 0 ? new WorldPoint(this.x1 + 8 + ghostIndex, this.y1 + 13, 0) : null;
    }

    public int getX1() {
        return this.x1;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY1() {
        return this.y1;
    }

    public int getY2() {
        return this.y2;
    }

    private Arena(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
}
