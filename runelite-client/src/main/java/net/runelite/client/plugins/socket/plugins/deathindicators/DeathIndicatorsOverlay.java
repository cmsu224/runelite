/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.NPC
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.outline.ModelOutlineRenderer
 */
package net.runelite.client.plugins.socket.plugins.deathindicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.client.plugins.socket.plugins.deathindicators.DeathIndicatorsConfig;
import net.runelite.client.plugins.socket.plugins.deathindicators.DeathIndicatorsPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class DeathIndicatorsOverlay
extends Overlay {
    private final DeathIndicatorsConfig config;
    private final DeathIndicatorsPlugin plugin;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    public DeathIndicatorsOverlay(DeathIndicatorsPlugin plugin, DeathIndicatorsConfig config, ModelOutlineRenderer modelOutlineRenderer) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.showOutline()) {
            for (NPC n : this.plugin.getDeadNylos()) {
                this.modelOutlineRenderer.drawOutline(n, 2, Color.red, 4);
            }
        }
        return null;
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(2.0f));
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            graphics.fill(polygon);
        }
    }
}

