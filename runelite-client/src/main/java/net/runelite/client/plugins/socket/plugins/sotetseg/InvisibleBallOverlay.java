/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.components.LineComponent
 *  net.runelite.client.ui.overlay.components.PanelComponent
 */
package net.runelite.client.plugins.socket.plugins.sotetseg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.socket.plugins.sotetseg.SotetsegConfig;
import net.runelite.client.plugins.socket.plugins.sotetseg.SotetsegPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class InvisibleBallOverlay
extends Overlay {
    private final Client client;
    private final SotetsegPlugin plugin;
    private final SotetsegConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    private int opacity = 5;

    @Inject
    private InvisibleBallOverlay(Client client, SotetsegPlugin plugin, SotetsegConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.setPriority(OverlayPriority.LOW);
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    public Dimension render(Graphics2D graphics) {
        this.panelComponent.getChildren().clear();
        if (this.config.warnBall() && this.plugin.invisibleTicks > 0 && (this.plugin.isInUnderWorld() || this.plugin.isInOverWorld())) {
            this.opacity = this.opacity >= 80 ? 5 : (this.opacity += 3);
            this.panelComponent.setBackgroundColor(new Color(255, 0, 0, this.opacity));
            this.panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("INVISIBLE") + 10, 0));
            this.panelComponent.getChildren().add(LineComponent.builder().left("INVISIBLE").build());
            return this.panelComponent.render(graphics);
        }
        return null;
    }
}

