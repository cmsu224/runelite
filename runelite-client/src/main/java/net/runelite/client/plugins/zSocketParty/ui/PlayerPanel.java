//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.runelite.client.plugins.zSocketParty.ui;

import lombok.Getter;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.data.GameItem;
import net.runelite.client.plugins.zSocketParty.data.PartyPlayer;
import net.runelite.client.plugins.zSocketParty.data.PrayerData;
import net.runelite.client.plugins.zSocketParty.ui.equipment.EquipmentPanelSlot;
import net.runelite.client.plugins.zSocketParty.ui.equipment.PlayerEquipmentPanel;
import net.runelite.client.plugins.zSocketParty.ui.prayer.PlayerPrayerPanel;
import net.runelite.client.plugins.zSocketParty.ui.prayer.PrayerSlot;
import net.runelite.client.plugins.zSocketParty.ui.skills.PlayerSkillsPanel;
import net.runelite.client.plugins.zSocketParty.ui.skills.SkillPanelSlot;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map.Entry;

@Getter
public class PlayerPanel extends JPanel
{
    private static final Dimension IMAGE_SIZE = new Dimension(24, 24);

    private PartyPlayer player;
    private final SpriteManager spriteManager;
    private final ItemManager itemManager;

    private final PlayerBanner banner;
    private final PlayerInventoryPanel inventoryPanel;
    private final PlayerEquipmentPanel equipmentPanel;
    private final PlayerSkillsPanel skillsPanel;
    private final PlayerPrayerPanel prayersPanel;
    private MaterialTabGroup tabGroup;

    public PlayerPanel(final PartyPlayer selectedPlayer, final SpriteManager spriteManager, final ItemManager itemManager)
    {
        this.player = selectedPlayer;
        this.spriteManager = spriteManager;
        this.itemManager = itemManager;

        this.banner = new PlayerBanner(selectedPlayer, spriteManager);
        this.inventoryPanel = new PlayerInventoryPanel(selectedPlayer.getInventory(), itemManager);
        this.equipmentPanel = new PlayerEquipmentPanel(selectedPlayer.getEquipment(), spriteManager, itemManager);
        this.skillsPanel = new PlayerSkillsPanel(selectedPlayer, spriteManager);
        this.prayersPanel = new PlayerPrayerPanel(selectedPlayer, spriteManager);

        final JPanel view = new JPanel();
        this.tabGroup = new MaterialTabGroup(view);
        tabGroup.setBorder(new EmptyBorder(10, 0, 10, 0));

        addTab(tabGroup, SpriteID.TAB_INVENTORY, inventoryPanel, "Inventory");
        addTab(tabGroup, SpriteID.TAB_EQUIPMENT, equipmentPanel, "Equipment");
        addTab(tabGroup, SpriteID.TAB_PRAYER, prayersPanel, "Prayers");
        addTab(tabGroup, SpriteID.TAB_STATS, skillsPanel, "Skills");

        setLayout(new DynamicGridLayout(0, 1));
        add(banner);
        add(tabGroup);
        add(view);

        revalidate();
        repaint();
    }

    private void addTab(final MaterialTabGroup tabGroup, final int spriteID, final JPanel panel, final String tooltip)
    {
        spriteManager.getSpriteAsync(spriteID, 0, img ->
        {
            SwingUtilities.invokeLater(() ->
            {
                final MaterialTab tab = new MaterialTab(createImageIcon(img), tabGroup, panel);
                tab.setToolTipText(tooltip);
                tabGroup.addTab(tab);
                tabGroup.revalidate();
                tabGroup.repaint();

                if (spriteID == SpriteID.TAB_INVENTORY)
                {
                    tabGroup.select(tab);
                }
            });
        });
    }

    private ImageIcon createImageIcon(BufferedImage image)
    {
        return new ImageIcon(ImageUtil.resizeImage(image, IMAGE_SIZE.width, IMAGE_SIZE.height));
    }

    // TODO add smarter ways to update data
    public void changePlayer(final PartyPlayer newPlayer)
    {
        final boolean newUser = !newPlayer.getUsername().equals(player.getUsername());

        player = newPlayer;
        banner.setPlayer(player);

        //if(!this.tabGroup.getTab(0).isSelected()) {
            inventoryPanel.updateInventory(player.getInventory());
        //}
        for (final EquipmentInventorySlot equipSlot : EquipmentInventorySlot.values())
        {
            GameItem item = null;
            if (player.getEquipment().length > equipSlot.getSlotIdx())
            {
                item = player.getEquipment()[equipSlot.getSlotIdx()];
            }

            final EquipmentPanelSlot slot = this.equipmentPanel.getPanelMap().get(equipSlot);
            if (item != null)
            {
                final AsyncBufferedImage img = itemManager.getImage(item.getId(), item.getQty(), item.isStackable());
                slot.setGameItem(item, img);

                // Ensure item is set when image loads
                final GameItem finalItem = item;
                img.onLoaded(() -> slot.setGameItem(finalItem, img));
            }
            else
            {
                slot.setGameItem(null, null);
            }
        }

        if (newUser)
        {
            banner.recreatePanel();
        }

        if (player.getStats() != null)
        {
            banner.refreshStats();
            for (final Skill s : Skill.values())
            {
                if (s.equals(Skill.OVERALL))
                {
                    continue;
                }

                final SkillPanelSlot panel = skillsPanel.getPanelMap().get(s);
                panel.updateBoostedLevel(player.getStats().getBoostedLevels().get(s));
                panel.updateBaseLevel(player.getStats().getBaseLevels().get(s));
            }
            skillsPanel.getTotalLevelPanel().updateTotalLevel(player.getStats().getTotalLevel());
        }

        if (player.getPrayers() != null)
        {
            for (final Entry<Prayer, PrayerSlot> entry : prayersPanel.getSlotMap().entrySet())
            {
                final PrayerData data = player.getPrayers().getPrayerData().get(entry.getKey());
                if (data != null)
                {
                    entry.getValue().updatePrayerData(data);
                }
            }

            prayersPanel.updatePrayerRemaining(player.getSkillBoostedLevel(Skill.PRAYER), player.getSkillRealLevel(Skill.PRAYER));
        }
    }
}
