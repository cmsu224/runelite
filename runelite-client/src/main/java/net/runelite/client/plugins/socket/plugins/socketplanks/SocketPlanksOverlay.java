/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.FontManager
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.socket.plugins.socketplanks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksConfig;
import net.runelite.client.plugins.socket.plugins.socketplanks.SocketPlanksPlugin;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SocketPlanksOverlay
extends OverlayPanel {
    private final Client client;
    private final SocketPlanksPlugin plugin;
    private final SocketPlanksConfig config;

    @Inject
    private SocketPlanksOverlay(Client client, SocketPlanksPlugin plugin, SocketPlanksConfig config) {
        super((Plugin)plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.LOW);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.plugin.planksDropped && !this.plugin.planksPickedUp && this.plugin.planksDroppedTile != null) {
            LocalPoint point = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.plugin.planksDroppedTile);
            this.renderSpot(graphics, this.client, point, Color.WHITE);
        }
        return super.render(graphics);
    }

    private void renderSpot(Graphics2D graphics, Client client, LocalPoint point, Color color) {
        Polygon poly = Perspective.getCanvasTilePoly((Client)client, (LocalPoint)point);
        if (poly != null) {
            OverlayUtil.renderPolygon((Graphics2D)graphics, (Shape)poly, (Color)color);
        }
        Point pt = Perspective.getCanvasTextLocation((Client)client, (Graphics2D)graphics, (LocalPoint)point, (String)this.plugin.nameGotPlanks, (int)0);
        graphics.setFont(FontManager.getRunescapeBoldFont());
        OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)pt, (String)this.plugin.nameGotPlanks, (Color)Color.WHITE);
    }
}

