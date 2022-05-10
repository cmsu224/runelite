/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.widgets.WidgetItem
 *  net.runelite.client.game.ItemManager
 *  net.runelite.client.ui.overlay.WidgetItemOverlay
 */
package net.runelite.client.plugins.socket.plugins.socketba;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAConfig;
import net.runelite.client.plugins.socket.plugins.socketba.SocketBAPlugin;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

public class SocketBAItemOverlay
extends WidgetItemOverlay {
    private final Client client;
    private final ItemManager itemManager;
    private final SocketBAPlugin plugin;
    private final SocketBAConfig config;

    @Inject
    public SocketBAItemOverlay(Client client, ItemManager itemManager, SocketBAPlugin plugin, SocketBAConfig config) {
        this.client = client;
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.config = config;
        this.showOnInterfaces(new int[]{164, 149});
    }

    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget) {
        if (this.client.getVarbitValue(3923) == 1 && this.config.correctItemHighlight() != SocketBAConfig.correctItemHighlightMode.OFF) {
            boolean highlight = false;
            if (this.plugin.role.equals("Defender")) {
                if (itemId == 10513 && this.plugin.defCall.equals("Crackers") || itemId == 10514 && this.plugin.defCall.equals("Tofu") || itemId == 10515 && this.plugin.defCall.equals("Worms")) {
                    highlight = true;
                }
            } else if (this.plugin.role.equals("Attacker")) {
                if (this.plugin.attCall.toLowerCase().contains("aggressive") && (itemId == 22229 || this.config.meleeSpecHighlight() && itemId == 23987) || this.plugin.attCall.toLowerCase().contains("accurate") && (itemId == 22228 || this.config.meleeSpecHighlight() && itemId == 21015) || this.plugin.attCall.toLowerCase().contains("controlled") && (itemId == 22227 || this.config.meleeSpecHighlight() && itemId == 23987) || this.plugin.attCall.toLowerCase().contains("defensive") && (itemId == 22230 || this.config.meleeSpecHighlight() && itemId == 23987)) {
                    highlight = true;
                }
            } else if (this.plugin.role.equals("Healer") && (itemId == 10539 && this.plugin.healCall.contains("Tofu") || itemId == 10540 && this.plugin.healCall.contains("Worms") || itemId == 10541 && this.plugin.healCall.contains("Meat"))) {
                highlight = true;
            }
            if (highlight) {
                if (this.config.correctItemHighlight() == SocketBAConfig.correctItemHighlightMode.OUTLINE) {
                    this.highlightItem(graphics, itemId, itemWidget, this.config.correctItemColor());
                } else if (this.config.correctItemHighlight() == SocketBAConfig.correctItemHighlightMode.UNDERLINE) {
                    this.underlineItem(graphics, itemId, itemWidget, this.config.correctItemColor());
                } else if (this.config.correctItemHighlight() == SocketBAConfig.correctItemHighlightMode.BOX) {
                    this.drawBox(graphics, itemWidget.getCanvasLocation().getX(), itemWidget.getCanvasLocation().getY(), itemWidget.getCanvasBounds().height, itemWidget.getCanvasBounds().width);
                }
            }
        }
    }

    private void highlightItem(Graphics2D graphics, int itemId, WidgetItem itemWidget, Color color) {
        Rectangle bounds = itemWidget.getCanvasBounds();
        BufferedImage outline = this.itemManager.getItemOutline(itemId, itemWidget.getQuantity(), color);
        graphics.drawImage((Image)outline, (int)bounds.getX(), (int)bounds.getY(), null);
    }

    private void underlineItem(Graphics2D graphics, int itemId, WidgetItem itemWidget, Color color) {
        Rectangle bounds = itemWidget.getCanvasBounds();
        int heightOffSet = (int)bounds.getY() + (int)bounds.getHeight() + 2;
        graphics.setColor(color);
        graphics.drawLine((int)bounds.getX(), heightOffSet, (int)bounds.getX() + (int)bounds.getWidth(), heightOffSet);
    }

    private void drawBox(Graphics2D graphics, int startX, int startY, int height, int width) {
        graphics.setColor(this.config.meleeStyleHighlightColor());
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.drawLine(startX, startY, startX + width, startY);
        graphics.drawLine(startX + width, startY, startX + width, startY + height);
        graphics.drawLine(startX + width, startY + height, startX, startY + height);
        graphics.drawLine(startX, startY + height, startX, startY);
    }
}

