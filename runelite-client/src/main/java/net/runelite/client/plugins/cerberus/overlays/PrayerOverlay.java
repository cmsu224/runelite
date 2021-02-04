//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.cerberus.CerberusConfig;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.domain.CerberusAttack;
import net.runelite.client.plugins.cerberus.util.Utility;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class PrayerOverlay extends Overlay {
    private static final int TICK_PIXEL_SIZE = 60;
    private static final int BOX_WIDTH = 10;
    private static final int BOX_HEIGHT = 5;
    private final Client client;
    private final CerberusPlugin plugin;
    private final CerberusConfig config;
    private final Map<Widget, Integer> lastBoxBaseYMap = new HashMap();

    @Inject
    private PrayerOverlay(Client client, CerberusPlugin plugin, CerberusConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPriority(OverlayPriority.HIGHEST);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.determineLayer();
    }

    public Dimension render(Graphics2D graphics2D) {
        this.renderPrayer(graphics2D);
        return null;
    }

    private void renderPrayer(Graphics2D graphics2D) {
        if (this.config.guitarHeroMode() && this.plugin.getCerberus() != null) {
            int gameTick = this.plugin.getGameTick();
            List<CerberusAttack> upcomingAttacks = this.plugin.getUpcomingAttacks();
            boolean first = true;
            Iterator var5 = upcomingAttacks.iterator();

            while(var5.hasNext()) {
                CerberusAttack attack = (CerberusAttack)var5.next();
                int tick = attack.getTick() - gameTick;
                if (tick <= this.config.guitarHeroTicks()) {
                    Prayer prayer = attack.getAttack().getPrayer();
                    this.renderDescendingBoxes(graphics2D, prayer, tick);
                    if (first) {
                        this.renderPrayerWidget(graphics2D, prayer, tick);
                        first = false;
                    }
                }
            }

        }
    }

    private void renderDescendingBoxes(Graphics2D graphics2D, Prayer prayer, int tick) {
        Widget prayerWidget = this.client.getWidget(prayer.getWidgetInfo());
        if (prayerWidget != null && !prayerWidget.isHidden()) {
            long lastTick = this.plugin.getLastTick();
            int baseX = (int)prayerWidget.getBounds().getX();
            baseX = (int)((double)baseX + prayerWidget.getBounds().getWidth() / 2.0D);
            baseX -= 5;
            int baseY = (int)prayerWidget.getBounds().getY() - tick * 60 - 5;
            baseY = (int)((double)baseY + (60.0D - (double)(lastTick + 600L - System.currentTimeMillis()) / 600.0D * 60.0D));
            if (baseY <= (int)prayerWidget.getBounds().getY() - 5) {
                if (System.currentTimeMillis() - lastTick > 600L) {
                    this.lastBoxBaseYMap.put(prayerWidget, baseY);
                } else if (this.lastBoxBaseYMap.containsKey(prayerWidget)) {
                    if ((Integer)this.lastBoxBaseYMap.get(prayerWidget) >= baseY && (Integer)this.lastBoxBaseYMap.get(prayerWidget) < (int)(prayerWidget.getBounds().getY() - 5.0D)) {
                        baseY = (Integer)this.lastBoxBaseYMap.get(prayerWidget) + 1;
                        this.lastBoxBaseYMap.put(prayerWidget, baseY);
                    } else {
                        this.lastBoxBaseYMap.remove(prayerWidget);
                    }
                }

                Rectangle boxRectangle = new Rectangle(10, 5);
                boxRectangle.translate(baseX, baseY);
                OverlayUtil.renderFilledPolygon(graphics2D, boxRectangle, Color.ORANGE);
            }
        }
    }

    private void renderPrayerWidget(Graphics2D graphics2D, Prayer prayer, int tick) {
        Rectangle rectangle = OverlayUtil.renderPrayerOverlay(graphics2D, this.client, prayer, Utility.getColorFromPrayer(prayer));
        if (rectangle != null) {
            String text = String.valueOf(tick);
            int fontSize = true;
            int fontStyle = true;
            Color fontColor = tick == 1 ? Color.RED : Color.WHITE;
            int x = (int)(rectangle.getX() + rectangle.getWidth() / 2.0D);
            int y = (int)(rectangle.getY() + rectangle.getHeight() / 2.0D);
            Point point = new Point(x - 3, y + 6);
            OverlayUtil.renderTextLocation(graphics2D, text, 16, 1, fontColor, point, true, 0);
        }
    }

    public void determineLayer() {
        this.setLayer(this.config.mirrorMode() ? OverlayLayer.AFTER_MIRROR : OverlayLayer.ABOVE_WIDGETS);
    }
}
