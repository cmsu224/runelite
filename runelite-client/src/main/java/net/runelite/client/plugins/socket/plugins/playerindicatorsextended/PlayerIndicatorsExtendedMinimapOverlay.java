/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.Point
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 *  net.runelite.client.util.Text
 */
package net.runelite.client.plugins.socket.plugins.playerindicatorsextended;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.plugins.socket.plugins.playerindicatorsextended.PlayerIndicatorsExtendedConfig;
import net.runelite.client.plugins.socket.plugins.playerindicatorsextended.PlayerIndicatorsExtendedPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

public class PlayerIndicatorsExtendedMinimapOverlay
extends Overlay {
    private final Client client;
    private final PlayerIndicatorsExtendedPlugin plugin;
    private final PlayerIndicatorsExtendedConfig config;

    @Inject
    private PlayerIndicatorsExtendedMinimapOverlay(Client client, PlayerIndicatorsExtendedPlugin plugin, PlayerIndicatorsExtendedConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPriority(OverlayPriority.HIGHEST);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public Dimension render(Graphics2D graphics) {
        for (Actor actor : this.plugin.getPlayers()) {
            Point minimapPoint;
            String name = Text.sanitize((String)actor.getName());
            if (!this.config.drawMinimap() || (minimapPoint = actor.getMinimapLocation()) == null) continue;
            OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)minimapPoint, (String)name, (Color)this.config.nameColor());
        }
        return null;
    }
}

