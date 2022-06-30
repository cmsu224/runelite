package net.runelite.client.plugins.maz.plugins.socket.plugins.deathindicators;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class DeathIndicatorsOverlay extends Overlay {
    private final DeathIndicatorsConfig config;
    private final DeathIndicatorsPlugin plugin;

    @Inject
    public DeathIndicatorsOverlay(DeathIndicatorsPlugin plugin, DeathIndicatorsConfig config) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.plugin = plugin;
        this.config = config;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.showOutline()) {
            Iterator var2 = this.plugin.getDeadNylos().iterator();

            while(var2.hasNext()) {
                NPC n = (NPC)var2.next();
                Shape objectClickbox = n.getConvexHull();
                this.renderPoly(graphics, Color.red, objectClickbox);
            }
        }

        return null;
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(2.0F));
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            graphics.fill(polygon);
        }

    }
}
