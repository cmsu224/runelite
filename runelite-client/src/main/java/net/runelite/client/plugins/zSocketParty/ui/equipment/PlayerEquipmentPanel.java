//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.equipment;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import lombok.Getter;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.SpriteID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.data.GameItem;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PlayerEquipmentPanel extends JPanel
{
    private static final ImmutableMap<EquipmentInventorySlot, Integer> EQUIPMENT_SLOT_SPRITE_MAP;
    static
    {
        final Builder<EquipmentInventorySlot, Integer> sprites = new Builder<>();
        sprites.put(EquipmentInventorySlot.HEAD, SpriteID.EQUIPMENT_SLOT_HEAD);
        sprites.put(EquipmentInventorySlot.CAPE, SpriteID.EQUIPMENT_SLOT_CAPE);
        sprites.put(EquipmentInventorySlot.AMULET, SpriteID.EQUIPMENT_SLOT_NECK);
        sprites.put(EquipmentInventorySlot.WEAPON, SpriteID.EQUIPMENT_SLOT_WEAPON);
        sprites.put(EquipmentInventorySlot.RING, SpriteID.EQUIPMENT_SLOT_RING);
        sprites.put(EquipmentInventorySlot.BODY, SpriteID.EQUIPMENT_SLOT_TORSO);
        sprites.put(EquipmentInventorySlot.SHIELD, SpriteID.EQUIPMENT_SLOT_SHIELD);
        sprites.put(EquipmentInventorySlot.LEGS, SpriteID.EQUIPMENT_SLOT_LEGS);
        sprites.put(EquipmentInventorySlot.GLOVES, SpriteID.EQUIPMENT_SLOT_HANDS);
        sprites.put(EquipmentInventorySlot.BOOTS, SpriteID.EQUIPMENT_SLOT_FEET);
        sprites.put(EquipmentInventorySlot.AMMO, SpriteID.EQUIPMENT_SLOT_AMMUNITION);

        EQUIPMENT_SLOT_SPRITE_MAP = sprites.build();
    }

    private static final BufferedImage PANEL_BACKGROUND = ImageUtil.loadImageResource(PlayerEquipmentPanel.class, "equipment-bars.png");
    private static final Dimension PANEL_SIZE = new Dimension(PluginPanel.PANEL_WIDTH - 10, 300);
    private static final Color PANEL_BORDER_COLOR = new Color(87, 80, 64);
    private static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 3, 3, 3, PANEL_BORDER_COLOR),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
    );

    // Used to offset the weapon/shield and glove/ring slots
    private static final Border BORDER_LEFT = new EmptyBorder(0, 15, 0, 0);
    private static final Border BORDER_RIGHT = new EmptyBorder(0, 0, 0, 15);

    @Getter
    private final Map<EquipmentInventorySlot, EquipmentPanelSlot> panelMap = new HashMap<>();

    private final ItemManager itemManager;
    private final SpriteManager spriteManager;

    public PlayerEquipmentPanel(final GameItem[] items, final SpriteManager spriteManager, final ItemManager itemManager)
    {
        super();

        this.spriteManager = spriteManager;
        this.itemManager = itemManager;

        this.setMinimumSize(PANEL_SIZE);
        this.setPreferredSize(PANEL_SIZE);
        this.setBorder(PANEL_BORDER);
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(62, 53, 41));

        spriteManager.getSpriteAsync(SpriteID.EQUIPMENT_SLOT_TILE, 0, img -> SwingUtilities.invokeLater(() -> createPanel(items, img)));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        final int x = (this.getWidth() - PANEL_BACKGROUND.getWidth()) / 2;
        final int y = (this.getHeight() - PANEL_BACKGROUND.getHeight()) / 2;
        g2d.drawImage(PANEL_BACKGROUND, x, y, null);
    }

    private void createPanel(final GameItem[] items, final BufferedImage background)
    {
        this.removeAll();

        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.ipadx = 4;
        c.ipady = 3;
        c.anchor = GridBagConstraints.CENTER;

        // I don't see an iterative way to setup this layout correctly
        // First row
        c.gridx = 1;
        c.gridy = 0;
        createEquipmentPanelSlot(EquipmentInventorySlot.HEAD, items, background, c);

        c.gridx = 0;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        createEquipmentPanelSlot(EquipmentInventorySlot.CAPE, items, background, c);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx++;
        createEquipmentPanelSlot(EquipmentInventorySlot.AMULET, items, background, c);
        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        createEquipmentPanelSlot(EquipmentInventorySlot.AMMO, items, background, c);
        c.anchor = GridBagConstraints.CENTER;

        c.gridx = 0;
        c.gridy++;
        createEquipmentPanelSlot(EquipmentInventorySlot.WEAPON, items, background, c, BORDER_RIGHT);
        c.gridx++;
        createEquipmentPanelSlot(EquipmentInventorySlot.BODY, items, background, c);
        c.gridx++;
        createEquipmentPanelSlot(EquipmentInventorySlot.SHIELD, items, background, c, BORDER_LEFT);

        c.gridx = 1;
        c.gridy++;
        createEquipmentPanelSlot(EquipmentInventorySlot.LEGS, items, background, c);
        c.gridx = 0;
        c.gridy++;
        createEquipmentPanelSlot(EquipmentInventorySlot.GLOVES, items, background, c, BORDER_RIGHT);
        c.gridx++;
        createEquipmentPanelSlot(EquipmentInventorySlot.BOOTS, items, background, c);
        c.gridx++;
        createEquipmentPanelSlot(EquipmentInventorySlot.RING, items, background, c, BORDER_LEFT);

        this.revalidate();
        this.repaint();
    }
    private void createEquipmentPanelSlot(final EquipmentInventorySlot slot, final GameItem[] items,
                                          final BufferedImage background, final GridBagConstraints c)
    {
        createEquipmentPanelSlot(slot, items, background, c, null);
    }

    private void createEquipmentPanelSlot(final EquipmentInventorySlot slot, final GameItem[] items,
                                          final BufferedImage background, final GridBagConstraints constraints, final Border border)
    {
        // Clone constraints for async support
        final GridBagConstraints c = (GridBagConstraints) constraints.clone();
        final GameItem item = items.length > slot.getSlotIdx() ? items[slot.getSlotIdx()] : null;
        final AsyncBufferedImage image = item == null ? null : itemManager.getImage(item.getId(), item.getQty(), item.isStackable());

        spriteManager.getSpriteAsync(EQUIPMENT_SLOT_SPRITE_MAP.get(slot), 0, img ->
        {
            SwingUtilities.invokeLater(() ->
            {
                final EquipmentPanelSlot panel = new EquipmentPanelSlot(item, image, background, img);
                if (border != null)
                {
                    panel.setBorder(border);
                }
                panelMap.put(slot, panel);

                if (image != null)
                {
                    image.onLoaded(() -> panel.setGameItem(item, image));
                }

                add(panel, c);
            });
        });
    }
}
