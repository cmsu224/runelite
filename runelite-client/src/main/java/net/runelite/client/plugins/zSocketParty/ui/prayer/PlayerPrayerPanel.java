//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.prayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.plugins.zSocketParty.data.PartyPlayer;
import net.runelite.client.plugins.zSocketParty.data.PrayerData;
import net.runelite.client.plugins.zSocketParty.data.Prayers;

public class PlayerPrayerPanel extends JPanel {
    private static final Dimension PANEL_SIZE = new Dimension(215, 300);
    private static final Color BACKGROUND = new Color(62, 53, 41);
    private static final Color BORDER_COLOR = new Color(87, 80, 64);
    private static final Border BORDER;
    private static final int MAX_COLUMNS = 5;
    private final Map<Prayer, PrayerSlot> slotMap = new HashMap();
    private final JLabel remainingLabel = new JLabel();

    public PlayerPrayerPanel(PartyPlayer player, SpriteManager spriteManager) {
        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND);
        this.setBorder(BORDER);
        this.setPreferredSize(PANEL_SIZE);
        this.add(this.createPrayerContainer(player.getPrayers(), spriteManager), "North");
        this.add(this.createPrayerRemainingPanel(spriteManager), "South");
        this.updatePrayerRemaining(player.getSkillBoostedLevel(Skill.PRAYER), player.getSkillRealLevel(Skill.PRAYER));
    }

    private JPanel createPrayerContainer(Prayers prayer, SpriteManager spriteManager) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(PANEL_SIZE.width, PANEL_SIZE.height - 25));
        panel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.5D;
        c.weightx = 0.5D;
        c.ipadx = 2;
        c.ipady = 2;
        c.anchor = 10;
        PrayerSprites[] var5 = PrayerSprites.values();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            PrayerSprites p = var5[var7];
            PrayerSlot slot = new PrayerSlot(p, spriteManager);
            if (prayer != null) {
                PrayerData data = (PrayerData)prayer.getPrayerData().get(p.getPrayer());
                if (data != null) {
                    slot.updatePrayerData(data);
                }
            }

            this.slotMap.put(p.getPrayer(), slot);
            if (c.gridx == 5) {
                c.gridx = 0;
                ++c.gridy;
            }

            panel.add(slot, c);
            ++c.gridx;
        }

        return panel;
    }

    private JPanel createPrayerRemainingPanel(SpriteManager spriteManager) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = 10;
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 4;
        c.gridwidth = 1;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(PANEL_SIZE.width, 25));
        JLabel iconLabel = new JLabel();
        iconLabel.setOpaque(false);
        spriteManager.addSpriteTo(iconLabel, 651, 0);
        iconLabel.setHorizontalAlignment(4);
        this.remainingLabel.setFont(FontManager.getRunescapeSmallFont());
        this.remainingLabel.setForeground(ColorScheme.BRAND_ORANGE);
        this.remainingLabel.setVerticalAlignment(0);
        this.remainingLabel.setHorizontalTextPosition(2);
        this.remainingLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
        this.remainingLabel.setOpaque(false);
        panel.add(iconLabel, c);
        ++c.gridx;
        panel.add(this.remainingLabel, c);
        return panel;
    }

    public void updatePrayerRemaining(int remaining, int maximum) {
        this.remainingLabel.setText(remaining + "/" + maximum);
    }

    public Map<Prayer, PrayerSlot> getSlotMap() {
        return this.slotMap;
    }

    static {
        BORDER = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, BORDER_COLOR), BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
}