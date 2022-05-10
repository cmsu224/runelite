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
 *  net.runelite.client.game.ItemManager
 *  net.runelite.client.game.SpriteManager
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 */
package net.runelite.client.plugins.socket.plugins.playerstatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatus;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusConfig;
import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusPlugin;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameTimer;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.AbstractMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.IndicatorMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.TimerMarker;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class PlayerStatusOverlay
extends Overlay {
    private final Client client;
    private final PlayerStatusPlugin plugin;
    private final PlayerStatusConfig config;
    private final ItemManager itemManager;
    private final SpriteManager spriteManager;

    @Inject
    public PlayerStatusOverlay(Client client, PlayerStatusPlugin plugin, PlayerStatusConfig config, ItemManager itemManager, SpriteManager spriteManager) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    private boolean ignoreMarker(AbstractMarker marker) {
        if (marker == null) {
            return true;
        }
        if (marker instanceof IndicatorMarker) {
            GameIndicator indicator = ((IndicatorMarker)marker).getIndicator();
            switch (indicator) {
                case VENGEANCE_ACTIVE: {
                    return this.config.showVengeanceActive() == PlayerStatusConfig.vengeMode.OFF;
                }
                case SPEC_XFER: {
                    return this.config.showSpecXfer() == PlayerStatusConfig.xferIconMode.OFF;
                }
            }
            return true;
        }
        if (marker instanceof TimerMarker) {
            GameTimer timer = ((TimerMarker)marker).getTimer();
            switch (timer) {
                case VENGEANCE: {
                    return !this.config.showVengeanceCooldown();
                }
                case IMBUED_HEART: {
                    return !this.config.showImbuedHeart();
                }
                case OVERLOAD: 
                case OVERLOAD_RAID: {
                    return !this.config.showOverload();
                }
                case PRAYER_ENHANCE: {
                    return !this.config.showPrayerEnhance();
                }
                case STAMINA: {
                    return !this.config.showStamina();
                }
                case DIVINE_SCB: 
                case DIVINE_ATTACK: 
                case DIVINE_STRENGTH: 
                case DIVINE_BASTION: 
                case DIVINE_RANGE: {
                    return !this.config.showDivines();
                }
            }
            return true;
        }
        return true;
    }

    private List<AbstractMarker> renderPlayer(Graphics graphics, Player p, List<AbstractMarker> markers) {
        ArrayList<AbstractMarker> toRemove = new ArrayList<AbstractMarker>();
        int size = this.config.getIndicatorSize();
        int margin = this.config.getIndicatorPadding();
        graphics.setFont(new Font("SansSerif", 1, (int)(0.75 * (double)size)));
        Point base = Perspective.localToCanvas((Client)this.client, (LocalPoint)p.getLocalLocation(), (int)this.client.getPlane(), (int)p.getLogicalHeight());
        int zOffset = 0;
        int xOffset = this.config.getIndicatorXOffset() - size / 2;
        for (AbstractMarker marker : markers) {
            AbstractMarker timer;
            if (this.ignoreMarker(marker)) continue;
            if (marker instanceof TimerMarker) {
                timer = (TimerMarker)marker;
                long elapsedTime = System.currentTimeMillis() - ((TimerMarker)timer).getStartTime();
                double timeRemaining = ((TimerMarker)timer).getTimer().getDuration().toMillis() - elapsedTime;
                if (timeRemaining < 0.0) {
                    toRemove.add(marker);
                    continue;
                }
                BufferedImage icon = timer.getImage(size);
                graphics.drawImage(icon, base.getX() + xOffset, base.getY() + zOffset, null);
                int xDelta = icon.getWidth() + margin;
                String text = timeRemaining > 100000.0 ? String.format("%d", (long)(timeRemaining / 1000.0)) : String.format("%.1f", timeRemaining / 1000.0);
                graphics.setColor(Color.BLACK);
                graphics.drawString(text, base.getX() + xOffset + xDelta + 1, base.getY() + (zOffset += size));
                graphics.setColor(Color.WHITE);
                graphics.drawString(text, base.getX() + xOffset + xDelta, base.getY() + zOffset);
                zOffset += margin;
                continue;
            }
            if (!(marker instanceof IndicatorMarker)) continue;
            timer = (IndicatorMarker)marker;
            BufferedImage icon = timer.getImage(size);
            graphics.drawImage(icon, base.getX() + xOffset, base.getY() + zOffset, null);
            zOffset += size + margin;
        }
        return toRemove;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Dimension render(Graphics2D graphics) {
        List<AbstractMarker> toRemove;
        Map<String, List<AbstractMarker>> effects = this.plugin.getStatusEffects();
        Player p = this.client.getLocalPlayer();
        List<AbstractMarker> localMarkers = effects.get(null);
        if (localMarkers != null && !(toRemove = this.renderPlayer(graphics, p, localMarkers)).isEmpty()) {
            Map<String, List<AbstractMarker>> map = effects;
            synchronized (map) {
                for (AbstractMarker abstractMarker : toRemove) {
                    localMarkers.remove(abstractMarker);
                }
                if (localMarkers.isEmpty()) {
                    effects.remove(null);
                }
            }
        }
        for (Player t : this.client.getPlayers()) {
            List<AbstractMarker> list;
            List<AbstractMarker> markers;
            Point base;
            if (this.config.showSpecXfer() != PlayerStatusConfig.xferIconMode.OFF) {
                for (Map.Entry entry : this.plugin.getPartyStatus().entrySet()) {
                    String name = (String)entry.getKey();
                    PlayerStatus status = (PlayerStatus)entry.getValue();
                    if (!name.equals(t.getName())) continue;
                    System.out.println(this.plugin.playerNames.size());
                    if (this.config.showSpecXfer() != PlayerStatusConfig.xferIconMode.ALL && (this.config.showSpecXfer() != PlayerStatusConfig.xferIconMode.LIST || !this.plugin.playerNames.contains(t.getName().toLowerCase()))) continue;
                    int size = this.config.getIndicatorSize();
                    int margin = this.config.getIndicatorPadding();
                    graphics.setFont(new Font("SansSerif", 1, (int)(0.75 * (double)size)));
                    Point base2 = Perspective.localToCanvas((Client)this.client, (LocalPoint)t.getLocalLocation(), (int)this.client.getPlane(), (int)t.getLogicalHeight());
                    int zOffset = 0;
                    int xOffset = this.config.getIndicatorXOffset() - size / 2;
                    BufferedImage icon = this.spriteManager.getSprite(558, 0);
                    zOffset += size;
                    int xDelta = icon.getWidth() + margin;
                    String text = status.getSpecial() + "%";
                    if (status.getSpecial() > this.config.specThreshold()) continue;
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(text, base2.getX() + xOffset + xDelta + 1, base2.getY() + zOffset);
                    if (status.getSpecial() > 0) {
                        graphics.setColor(Color.YELLOW);
                    } else {
                        graphics.setColor(Color.RED);
                    }
                    graphics.drawString(text, base2.getX() + xOffset + xDelta, base2.getY() + zOffset);
                    zOffset += margin;
                }
            }
            if (p == t) continue;
            if (this.plugin.noSocketVenged.contains(t.getName()) && this.config.showVengeanceActive() == PlayerStatusConfig.vengeMode.ALL && (base = Perspective.localToCanvas((Client)this.client, (LocalPoint)t.getLocalLocation(), (int)t.getWorldLocation().getPlane(), (int)t.getLogicalHeight())) != null) {
                int n = this.config.getIndicatorSize();
                IndicatorMarker marker = new IndicatorMarker(GameIndicator.VENGEANCE_ACTIVE);
                marker.setBaseImage(this.spriteManager.getSprite(GameIndicator.VENGEANCE_ACTIVE.getImageId(), 0));
                BufferedImage icon = marker.getImage(n);
                graphics.drawImage((Image)icon, base.getX() + this.config.getIndicatorXOffset() - n / 2, base.getY(), null);
            }
            if ((markers = effects.get(t.getName())) == null || (list = this.renderPlayer(graphics, t, markers)).isEmpty()) continue;
            List<AbstractMarker> list2 = markers;
            synchronized (list2) {
                for (AbstractMarker marker : list) {
                    markers.remove(marker);
                }
                if (markers.isEmpty()) {
                    effects.remove(t.getName());
                }
            }
        }
        return null;
    }
}

