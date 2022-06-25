/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.coords.LocalPoint
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
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class InvisibleTargetsOverlay
extends Overlay {
    private final Client client;
    private final SotetsegPlugin plugin;
    private final SotetsegConfig config;

    @Inject
    private InvisibleTargetsOverlay(Client client, SotetsegPlugin plugin, SotetsegConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.warnBall() && this.plugin.invisibleTicks > 0) {
            for (Player players : this.client.getPlayers()) {
                if (!this.plugin.invisibleTargets.contains(players.getName()) || players.getLocalLocation() == null || players.getName() == null) continue;
                this.renderTile(graphics, players.getLocalLocation(), this.config.ballTargetColor());
            }
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
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.draw(poly);
        graphics.setColor(new Color(0, 0, 0, 0));
    }
}

