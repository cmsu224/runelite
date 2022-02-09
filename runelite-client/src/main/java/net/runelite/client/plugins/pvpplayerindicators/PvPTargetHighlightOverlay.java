//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.pvpplayerindicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.pvpplayerindicators.TargetHighlightMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
public class PvPTargetHighlightOverlay extends Overlay {
    private final PvPPlayerIndicatorsTargetService playerIndicatorsTargetService;
    private final PvPPlayerIndicatorsPlugin playerIndicatorsPlugin;
    private final PvPPlayerIndicatorsConfig config;
    @Inject
    private Client client;

    @Inject
    private PvPTargetHighlightOverlay(PvPPlayerIndicatorsConfig config, PvPPlayerIndicatorsTargetService playerIndicatorsTargetService, PvPPlayerIndicatorsPlugin plugin) {
        this.config = config;
        this.playerIndicatorsTargetService = playerIndicatorsTargetService;
        this.playerIndicatorsPlugin = plugin;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.MED);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.highlightTargets() != TargetHighlightMode.OFF) {
            this.playerIndicatorsTargetService.forEachPlayer((player, color) -> {
                if (this.config.highlightTargets() == TargetHighlightMode.TILE) {
                    renderPolygon(graphics, player.getCanvasTilePoly(), color);
                } else if (this.config.highlightTargets() == TargetHighlightMode.HULL) {
                    renderPolygon(graphics, player.getConvexHull(), this.config.getTargetColor());
                } else if (this.config.highlightTargets() == TargetHighlightMode.TRUE_LOCATION) {
                    WorldPoint playerPos = player.getWorldLocation();
                    if (playerPos != null) {
                        LocalPoint playerPosLocal = LocalPoint.fromWorld(this.client, playerPos);
                        if (playerPosLocal != null) {
                            this.renderTile(graphics, playerPosLocal, color);
                        }
                    }
                }

            });
        }

        return null;
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
