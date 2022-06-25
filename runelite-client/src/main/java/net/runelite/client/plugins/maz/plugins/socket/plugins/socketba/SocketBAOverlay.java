/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.ItemLayer
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.Renderable
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 *  net.runelite.api.TileObject
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.widgets.Widget
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketba;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SocketBAOverlay
extends OverlayPanel {
    private SocketBAPlugin plugin;
    private SocketBAConfig config;
    private Client client;

    @Inject
    public SocketBAOverlay(SocketBAPlugin plugin, SocketBAConfig config, Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.client.getVarbitValue(3923) == 1) {
            AttackStyle[] styleList;
            if (this.config.highlightRoleNpcs()) {
                for (NPC npc : this.client.getNpcs()) {
                    Polygon tilePoly;
                    NPCComposition npcComposition = npc.getComposition();
                    if (npc.getName() == null || !npc.getName().toLowerCase().contains("penance") || npcComposition == null) continue;
                    String name = npc.getName().toLowerCase();
                    if ((!this.plugin.role.equals("Attacker") || !name.contains("fighter") && !name.contains("ranger")) && (!this.plugin.role.equals("Defender") || !name.contains("runner")) && (!this.plugin.role.equals("Healer") || !name.contains("healer"))) continue;
                    int size = npcComposition.getSize();
                    LocalPoint lp = npc.getLocalLocation();
                    if (lp == null || (tilePoly = Perspective.getCanvasTileAreaPoly((Client)this.client, (LocalPoint)lp, (int)size)) == null) continue;
                    Color outlineColor = new Color(this.config.highlightRoleNpcsColor().getRed(), this.config.highlightRoleNpcsColor().getGreen(), this.config.highlightRoleNpcsColor().getBlue(), 255);
                    Color fillColor = this.config.highlightRoleNpcsColor();
                    this.renderPoly(graphics, outlineColor, fillColor, tilePoly, 1.0);
                }
            }
            if (this.config.meleeStyleHighlight() && this.plugin.equippedWeaponTypeVarbit != -1 && this.plugin.role.equals("Attacker") && (styleList = WeaponType.getWeaponType(this.plugin.equippedWeaponTypeVarbit).getAttackStyles()) != null) {
                int index = 1;
                for (AttackStyle aStyle : styleList) {
                    Widget widget;
                    if (aStyle != null && this.plugin.attCall.contains(aStyle.getName()) && (widget = index == 1 ? this.client.getWidget(38862852) : (index == 2 ? this.client.getWidget(38862856) : (index == 3 ? this.client.getWidget(38862860) : this.client.getWidget(38862864)))) != null && !widget.isHidden()) {
                        this.drawBox(graphics, widget.getCanvasLocation().getX(), widget.getCanvasLocation().getY(), widget.getBounds().height, widget.getBounds().width);
                    }
                    ++index;
                }
            }
            if (this.config.highlightEggs()) {
                this.renderTileObjects(graphics);
            }
            if (this.config.highlightVendingMachine()) {
                for (GameObject obj : this.plugin.vendingMachines) {
                    if (obj.getConvexHull() == null || !(this.plugin.role.equals("Attacker") && obj.getId() == 20241 || this.plugin.role.equals("Defender") && obj.getId() == 20242) && (!this.plugin.role.equals("Healer") || obj.getId() != 20243)) continue;
                    graphics.setColor(this.config.vendingMachineColor());
                    graphics.draw(obj.getConvexHull());
                    graphics.setColor(new Color(this.config.vendingMachineColor().getRed(), this.config.vendingMachineColor().getGreen(), this.config.vendingMachineColor().getBlue(), this.config.vendingMachineOpacity()));
                    graphics.fill(obj.getConvexHull());
                }
            }
            if (this.config.cannonHelper()) {
                this.plugin.cannons.forEach((n, c) -> {
                    if (n != null && n.getConvexHull() != null) {
                        graphics.setColor((Color)c);
                        graphics.setStroke(new BasicStroke((float)this.plugin.cannonWidth));
                        graphics.draw(n.getConvexHull());
                    }
                });
                this.plugin.eggHoppers.forEach((o, c) -> {
                    if (o != null && o.getConvexHull() != null) {
                        graphics.setColor((Color)c);
                        graphics.setStroke(new BasicStroke((float)this.plugin.cannonWidth));
                        graphics.draw(o.getConvexHull());
                    }
                });
            }
            if (this.config.discoQueen() && this.plugin.queen != null) {
                this.plugin.discoTiles.forEach((o, c) -> {
                    if (o != null) {
                        Color fillColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
                        this.renderPoly(graphics, (Color)c, fillColor, o.getCanvasTilePoly(), 2.0);
                    }
                });
            }
        }
        return super.render(graphics);
    }

    private void drawBox(Graphics2D graphics, int startX, int startY, int height, int width) {
        graphics.setColor(this.config.meleeStyleHighlightColor());
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawLine(startX, startY, startX + width, startY);
        graphics.drawLine(startX + width, startY, startX + width, startY + height);
        graphics.drawLine(startX + width, startY + height, startX, startY + height);
        graphics.drawLine(startX, startY + height, startX, startY);
    }

    private void renderPoly(Graphics2D graphics, Color outlineColor, Color fillColor, Shape polygon, double width) {
        if (polygon != null) {
            graphics.setColor(outlineColor);
            graphics.setStroke(new BasicStroke((float)width));
            graphics.draw(polygon);
            graphics.setColor(fillColor);
            graphics.fill(polygon);
        }
    }

    private void renderTileObjects(Graphics2D graphics) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = this.client.getPlane();
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                Tile tile = tiles[z][x][y];
                if (tile == null || this.client.getLocalPlayer() == null) continue;
                this.renderGroundItems(graphics, tile, this.client.getLocalPlayer());
            }
        }
    }

    private void renderGroundItems(Graphics2D graphics, Tile tile, Player player) {
        ItemLayer itemLayer = tile.getItemLayer();
        if (itemLayer != null && player.getLocalLocation().distanceTo(itemLayer.getLocalLocation()) <= 2400) {
            Renderable current = itemLayer.getBottom();
            while (current instanceof TileItem) {
                TileItem item = (TileItem)current;
                Color color = Color.WHITE;
                String text = "";
                int id = item.getId();
                if (id == 10534) {
                    color = Color.YELLOW;
                    text = "Yellow egg";
                } else if (this.plugin.colCall.equalsIgnoreCase("Green eggs") && id == 10531) {
                    color = Color.GREEN;
                    text = "Green egg";
                } else if (this.plugin.colCall.equalsIgnoreCase("Red eggs") && id == 10532) {
                    color = Color.RED;
                    text = "Red egg";
                } else if (this.plugin.colCall.equalsIgnoreCase("Blue eggs") && id == 10533) {
                    color = Color.BLUE;
                    text = "Blue egg";
                }
                if (color != Color.WHITE) {
                    OverlayUtil.renderTileOverlay((Graphics2D)graphics, (TileObject)itemLayer, (String)text, (Color)color);
                }
                current = (Renderable) current.getNext();
            }
        }
    }
}

