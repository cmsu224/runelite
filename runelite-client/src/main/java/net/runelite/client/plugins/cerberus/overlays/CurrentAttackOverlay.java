//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.cerberus.CerberusConfig;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.CerberusConfig.InfoBoxComponentSize;
import net.runelite.client.plugins.cerberus.domain.CerberusAttack;
import net.runelite.client.plugins.cerberus.domain.Phase;
import net.runelite.client.plugins.cerberus.domain.Cerberus.Attack;
import net.runelite.client.plugins.cerberus.util.ImageManager;
import net.runelite.client.plugins.cerberus.util.InfoBoxComponent;
import net.runelite.client.plugins.cerberus.util.Utility;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
public final class CurrentAttackOverlay extends Overlay {
    private static final Color COLOR_BORDER;
    private static final Color COLOR_PRAYER_ENABLED;
    private static final Color COLOR_PRAYER_DISABLED;
    private static final int GAME_TICK_THRESHOLD = 6;
    private final Client client;
    private final CerberusPlugin plugin;
    private final CerberusConfig config;
    private final InfoBoxComponent infoBoxComponent;

    @Inject
    CurrentAttackOverlay(Client client, CerberusPlugin plugin, CerberusConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.infoBoxComponent = new InfoBoxComponent();
        this.infoBoxComponent.setTextColor(Color.WHITE);
        this.setPriority(OverlayPriority.HIGHEST);
        this.setPosition(OverlayPosition.BOTTOM_RIGHT);
        this.determineLayer();
    }

    public Dimension render(Graphics2D graphics2D) {
        if (this.config.showCurrentAttack() && this.plugin.getCerberus() != null) {
            List<CerberusAttack> upcomingAttacks = this.plugin.getUpcomingAttacks();
            if (upcomingAttacks.isEmpty()) {
                return null;
            } else {
                CerberusAttack cerberusAttack = (CerberusAttack)this.plugin.getUpcomingAttacks().get(0);
                if (cerberusAttack.getTick() > this.plugin.getGameTick() + 6) {
                    return null;
                } else {
                    Prayer prayer;
                    if (cerberusAttack.getAttack() == Attack.AUTO) {
                        prayer = this.plugin.getPrayer();
                    } else {
                        prayer = cerberusAttack.getAttack().getPrayer();
                    }

                    if (prayer == null) {
                        return null;
                    } else {
                        InfoBoxComponentSize infoBoxComponentSize = this.config.infoBoxComponentSize();
                        int size = infoBoxComponentSize.getSize();
                        this.infoBoxComponent.setPreferredSize(new Dimension(size, size));
                        BufferedImage image = ImageManager.getCerberusBufferedImage(Phase.AUTO, prayer, infoBoxComponentSize);
                        this.infoBoxComponent.setImage(image);
                        Color backgroundColor = this.client.isPrayerActive(prayer) ? COLOR_PRAYER_ENABLED : COLOR_PRAYER_DISABLED;
                        this.infoBoxComponent.setBackgroundColor(backgroundColor);
                        this.infoBoxComponent.setFont(Utility.getFontFromInfoboxComponentSize(infoBoxComponentSize));
                        if (this.config.showCurrentAttackTimer()) {
                            double timeUntilAttack = Math.max((double)((long)((cerberusAttack.getTick() - this.plugin.getGameTick()) * 600) - (System.currentTimeMillis() - this.plugin.getLastTick())) / 1000.0D, 0.0D);
                            this.infoBoxComponent.setText(String.format("+%.1fs", timeUntilAttack));
                        } else {
                            this.infoBoxComponent.setText((String)null);
                        }

                        this.renderOutlineBorder(graphics2D, size);
                        return this.infoBoxComponent.render(graphics2D);
                    }
                }
            }
        } else {
            return null;
        }
    }

    private void renderOutlineBorder(Graphics2D graphics2D, int size) {
        int x = true;
        int y = true;
        Rectangle rectangle = new Rectangle();
        rectangle.setLocation(-1, -1);
        rectangle.setSize(size + 1, size + 1);
        graphics2D.setColor(COLOR_BORDER);
        graphics2D.draw(rectangle);
    }

    public void determineLayer() {
        this.setLayer(this.config.mirrorMode() ? OverlayLayer.AFTER_MIRROR : OverlayLayer.ABOVE_WIDGETS);
    }

    static {
        COLOR_BORDER = Color.WHITE;
        COLOR_PRAYER_ENABLED = new Color(70, 61, 50, 225);
        COLOR_PRAYER_DISABLED = new Color(150, 0, 0, 225);
    }
}
