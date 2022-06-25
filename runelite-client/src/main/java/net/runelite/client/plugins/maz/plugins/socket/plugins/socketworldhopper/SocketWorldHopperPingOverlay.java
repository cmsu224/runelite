/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Point
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

class SocketWorldHopperPingOverlay
extends Overlay {
    private static final int Y_OFFSET = 11;
    private static final int X_OFFSET = 1;
    private final Client client;
    private final SocketWorldHopperPlugin worldHopperPlugin;
    private final SocketWorldHopperConfig worldHopperConfig;

    @Inject
    private SocketWorldHopperPingOverlay(Client client, SocketWorldHopperPlugin worldHopperPlugin, SocketWorldHopperConfig worldHopperConfig) {
        this.client = client;
        this.worldHopperPlugin = worldHopperPlugin;
        this.worldHopperConfig = worldHopperConfig;
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPriority(OverlayPriority.HIGH);
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    public Dimension render(Graphics2D graphics) {
        if (!this.worldHopperConfig.displayPing()) {
            return null;
        }
        int ping = this.worldHopperPlugin.getCurrentPing();
        if (ping < 0) {
            return null;
        }
        String text = ping + " ms";
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        int textHeight = graphics.getFontMetrics().getAscent() - graphics.getFontMetrics().getDescent();
        Widget logoutButton = this.client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_LOGOUT_BUTTON);
        int xOffset = 1;
        if (logoutButton != null && !logoutButton.isHidden()) {
            xOffset += logoutButton.getWidth();
        }
        int width = (int)this.client.getRealDimensions().getWidth();
        Point point = new Point(width - textWidth - xOffset, textHeight + 11);
        OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)point, (String)text, (Color)Color.YELLOW);
        return null;
    }
}

