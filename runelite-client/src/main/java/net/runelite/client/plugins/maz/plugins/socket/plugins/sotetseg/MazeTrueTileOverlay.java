/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.sotetseg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class MazeTrueTileOverlay
extends Overlay {
    private final Client client;
    private final SotetsegConfig config;
    private final SotetsegPlugin plugin;
    private final SotetsegOverlay overlay;

    @Inject
    private MazeTrueTileOverlay(Client client, SotetsegConfig config, SotetsegPlugin plugin, SotetsegOverlay overlay) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.overlay = overlay;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.trueMaze() && this.plugin.isSotetsegActive() && !this.plugin.mazePings.isEmpty()) {
            WorldPoint playerPos = this.client.getLocalPlayer().getWorldLocation();
            if (playerPos == null) {
                return null;
            }
            LocalPoint playerPosLocal = LocalPoint.fromWorld((Client)this.client, (WorldPoint)playerPos);
            if (playerPosLocal == null) {
                return null;
            }
            this.renderTile(graphics, playerPosLocal, this.config.trueMazeColor());
        }
        return null;
    }

    private void renderTile(Graphics2D graphics, LocalPoint dest, Color color) {
        if (dest == null) {
            return;
        }
        Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)dest);
        if (poly == null) {
            return;
        }
        if (this.config.antiAlias()) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke((float)this.config.trueMazeThicc()));
        graphics.draw(poly);
        graphics.setColor(new Color(0, 0, 0, 50));
    }
}

