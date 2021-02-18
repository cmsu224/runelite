//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui;

import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.zSocketParty.data.GameItem;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PlayerInventoryPanel extends JPanel {
    private static final Dimension INVI_SLOT_SIZE = new Dimension(50, 42);
    private static final Dimension PANEL_SIZE = new Dimension(215, 300);
    private static final Color INVI_BACKGROUND = new Color(62, 53, 41);
    private static final Color INVI_BORDER_COLOR = new Color(87, 80, 64);
    private static final Border INVI_BORDER;
    private final ItemManager itemManager;

    public PlayerInventoryPanel(GameItem[] items, ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new DynamicGridLayout(7, 4, 2, 2));
        this.setBackground(INVI_BACKGROUND);
        this.setBorder(INVI_BORDER);
        this.setPreferredSize(PANEL_SIZE);
        this.updateInventory(items);
    }

    public void updateInventory(GameItem[] items) {
        this.removeAll();
        GameItem[] var2 = items;
        int var3 = items.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            GameItem i = var2[var4];
            JLabel label = new JLabel();
            label.setMinimumSize(INVI_SLOT_SIZE);
            label.setPreferredSize(INVI_SLOT_SIZE);
            label.setVerticalAlignment(0);
            label.setHorizontalAlignment(0);
            if (i != null) {
                String name = i.getName();
                if (i.getQty() > 1) {
                    name = name + " x " + QuantityFormatter.formatNumber((long)i.getQty());
                }

                label.setToolTipText(name);
                this.itemManager.getImage(i.getId(), i.getQty(), i.isStackable()).addTo(label);
            }

            this.add(label);
        }

        for(int i = this.getComponentCount(); i < 28; ++i) {
            JLabel label = new JLabel();
            label.setMinimumSize(INVI_SLOT_SIZE);
            label.setPreferredSize(INVI_SLOT_SIZE);
            label.setVerticalAlignment(0);
            label.setHorizontalAlignment(0);
            this.add(label);
        }

        this.revalidate();
        this.repaint();
    }

    static {
        INVI_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, INVI_BORDER_COLOR), BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
}

