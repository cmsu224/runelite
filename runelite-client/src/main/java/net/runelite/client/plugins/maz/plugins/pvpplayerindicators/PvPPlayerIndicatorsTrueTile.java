//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class PvPPlayerIndicatorsTrueTile extends Overlay {
    private final PvPPlayerIndicatorsService playerIndicatorsService;
    private final PvPPlayerIndicatorsConfig config;
    private final Client client;

    @Inject
    private PvPPlayerIndicatorsTrueTile(PvPPlayerIndicatorsConfig config, PvPPlayerIndicatorsService playerIndicatorsService, Client client) {
        this.config = config;
        this.playerIndicatorsService = playerIndicatorsService;
        this.client = client;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.MED);
    }

    public Dimension render(Graphics2D graphics) {
        if (!this.config.drawTL()) {
            return null;
        } else {
            this.playerIndicatorsService.forEachPlayer((player, color) -> {
                WorldPoint playerPos = player.getWorldLocation();
                if (playerPos != null) {
                    LocalPoint playerPosLocal = LocalPoint.fromWorld(this.client, playerPos);
                    if (playerPosLocal != null) {
                        this.renderTile(graphics, playerPosLocal, color);
                    }
                }

            });
            return null;
        }
    }

    private void renderTile(Graphics2D graphics, LocalPoint dest, Color color) {
        if (dest != null) {
            Polygon poly = Perspective.getCanvasTilePoly(this.client, dest);
            if (poly != null) {
                renderPolygon(graphics, poly, color);
            }
        }
    }

    public static void renderPolygon(Graphics2D graphics, Shape poly, Color color) {
        graphics.setColor(color);
        Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2.0F));
        graphics.draw(poly);
        graphics.setColor(new Color(0, 0, 0, 0));
        graphics.fill(poly);
        graphics.setStroke(originalStroke);
    }
}
