//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuOpcode;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;

class TimerOverlay extends Overlay {
    private final Client client;
    private final BarbarianAssaultPlugin plugin;
    private final BarbarianAssaultConfig config;

    @Inject
    private TimerOverlay(Client client, BarbarianAssaultPlugin plugin, BarbarianAssaultConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuOpcode.RUNELITE_OVERLAY_CONFIG, "Configure", "B.A. overlay"));
    }

    public Dimension render(Graphics2D graphics) {
        Round round = this.plugin.getCurrentRound();
        if (round == null) {
            return null;
        } else {
            Role role = round.getRoundRole();
            Widget roleText = this.client.getWidget(role.getRoleText());
            Widget roleSprite = this.client.getWidget(role.getRoleSprite());
            if (this.config.showTimer() && roleText != null && roleSprite != null) {
                roleText.setText(String.format("00:%02d", round.getTimeToChange()));
                Rectangle spriteBounds = roleSprite.getBounds();
                roleSprite.setHidden(true);
                graphics.drawImage(this.plugin.getClockImage(), spriteBounds.x, spriteBounds.y, (ImageObserver)null);
            }

            return null;
        }
    }
}
