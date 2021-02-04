//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.spellbook;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseWheelListener;

@Singleton
class SpellbookMouseListener extends MouseAdapter implements MouseWheelListener {
    private final SpellbookPlugin plugin;

    SpellbookMouseListener(SpellbookPlugin plugin) {
        this.plugin = plugin;
    }

    public MouseEvent mouseClicked(MouseEvent event) {
        if (this.plugin.isNotOnSpellWidget()) {
            return event;
        } else {
            if (SwingUtilities.isMiddleMouseButton(event)) {
                this.plugin.resetSize();
            }

            event.consume();
            return event;
        }
    }

    public MouseEvent mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            this.plugin.resetLocation();
            return event;
        } else {
            if (SwingUtilities.isLeftMouseButton(event) && !this.plugin.isNotOnSpellWidget() && !this.plugin.isDragging()) {
                this.plugin.startDragging(event.getPoint());
                event.consume();
            }

            return event;
        }
    }

    public MouseEvent mouseReleased(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && this.plugin.isDragging()) {
            this.plugin.completeDragging(event.getPoint());
            event.consume();
            return event;
        } else {
            return event;
        }
    }

    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
        if (this.plugin.isNotOnSpellWidget()) {
            return event;
        } else {
            int direction = event.getWheelRotation();
            if (direction > 0) {
                this.plugin.increaseSize();
            } else {
                this.plugin.decreaseSize();
            }

            event.consume();
            return event;
        }
    }
}
