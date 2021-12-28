//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

public class MemorizedTornado {
    private NPC npc;
    private WorldPoint lastPosition;
    private WorldPoint currentPosition;

    public MemorizedTornado(NPC npc) {
        this.npc = npc;
        this.lastPosition = null;
        this.currentPosition = null;
    }

    public int getRelativeXDelta(WorldPoint pt) {
        return pt.getX() - this.currentPosition.getX() - (pt.getX() - this.lastPosition.getX());
    }

    public int getRelativeYDelta(WorldPoint pt) {
        return pt.getY() - this.currentPosition.getY() - (pt.getY() - this.lastPosition.getY());
    }

    public int getRelativeDelta(WorldPoint pt) {
        if (this.lastPosition != null && this.currentPosition != null) {
            return this.lastPosition.distanceTo(this.currentPosition) == 0 ? -1 : pt.distanceTo(this.currentPosition) - pt.distanceTo(this.lastPosition);
        } else {
            return -1;
        }
    }

    public NPC getNpc() {
        return this.npc;
    }

    public WorldPoint getLastPosition() {
        return this.lastPosition;
    }

    public void setLastPosition(WorldPoint lastPosition) {
        this.lastPosition = lastPosition;
    }

    public WorldPoint getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(WorldPoint currentPosition) {
        this.currentPosition = currentPosition;
    }
}
