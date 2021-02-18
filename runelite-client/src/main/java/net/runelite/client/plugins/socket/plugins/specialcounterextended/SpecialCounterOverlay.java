//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.specialcounterextended;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

public class SpecialCounterOverlay extends Overlay {
    private final Client client;
    private final SpecialCounterExtendedPlugin plugin;
    private final SpecialCounterExtendedConfig config;
    private Map<String, ArrayList<SpecialIcon>> drawings = new HashMap();

    @Inject
    private SpecialCounterOverlay(Client client, SpecialCounterExtendedPlugin plugin, SpecialCounterExtendedConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void addOverlay(String player, SpecialIcon icon) {
        if (this.config.showHitOverlay()) {
            ArrayList<SpecialIcon> icons = new ArrayList();
            if (this.drawings.containsKey(player)) {
                icons = (ArrayList)this.drawings.get(player);
            }

            icons.add(icon);
            this.drawings.put(player, icons);
        }

    }

    public Dimension render(Graphics2D graphics) {
        ArrayList<String> removePlayers = new ArrayList();
        Map<String, LocalPoint> locations = new HashMap();
        Iterator var4 = this.client.getPlayers().iterator();

        while(var4.hasNext()) {
            Player player = (Player)var4.next();
            locations.put(player.getName(), player.getLocalLocation());
        }

        var4 = this.drawings.keySet().iterator();

        while(true) {
            String playerName;
            while(var4.hasNext()) {
                playerName = (String)var4.next();
                LocalPoint center = (LocalPoint)locations.get(playerName);
                if (center != null) {
                    ArrayList<SpecialIcon> icons = (ArrayList)this.drawings.get(playerName);
                    ArrayList<SpecialIcon> removeIcons = new ArrayList();
                    int currentHeight = 200;

                    SpecialIcon icon;
                    for(int i = icons.size() - 1; i >= 0; --i) {
                        icon = (SpecialIcon)icons.get(i);
                        long elapsedTime = System.currentTimeMillis() - icon.getStartTime();
                        int fadeDelay = Math.max(this.config.getFadeDelay(), 1);
                        long timeRemaining = (long)fadeDelay - elapsedTime;
                        if (timeRemaining <= 0L) {
                            removeIcons.add(icon);
                        } else {
                            float opacity = (float)timeRemaining / (float)fadeDelay;
                            float thresh = Math.min(opacity + 0.2F, 1.0F);
                            graphics.setComposite(AlphaComposite.getInstance(3, thresh));
                            int maxHeight = Math.max(this.config.getMaxHeight(), 1);
                            int updatedHeight = maxHeight - (int)((float)maxHeight * thresh);
                            Point drawPoint = Perspective.getCanvasImageLocation(this.client, center, icon.getImage(), currentHeight + updatedHeight);
                            graphics.drawImage(icon.getImage(), drawPoint.getX(), drawPoint.getY(), (ImageObserver)null);
                            if (icon.getText() != null) {
                                Point textPoint = Perspective.getCanvasTextLocation(this.client, graphics, center, icon.getText(), currentHeight + updatedHeight);
                                graphics.setFont(new Font("Arial", 1, 16));
                                Point canvasCenterPoint = new Point(textPoint.getX(), textPoint.getY());
                                Point canvasCenterPointShadow = new Point(textPoint.getX() + 1, textPoint.getY() + 1);
                                OverlayUtil.renderTextLocation(graphics, canvasCenterPointShadow, icon.getText(), Color.BLACK);
                                OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, icon.getText(), Color.WHITE);
                            }

                            currentHeight += icon.getImage().getHeight() * 2;
                        }
                    }

                    Iterator var26 = removeIcons.iterator();

                    while(var26.hasNext()) {
                        icon = (SpecialIcon)var26.next();
                        icons.remove(icon);
                    }

                    if (icons.isEmpty()) {
                        removePlayers.add(playerName);
                    }
                } else {
                    removePlayers.add(playerName);
                }
            }

            var4 = removePlayers.iterator();

            while(var4.hasNext()) {
                playerName = (String)var4.next();
                this.drawings.remove(playerName);
            }

            return null;
        }
    }
}
