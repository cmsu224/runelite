//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.externals.autoclicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class AutoClickOverlay extends Overlay {
    private static final Color FLASH_COLOR = new Color(255, 0, 0, 70);
    private final Client client;
    private final AutoClick plugin;
    private final AutoClickConfig config;
    private int timeout;

    @Inject
    AutoClickOverlay(Client client, AutoClick plugin, AutoClickConfig config) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.plugin.isFlash() && this.config.flash()) {
            Color flash = graphics.getColor();
            graphics.setColor(FLASH_COLOR);
            graphics.fill(new Rectangle(this.client.getCanvas().getSize()));
            graphics.setColor(flash);
            ++this.timeout;
            if (this.timeout >= 50) {
                this.timeout = 0;
                this.plugin.setFlash(false);
            }
        }

        return null;
    }
}
