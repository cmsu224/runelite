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
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.OverlayUtil
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.specialcounterextended;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SpecialCounterOverlay
extends Overlay {
    private final Client client;
    private final SpecialCounterExtendedPlugin plugin;
    private final SpecialCounterExtendedConfig config;
    private Map<String, ArrayList<SpecialIcon>> drawings = new HashMap<String, ArrayList<SpecialIcon>>();

    @Inject
    private SpecialCounterOverlay(Client client, SpecialCounterExtendedPlugin plugin, SpecialCounterExtendedConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    public void addOverlay(String player, SpecialIcon icon) {
        if (this.config.showHitOverlay()) {
            ArrayList<SpecialIcon> icons = new ArrayList<SpecialIcon>();
            if (this.drawings.containsKey(player)) {
                icons = this.drawings.get(player);
            }
            icons.add(icon);
            this.drawings.put(player, icons);
        }
    }

    public Dimension render(Graphics2D graphics) {
        ArrayList<String> removePlayers = new ArrayList<String>();
        HashMap<String, LocalPoint> locations = new HashMap<String, LocalPoint>();
        for (Player player : this.client.getPlayers()) {
            locations.put(player.getName(), player.getLocalLocation());
        }
        for (String playerName : this.drawings.keySet()) {
            LocalPoint center = (LocalPoint)locations.get(playerName);
            if (center != null) {
                ArrayList<SpecialIcon> icons = this.drawings.get(playerName);
                ArrayList<SpecialIcon> removeIcons = new ArrayList<SpecialIcon>();
                int currentHeight = 200;
                for (int i = icons.size() - 1; i >= 0; --i) {
                    SpecialIcon icon = icons.get(i);
                    long elapsedTime = System.currentTimeMillis() - icon.getStartTime();
                    int fadeDelay = Math.max(this.config.getFadeDelay(), 1);
                    long timeRemaining = (long)fadeDelay - elapsedTime;
                    if (timeRemaining <= 0L) {
                        removeIcons.add(icon);
                        continue;
                    }
                    float opacity = (float)timeRemaining / (float)fadeDelay;
                    float thresh = Math.min(opacity + 0.2f, 1.0f);
                    graphics.setComposite(AlphaComposite.getInstance(3, thresh));
                    int maxHeight = Math.max(this.config.getMaxHeight(), 1);
                    int updatedHeight = maxHeight - (int)((float)maxHeight * thresh);
                    Point drawPoint = Perspective.getCanvasImageLocation((Client)this.client, (LocalPoint)center, (BufferedImage)icon.getImage(), (int)(currentHeight + updatedHeight));
                    graphics.drawImage((Image)icon.getImage(), drawPoint.getX(), drawPoint.getY(), null);
                    if (icon.getText() != null) {
                        Point textPoint = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)center, (String)icon.getText(), (int)(currentHeight + updatedHeight));
                        graphics.setFont(new Font("Arial", 1, 16));
                        Point canvasCenterPoint = new Point(textPoint.getX(), textPoint.getY());
                        Point canvasCenterPointShadow = new Point(textPoint.getX() + 1, textPoint.getY() + 1);
                        OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)canvasCenterPointShadow, (String)icon.getText(), (Color)Color.BLACK);
                        OverlayUtil.renderTextLocation((Graphics2D)graphics, (Point)canvasCenterPoint, (String)icon.getText(), (Color)Color.WHITE);
                    }
                    currentHeight += icon.getImage().getHeight() * 2;
                }
                for (SpecialIcon icon : removeIcons) {
                    icons.remove(icon);
                }
                if (!icons.isEmpty()) continue;
                removePlayers.add(playerName);
                continue;
            }
            removePlayers.add(playerName);
        }
        for (String playerName : removePlayers) {
            this.drawings.remove(playerName);
        }
        return null;
    }
}

