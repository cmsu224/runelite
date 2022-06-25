//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.pvpplayerindicators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class PvPPlayerIndicatorsMinimapOverlay extends Overlay {
    private final PvPPlayerIndicatorsService playerIndicatorsService;
    private final PvPPlayerIndicatorsConfig config;

    @Inject
    private PvPPlayerIndicatorsMinimapOverlay(PvPPlayerIndicatorsConfig config, PvPPlayerIndicatorsService playerIndicatorsService) {
        this.config = config;
        this.playerIndicatorsService = playerIndicatorsService;
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
    }

    public Dimension render(Graphics2D graphics) {
        this.playerIndicatorsService.forEachPlayer((player, color) -> {
            this.renderPlayerOverlay(graphics, player, color);
        });
        return null;
    }

    private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
        String name = actor.getName().replace(' ', ' ');
        if (this.config.drawMinimapNames()) {
            Point minimapLocation = actor.getMinimapLocation();
            if (minimapLocation != null) {
                OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
            }
        }

    }
}
