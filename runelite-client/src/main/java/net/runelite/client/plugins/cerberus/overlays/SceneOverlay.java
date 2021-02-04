//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cerberus.CerberusConfig;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.domain.Arena;
import net.runelite.client.plugins.cerberus.domain.Cerberus;
import net.runelite.client.plugins.cerberus.domain.Ghost;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public final class SceneOverlay extends Overlay {
    private static final int MAX_RENDER_DISTANCE = 32;
    private static final int GHOST_TIME_WARNING = 2;
    private static final int GHOST_TIME_FONT_SIZE = 12;
    private static final int GHOST_TILE_OUTLINE_WIDTH = 2;
    private static final int GHOST_TILE_OUTLINE_APLHA = 255;
    private static final int GHOST_TILE_FILL_APLHA = 20;
    private static final int GHOST_YELL_TICK_WINDOW = 17;
    private final Client client;
    private final CerberusPlugin plugin;
    private final CerberusConfig config;
    private Cerberus cerberus;

    @Inject
    SceneOverlay(Client client, CerberusPlugin plugin, CerberusConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPriority(OverlayPriority.HIGHEST);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.determineLayer();
    }

    public Dimension render(Graphics2D graphics2D) {
        this.cerberus = this.plugin.getCerberus();
        if (this.cerberus == null) {
            return null;
        } else {
            this.renderGhostTiles(graphics2D);
            return null;
        }
    }

    private void renderGhostTiles(Graphics2D graphics2D) {
        if (this.config.drawGhostTiles() && this.cerberus.getLastGhostYellTick() != 0 && this.plugin.getGameTick() - this.cerberus.getLastGhostYellTick() < 17) {
            Player player = this.client.getLocalPlayer();
            if (player != null) {
                WorldPoint playerTile = player.getWorldLocation();
                Arena arena = Arena.getArena(playerTile);
                if (arena != null) {
                    int numberOfTiles = true;

                    for(int i = 0; i < 3; ++i) {
                        WorldPoint ghostTile = arena.getGhostTile(i);
                        if (ghostTile == null || ghostTile.distanceTo(playerTile) >= 32) {
                            return;
                        }

                        this.renderGhostTileOutline(graphics2D, ghostTile, playerTile, i);
                        this.renderGhostTileAttackTime(graphics2D, ghostTile, i);
                    }

                }
            }
        }
    }

    private void renderGhostTileOutline(Graphics2D graphics2D, WorldPoint ghostTile, WorldPoint playerPoint, int tileIndex) {
        Color ghostTileFillColor = Color.WHITE;
        List<NPC> ghosts = this.plugin.getGhosts();
        if (tileIndex < ghosts.size()) {
            Ghost ghost = Ghost.fromNPC((NPC)ghosts.get(tileIndex));
            if (ghost != null) {
                ghostTileFillColor = ghost.getColor();
            }
        }

        OverlayUtil.drawTiles(graphics2D, this.client, ghostTile, playerPoint, ghostTileFillColor, 2, 255, 20);
    }

    private void renderGhostTileAttackTime(Graphics2D graphics2D, WorldPoint ghostTile, int tileIndex) {
        LocalPoint localPoint = LocalPoint.fromWorld(this.client, ghostTile);
        if (localPoint != null) {
            Polygon polygon = Perspective.getCanvasTilePoly(this.client, localPoint);
            if (polygon != null) {
                long time = System.currentTimeMillis();
                int tick = this.plugin.getGameTick();
                int lastGhostsTick = this.cerberus.getLastGhostYellTick();
                long lastGhostsTime = Math.min(this.cerberus.getLastGhostYellTime(), time - (long)(600 * (tick - lastGhostsTick)));
                this.cerberus.setLastGhostYellTime(lastGhostsTime);
                double timeUntilGhostAttack = Math.max((double)(lastGhostsTime + (long)(600 * (13 + tileIndex * 2)) - System.currentTimeMillis()) / 1000.0D, 0.0D);
                Color textColor = timeUntilGhostAttack <= 2.0D ? Color.RED : Color.WHITE;
                String timeUntilAttack = String.format("%.1f", timeUntilGhostAttack);
                graphics2D.setFont(new Font("Arial", 0, 12));
                FontMetrics metrics = graphics2D.getFontMetrics();
                Point centerPoint = this.getRectangleCenterPoint(polygon.getBounds());
                Point newPoint = new Point(centerPoint.getX() - metrics.stringWidth(timeUntilAttack) / 2, centerPoint.getY() + metrics.getHeight() / 2);
                OverlayUtil.renderTextLocation(graphics2D, timeUntilAttack, 12, 0, textColor, newPoint, true, 0);
            }
        }
    }

    private Point getRectangleCenterPoint(Rectangle rect) {
        int x = (int)(rect.getX() + rect.getWidth() / 2.0D);
        int y = (int)(rect.getY() + rect.getHeight() / 2.0D);
        return new Point(x, y);
    }

    public void determineLayer() {
        this.setLayer(this.config.mirrorMode() ? OverlayLayer.AFTER_MIRROR : OverlayLayer.ABOVE_SCENE);
    }
}
