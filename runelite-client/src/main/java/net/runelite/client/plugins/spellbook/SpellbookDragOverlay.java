//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.spellbook;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
public class SpellbookDragOverlay extends Overlay {
    private final SpellbookPlugin plugin;
    private final Client client;

    @Inject
    private SpellbookDragOverlay(SpellbookPlugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
        this.setPosition(OverlayPosition.TOOLTIP);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    public Dimension render(Graphics2D g) {
        if (!this.plugin.isDragging()) {
            return null;
        } else {
            Point mouseCanvasPosition = this.client.getMouseCanvasPosition();
            Point draggingLocation = this.plugin.getDraggingLocation();
            Sprite sprite = this.plugin.getDraggingWidget().getSprite();
            java.awt.Point drawPos = new java.awt.Point(mouseCanvasPosition.getX() - draggingLocation.getX(), mouseCanvasPosition.getY() - draggingLocation.getY());
            if (sprite != null) {
                sprite.drawAt(drawPos.x, drawPos.y);
            }

            return null;
        }
    }
}
