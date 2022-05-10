/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.socket.plugins.sotetseg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.socket.plugins.sotetseg.SotetsegConfig;
import net.runelite.client.plugins.socket.plugins.sotetseg.SotetsegPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SotetsegOverlay
extends Overlay {
    private final Client client;
    private final SotetsegPlugin plugin;
    private final SotetsegConfig config;
    private int flashTimeout;
    private int chosenTextTimeout;

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
        if (this.plugin.isSotetsegActive() || this.config.showTestOverlay()) {
            Set<WorldPoint> tiles;
            if (this.config.showTestOverlay()) {
                tiles = new HashSet();
                for (int i = 0; i < 5; ++i) {
                    try {
                        WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
                        WorldPoint wp = new WorldPoint(worldPoint.getX(), worldPoint.getY() + i, worldPoint.getPlane());
                        tiles.add(wp);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            } else {
                tiles = this.plugin.getMazePings();
            }
            for (WorldPoint worldPoint : tiles) {
                Polygon poly;
                LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)worldPoint);
                if (localPoint == null || (poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)localPoint)) == null || this.config.streamerMode()) continue;
                int outlineAlpha = this.config.getTileOutlineSize() > 0.0 ? 255 : 0;
                if (this.config.antiAlias()) {
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                } else {
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                }
                Color color = new Color(this.config.getTileOutline().getRed(), this.config.getTileOutline().getGreen(), this.config.getTileOutline().getBlue(), outlineAlpha);
                graphics.setColor(color);
                Stroke originalStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke((float)this.config.getTileOutlineSize()));
                graphics.draw(poly);
                Color fill = this.config.getTileColor();
                int alpha = Math.min(Math.max(this.config.getTileTransparency(), 0), 255);
                Color realFill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), alpha);
                graphics.setColor(realFill);
                graphics.fill(poly);
                graphics.setStroke(originalStroke);
            }
        }
        if (this.plugin.isSotetsegActive()) {
            if (this.config.solveMaze()) {
                if (this.plugin.isInUnderWorld()) {
                    this.drawPoints(graphics, this.plugin.getSolvedRedTiles(), this.plugin.getMazeSolvedIndex());
                    this.drawPoints(graphics, this.plugin.getMazeSolved(), this.plugin.getMazeSolvedIndex());
                } else {
                    this.drawPoints(graphics, this.plugin.getMazeSolved(), this.plugin.getMazeSolvedIndex());
                }
            }
            if (this.plugin.isMazeActive() && this.config.showSotetsegInstanceTimer()) {
                try {
                    String text2 = String.valueOf(this.plugin.getInstanceTime());
                    int width = graphics.getFontMetrics().stringWidth(text2);
                    Point point = Perspective.localToCanvas((Client)this.client, (LocalPoint)this.plugin.getSotetsegNPC().getLocalLocation(), (int)this.client.getPlane(), (int)this.plugin.getSotetsegNPC().getLogicalHeight());
                    Point actual = new Point(point.getX() - width / 2, point.getY() + 100);
                    graphics.setFont(new Font("Arial", 1, this.config.getFontSizeInstanceTimer()));
                    OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)actual, (String)text2, (Color)this.config.sotetsegInstanceTimerColor());
                }
                catch (NullPointerException text2) {
                    // empty catch block
                }
            }
            if (this.plugin.isMazeActive() && this.config.showSotetsegInstanceTimerPlayer()) {
                try {
                    Point point;
                    String text3 = String.valueOf(this.plugin.getInstanceTime());
                    Player player = this.client.getLocalPlayer();
                    if (player != null && (point = player.getCanvasTextLocation(graphics, "#", player.getLogicalHeight() + 250)) != null) {
                        graphics.setFont(new Font("Arial", 1, this.config.getFontSizeInstanceTimer()));
                        OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)point, (String)text3, (Color)this.config.sotetsegInstanceTimerPlayerColor());
                    }
                }
                catch (NullPointerException text3) {
                    // empty catch block
                }
            }
            if (this.plugin.flashScreen && this.config.flashScreen()) {
                Color originalColor = graphics.getColor();
                graphics.setColor(new Color(255, 0, 0, 70));
                graphics.fill(this.client.getCanvas().getBounds());
                graphics.setColor(originalColor);
                if (++this.flashTimeout >= 15) {
                    this.flashTimeout = 0;
                    this.plugin.flashScreen = false;
                }
            }
            if (this.plugin.chosenTextScreen && this.config.isChosenText() && this.config.hideScreenFlash()) {
                String text = this.config.customChosenText();
                graphics.setFont(new Font("Arial", 1, 20));
                int width = graphics.getFontMetrics().stringWidth(text);
                int n = this.client.getViewportWidth() / 2 - width / 2;
                int drawY = this.client.getViewportHeight() - this.client.getViewportHeight() / 2 - 12;
                OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)new Point(n, drawY), (String)text, (Color)Color.WHITE);
                if (++this.chosenTextTimeout >= this.config.chosenTextDuration() * 50) {
                    this.chosenTextTimeout = 0;
                    this.plugin.chosenTextScreen = false;
                }
            }
        }
        return null;
    }

    private void drawPoints(Graphics2D graphics, ArrayList<Point> points, int index) {
        WorldPoint player = Objects.requireNonNull(this.client.getLocalPlayer()).getWorldLocation();
        IntStream.range(0, points.size()).forEach(i -> {
            Point p = (Point)points.get(i);
            WorldPoint wp = WorldPoint.fromRegion((int)player.getRegionID(), (int)p.getX(), (int)p.getY(), (int)player.getPlane());
            if (this.plugin.isInUnderWorld()) {
                wp = WorldPoint.fromRegion((int)player.getRegionID(), (int)(p.getX() + 42 - 9), (int)(p.getY() + 31 - 22), (int)player.getPlane());
            }
            LocalPoint lp = LocalPoint.fromWorld((Client)this.client, (WorldPoint)wp);
            if (this.config.numbersOn() && index < i) {
                try {
                    Point textPoint = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)lp, (String)String.valueOf(i), (int)0);
                    Point canvasCenterPoint = new Point(textPoint.getX(), (int)((double)textPoint.getY() + Math.floor((double)this.config.getFontSize() / 2.0)));
                    OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)canvasCenterPoint, (String)String.valueOf(i), (Color)Color.WHITE);
                }
                catch (NullPointerException textPoint) {
                    // empty catch block
                }
            }
            if (this.config.highlightTiles() && index < i) {
                try {
                    Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)lp);
                    if (poly != null) {
                        Color colorTile = this.config.getHighlightTileOutline();
                        int outlineAlpha = this.config.solvedTileWidth() > 0.0 ? 255 : 0;
                        if (this.config.antiAlias()) {
                            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        } else {
                            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        }
                        graphics.setColor(new Color(colorTile.getRed(), colorTile.getGreen(), colorTile.getBlue(), outlineAlpha));
                        Stroke originalStroke = graphics.getStroke();
                        double strokeSize = this.config.solvedTileWidth();
                        graphics.setStroke(new BasicStroke((float)strokeSize));
                        if (i < points.size()) {
                            graphics.draw(poly);
                            int alpha = Math.min(Math.max(this.config.solvedTileOpacity(), 0), 255);
                            Color realFill = new Color(colorTile.getRed(), colorTile.getGreen(), colorTile.getBlue(), alpha);
                            graphics.setColor(realFill);
                            graphics.fill(poly);
                            graphics.setStroke(originalStroke);
                        }
                    }
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
        });
    }
}

