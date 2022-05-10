/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.NPC
 *  net.runelite.api.Point
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.FontManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.socket.plugins.socketthieving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketthieving.SocketThievingPlugin;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class ScavHighlight
extends Overlay {
    private Client client;

    @Inject
    public ScavHighlight(SocketThievingPlugin plugin, Client client) {
        super((Plugin)plugin);
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D g) {
        NPC scav = null;
        for (NPC npc : this.client.getNpcs()) {
            if (npc == null || npc.getId() != 7602 && npc.getId() != 7603) continue;
            scav = npc;
        }
        if (scav == null) {
            return null;
        }
        g.setFont(FontManager.getRunescapeBoldFont());
        Object str = this.client.getVarbitValue(5424) == 1 ? Integer.toString((scav.getHealthRatio() + 2) * 3 / 10) : String.valueOf(scav.getHealthRatio()) + "%";
        Point point = scav.getCanvasTextLocation(g, (String)str, scav.getLogicalHeight());
        if (point == null) {
            return null;
        }
        point = new Point(point.getX(), point.getY() + 20);
        OverlayUtil.renderTextLocation((Graphics2D)g, (Point)point, (String)str, (Color)Color.GREEN);
        return null;
    }
}

