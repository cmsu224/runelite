//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.sotetseg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class SotetsegOverlay extends Overlay {
    private final Client client;
    private final SotetsegPlugin plugin;
    private final SotetsegConfig config;

    @Inject
    private SotetsegOverlay(Client client, SotetsegPlugin plugin, SotetsegConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.plugin.isSotetsegActive()) {
            Iterator var2 = this.plugin.getMazePings().iterator();

            while(var2.hasNext()) {
                WorldPoint next = (WorldPoint)var2.next();
                LocalPoint localPoint = LocalPoint.fromWorld(this.client, next);
                if (localPoint != null) {
                    Polygon poly = Perspective.getCanvasTilePoly(this.client, localPoint);
                    if (poly != null) {
                        Color color = this.config.getTileOutline();
                        graphics.setColor(color);
                        Stroke originalStroke = graphics.getStroke();
                        int strokeSize = Math.max(this.config.getTileOutlineSize(), 1);
                        graphics.setStroke(new BasicStroke((float)strokeSize));
                        graphics.draw(poly);
                        Color fill = this.config.getTileColor();
                        int alpha = Math.min(Math.max(this.config.getTileTransparency(), 0), 255);
                        Color realFill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), alpha);
                        graphics.setColor(realFill);
                        graphics.fill(poly);
                        graphics.setStroke(originalStroke);
                    }
                }
            }
        }

        return null;
    }
}
