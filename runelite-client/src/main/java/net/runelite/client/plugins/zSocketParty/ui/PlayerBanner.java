//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Constants;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.data.PartyPlayer;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PlayerBanner extends JPanel
{
    private static final Dimension STAT_ICON_SIZE = new Dimension(18, 18);
    private static final Dimension ICON_SIZE = new Dimension(Constants.ITEM_SPRITE_WIDTH, Constants.ITEM_SPRITE_HEIGHT);
    private static final String SPECIAL_ATTACK_NAME = "Special Attack";
    private static final String RUN_ENERGY_NAME = "Run Energy";

    private final JPanel statsPanel = new JPanel();
    private final JLabel iconLabel = new JLabel();
    private final Map<String, JLabel> statLabels = new HashMap<>();

    @Setter
    @Getter
    private PartyPlayer player;
    private boolean checkIcon;

    public PlayerBanner(final PartyPlayer player, SpriteManager spriteManager)
    {
        super();
        this.player = player;

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 75));
        this.setBorder(new CompoundBorder(
                new MatteBorder(2, 2, 2, 2, ColorScheme.DARK_GRAY_HOVER_COLOR),
                new EmptyBorder(5, 5, 5,  5)
        ));

        statsPanel.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
        statsPanel.setLayout(new GridLayout(0, 4));
        statsPanel.setOpaque(false);

        statsPanel.add(createIconPanel(spriteManager, SpriteID.SKILL_HITPOINTS, Skill.HITPOINTS.getName(), String.valueOf(player.getSkillBoostedLevel(Skill.HITPOINTS))));
        statsPanel.add(createIconPanel(spriteManager, SpriteID.SKILL_PRAYER, Skill.PRAYER.getName(), String.valueOf(player.getSkillBoostedLevel(Skill.PRAYER))));
        statsPanel.add(createIconPanel(spriteManager, SpriteID.MULTI_COMBAT_ZONE_CROSSED_SWORDS, SPECIAL_ATTACK_NAME, player.getStats() == null ? "0%" : String.valueOf(player.getStats().getSpecialPercent()) + "%"));
        statsPanel.add(createIconPanel(spriteManager, SpriteID.MINIMAP_ORB_RUN_ICON, RUN_ENERGY_NAME, player.getStats() == null ? "0%" : String.valueOf(player.getStats().getRunEnergy()) + "%"));

        recreatePanel();
    }

    public void recreatePanel()
    {
        removeAll();

        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1.0;
        c.ipady = 4;

        // Add avatar label regardless of if one exists just to have UI matching
        iconLabel.setBorder(new MatteBorder(1, 1, 1, 1, ColorScheme.DARKER_GRAY_HOVER_COLOR));
        iconLabel.setPreferredSize(ICON_SIZE);
        iconLabel.setMinimumSize(ICON_SIZE);
        iconLabel.setOpaque(false);

        //checkIcon = player.getMember().getAvatar() == null;
        //if (!checkIcon)
        //{
        //    addIcon();
        //}

        add(iconLabel, c);
        c.gridx++;

        final JPanel nameContainer = new JPanel(new GridLayout(2, 1));
        nameContainer.setBorder(new EmptyBorder(0, 10, 0, 0));
        nameContainer.setOpaque(false);

        final JLabel usernameLabel = new JLabel();
        usernameLabel.setHorizontalTextPosition(JLabel.LEFT);
        if (player.getUsername() == null)
        {
            usernameLabel.setText("Not logged in");
        }
        else
        {
            final String levelText = player.getStats() == null ? "" : " (Lvl - " + player.getStats().getCombatLevel() + ")";
            usernameLabel.setText(player.getUsername() + levelText);
        }

        final JLabel discordNameLabel = new JLabel(player.getUsername());
        discordNameLabel.setHorizontalTextPosition(JLabel.LEFT);

        nameContainer.add(usernameLabel);
        nameContainer.add(discordNameLabel);

        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(nameContainer, c);

        refreshStats();
        c.gridy++;
        c.weightx = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        add(statsPanel, c);

        revalidate();
        repaint();
    }

    private void addIcon()
    {
        final BufferedImage resized = ImageUtil.getResourceStreamFromClass(this.getClass(), "icon.png");;
        iconLabel.setIcon(new ImageIcon(resized));
    }

    public void refreshStats()
    {
        if (checkIcon)
        {
            addIcon();
            checkIcon = false;
        }

        statLabels.getOrDefault(Skill.HITPOINTS.getName(), new JLabel()).setText(String.valueOf(player.getSkillBoostedLevel(Skill.HITPOINTS)));
        statLabels.getOrDefault(Skill.PRAYER.getName(), new JLabel()).setText(String.valueOf(player.getSkillBoostedLevel(Skill.PRAYER)));
        statLabels.getOrDefault(SPECIAL_ATTACK_NAME, new JLabel()).setText(player.getStats() == null ? "0%" : String.valueOf(player.getStats().getSpecialPercent()) + "%");
        statLabels.getOrDefault(RUN_ENERGY_NAME, new JLabel()).setText(player.getStats() == null ? "0%" : String.valueOf(player.getStats().getRunEnergy()) + "%");

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createIconPanel(final SpriteManager spriteManager, final int spriteID, final String name,
                                   final String value)
    {
        final JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(STAT_ICON_SIZE);
        spriteManager.getSpriteAsync(spriteID, 0, img ->
        {
            SwingUtilities.invokeLater(() ->
            {
                iconLabel.setIcon(new ImageIcon(ImageUtil.resizeImage(img, STAT_ICON_SIZE.width, STAT_ICON_SIZE.height)));
                iconLabel.revalidate();
                iconLabel.repaint();
            });
        });

        final JLabel textLabel = new JLabel(value);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setHorizontalTextPosition(JLabel.CENTER);
        statLabels.put(name, textLabel);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textLabel, BorderLayout.CENTER);
        panel.setOpaque(false);
        panel.setToolTipText(name);

        return panel;
    }
}
