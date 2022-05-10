/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileObject
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.FontManager
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.socket.plugins.socketicedemon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonConfig;
import net.runelite.client.plugins.socket.plugins.socketicedemon.SocketIceDemonPlugin;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SocketIceDemonOverlay
extends OverlayPanel {
    private SocketIceDemonPlugin plugin;
    private SocketIceDemonConfig config;
    private Client client;

    @Inject
    public SocketIceDemonOverlay(SocketIceDemonPlugin plugin, SocketIceDemonConfig config, Client client) {
        super((Plugin)plugin);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.highlightUnlitBrazier() && this.plugin.unlitBrazierList.size() > 0 && this.plugin.roomtype == 12 && !this.plugin.iceDemonActive) {
            this.renderTileObjects(graphics);
        }
        if (this.config.iceDemonSpawnTicks() && this.plugin.iceDemon != null && this.plugin.iceDemonActivateTicks > 0 && this.plugin.iceDemonActive && this.plugin.roomtype == 12) {
            String text = String.valueOf(this.plugin.iceDemonActivateTicks);
            Point textLoc = this.plugin.iceDemon.getCanvasTextLocation(graphics, text, 50);
            Font oldFont = graphics.getFont();
            graphics.setFont(FontManager.getRunescapeBoldFont());
            Point pointShadow = new Point(textLoc.getX() + 1, textLoc.getY() + 1);
            OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)pointShadow, (String)text, (Color)Color.BLACK);
            OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)textLoc, (String)text, (Color)Color.RED);
            graphics.setFont(oldFont);
        }
        if (this.config.iceDemonHp() && this.plugin.iceDemon != null && !this.plugin.iceDemonActive && this.plugin.roomtype == 12) {
            String str;
            Font oldFont = graphics.getFont();
            graphics.setFont(FontManager.getRunescapeBoldFont());
            Color textColor = Color.WHITE;
            float floatRatio = 0.0f;
            if (this.client.getVarbitValue(5424) == 1) {
                floatRatio = (float)this.plugin.iceDemon.getHealthRatio() / (float)this.plugin.iceDemon.getHealthScale() * 100.0f;
                textColor = floatRatio > 75.0f ? Color.GREEN : (floatRatio > 25.0f ? Color.YELLOW : Color.RED);
                String text = Float.toString(floatRatio);
                str = text.substring(0, text.indexOf(".")) + "%";
            } else {
                textColor = this.plugin.iceDemon.getHealthRatio() > 75 ? Color.GREEN : (this.plugin.iceDemon.getHealthRatio() > 25 ? Color.YELLOW : Color.RED);
                str = this.plugin.iceDemon.getHealthRatio() + "%";
            }
            Point point = this.plugin.iceDemon.getCanvasTextLocation(graphics, str, this.plugin.iceDemon.getLogicalHeight());
            if (point == null) {
                return null;
            }
            point = new Point(point.getX(), point.getY() + 20);
            OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)point, (String)str, (Color)textColor);
            graphics.setFont(oldFont);
        }
        return super.render(graphics);
    }

    private void renderTileObjects(Graphics2D graphics) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = this.client.getPlane();
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                Player player;
                Tile tile = tiles[z][x][y];
                if (tile == null || (player = this.client.getLocalPlayer()) == null) continue;
                this.renderGameObjects(graphics, tile, player);
            }
        }
    }

    private void renderGameObjects(Graphics2D graphics, Tile tile, Player player) {
        GameObject[] gameObjects = tile.getGameObjects();
        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject == null || !gameObject.getSceneMinLocation().equals((Object)tile.getSceneLocation()) || gameObject.getId() != 29747) continue;
                this.renderTileObject(graphics, (TileObject)gameObject, player, this.config.highlightBrazierColor());
            }
        }
    }

    private void renderTileObject(Graphics2D graphics, TileObject tileObject, Player player, Color color) {
        if (tileObject != null && player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) <= 2400) {
            Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.config.highlightBrazierOpacity());
            OverlayUtil.renderHoverableArea((Graphics2D)graphics, (Shape)tileObject.getClickbox(), (Point)this.client.getMouseCanvasPosition(), (Color)fillColor, (Color)color, (Color)color.darker());
        }
    }
}

