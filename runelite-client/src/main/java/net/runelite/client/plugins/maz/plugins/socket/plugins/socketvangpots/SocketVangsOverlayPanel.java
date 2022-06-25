/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.components.LineComponent
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketvangpots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

public class SocketVangsOverlayPanel
extends OverlayPanel {
    private final Client client;
    private final SocketVangPotsPlugin plugin;
    private final SocketVangPotsConfig config;

    @Inject
    private SocketVangsOverlayPanel(Client client, SocketVangPotsPlugin plugin, SocketVangPotsConfig config) {
        super((Plugin)plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setPriority(OverlayPriority.MED);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.showPanel() && this.plugin.overloadsDropped > 0) {
            WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
            int plane = this.client.getPlane();
            int x = wp.getX() - this.client.getBaseX();
            int y = wp.getY() - this.client.getBaseY();
            int type = CoxUtil.getroom_type(this.client.getInstanceTemplateChunks()[plane][x / 8][y / 8]);
            if (type == 6 || type == 3) {
                this.panelComponent.getChildren().clear();
                this.panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("Overloads: ") + 20, 0));
                this.panelComponent.getChildren().add(LineComponent.builder().leftColor(Color.WHITE).left("Overloads: ").right(String.valueOf(this.plugin.overloadsDropped)).build());
            }
        }
        return super.render(graphics);
    }
}

