//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.plugins.aztobextra.config.MaidenRedWCType;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class MaidenNyloWheelchairOverlay extends OverlayPanel {
    private final AzEasyTobPlugin plugin;
    private final AzEasyTobConfig config;

    @Inject
    public MaidenNyloWheelchairOverlay(AzEasyTobPlugin plugin, AzEasyTobConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.plugin = plugin;
        this.config = config;
    }

    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.getMaidenMatos().isEmpty() && this.config.getType() != MaidenRedWCType.OFF && this.config.getType() != MaidenRedWCType.TILE && this.plugin.isN1Orn2Alive()) {
            if (this.plugin.getClient() == null) {
                return null;
            } else if (this.plugin.getClient().getVarbitValue(4070) != 1 && !this.config.debug()) {
                return null;
            } else {
                String text = this.plugin.isMaidenNyloWheelchairState() ? "Good" : "Bad";
                this.panelComponent.getChildren().add(TitleComponent.builder().text(text).color(text.equalsIgnoreCase("good") ? Color.GREEN : Color.RED).build());
                int width = graphics.getFontMetrics().stringWidth("  good  ");
                this.panelComponent.setPreferredSize(new Dimension(width, 0));
                return super.render(graphics);
            }
        } else {
            return null;
        }
    }
}
