//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
//import net.runelite.client.plugins.socket.plugins.playerstatus.PlayerStatusOverlay.1;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameIndicator;
import net.runelite.client.plugins.socket.plugins.playerstatus.gametimer.GameTimer;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.AbstractMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.IndicatorMarker;
import net.runelite.client.plugins.socket.plugins.playerstatus.marker.TimerMarker;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class PlayerStatusOverlay extends Overlay {
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
/*
    private boolean ignoreMarker(AbstractMarker marker) {
        if (marker == null) {
            return true;
        } else if (marker instanceof IndicatorMarker) {
            GameIndicator indicator = ((IndicatorMarker)marker).getIndicator();
            switch(1.$SwitchMap$net$runelite$client$plugins$socket$plugins$playerstatus$gametimer$GameIndicator[indicator.ordinal()]) {
                case 1:
                    return !this.config.showVengeanceActive();
                default:
                    return true;
            }
        } else if (marker instanceof TimerMarker) {
            GameTimer timer = ((TimerMarker)marker).getTimer();
            switch(1.$SwitchMap$net$runelite$client$plugins$socket$plugins$playerstatus$gametimer$GameTimer[timer.ordinal()]) {
                case 1:
                    return !this.config.showVengeanceCooldown();
                case 2:
                    return !this.config.showImbuedHeart();
                case 3:
                case 4:
                    return !this.config.showOverload();
                case 5:
                    return !this.config.showPrayerEnhance();
                case 6:
                    return !this.config.showStamina();
                default:
                    return true;
            }
        } else {
            return true;
        }
    }*/

    private List<AbstractMarker> renderPlayer(Graphics graphics, Player p, List<AbstractMarker> markers) {
        List<AbstractMarker> toRemove = new ArrayList();
        int size = this.config.getIndicatorSize();
        int margin = this.config.getIndicatorPadding();
        graphics.setFont(new Font("SansSerif", 1, (int)(0.75D * (double)size)));
        Point base = Perspective.localToCanvas(this.client, p.getLocalLocation(), this.client.getPlane(), p.getLogicalHeight());
        int zOffset = 0;
        int xOffset = this.config.getIndicatorXOffset() - size / 2;
        Iterator var10 = markers.iterator();

        while(var10.hasNext()) {
            AbstractMarker marker = (AbstractMarker)var10.next();
            /*if (!this.ignoreMarker(marker)) {
                if (marker instanceof TimerMarker) {
                    TimerMarker timer = (TimerMarker)marker;
                    long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
                    double timeRemaining = (double)(timer.getTimer().getDuration().toMillis() - elapsedTime);
                    if (timeRemaining < 0.0D) {
                        toRemove.add(marker);
                    } else {
                        BufferedImage icon = timer.getImage(size);
                        graphics.drawImage(icon, base.getX() + xOffset, base.getY() + zOffset, (ImageObserver)null);
                        zOffset += size;
                        int xDelta = icon.getWidth() + margin;
                        String text;
                        if (timeRemaining > 100000.0D) {
                            text = String.format("%d", (long)(timeRemaining / 1000.0D));
                        } else {
                            text = String.format("%.1f", timeRemaining / 1000.0D);
                        }

                        graphics.setColor(Color.BLACK);
                        graphics.drawString(text, base.getX() + xOffset + xDelta + 1, base.getY() + zOffset);
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(text, base.getX() + xOffset + xDelta, base.getY() + zOffset);
                        zOffset += margin;
                    }
                } else if (marker instanceof IndicatorMarker) {
                    IndicatorMarker timer = (IndicatorMarker)marker;
                    BufferedImage icon = timer.getImage(size);
                    graphics.drawImage(icon, base.getX() + xOffset, base.getY() + zOffset, (ImageObserver)null);
                    zOffset += size + margin;
                }
            }*/
        }

        return toRemove;
    }

    public Dimension render(Graphics2D graphics) {
        Map<String, List<AbstractMarker>> effects = this.plugin.getStatusEffects();
        Player p = this.client.getLocalPlayer();
        List<AbstractMarker> localMarkers = (List)effects.get((Object)null);
        if (localMarkers != null) {
            List<AbstractMarker> toRemove = this.renderPlayer(graphics, p, localMarkers);
            if (!toRemove.isEmpty()) {
                synchronized(effects) {
                    Iterator var7 = toRemove.iterator();

                    while(var7.hasNext()) {
                        AbstractMarker marker = (AbstractMarker)var7.next();
                        localMarkers.remove(marker);
                    }

                    if (localMarkers.isEmpty()) {
                        effects.remove((Object)null);
                    }
                }
            }
        }

        Iterator var15 = this.client.getPlayers().iterator();

        while(true) {
            Player t;
            List markers;
            List toRemove;
            do {
                do {
                    do {
                        if (!var15.hasNext()) {
                            return null;
                        }

                        t = (Player)var15.next();
                    } while(p == t);

                    markers = (List)effects.get(t.getName());
                } while(markers == null);

                toRemove = this.renderPlayer(graphics, t, markers);
            } while(toRemove.isEmpty());

            synchronized(markers) {
                Iterator var10 = toRemove.iterator();

                while(var10.hasNext()) {
                    AbstractMarker marker = (AbstractMarker)var10.next();
                    markers.remove(marker);
                }

                if (markers.isEmpty()) {
                    effects.remove(t.getName());
                }
            }
        }
    }
}
