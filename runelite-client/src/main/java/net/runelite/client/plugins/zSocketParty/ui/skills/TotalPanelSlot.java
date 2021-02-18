//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


package net.runelite.client.plugins.zSocketParty.ui.skills;

import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.ImgUtil;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TotalPanelSlot extends JPanel
{
    private final JLabel levelLabel = new JLabel();
    private BufferedImage background;
    private BufferedImage skillHalf;
    private BufferedImage statHalf;

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (background == null)
        {
            return;
        }

        g.drawImage(background, 0, 0, null);
    }

    private void updateBackgroundImage()
    {
        if (skillHalf != null && statHalf != null)
        {
            background = ImgUtil.combineImages(skillHalf, statHalf);
            this.repaint();
        }
    }

    TotalPanelSlot(final int totalLevel, final SpriteManager spriteManager)
    {
        super();
        setOpaque(false);

        spriteManager.getSpriteAsync(SpriteID.STATS_TILE_HALF_LEFT_BLACK, 0, img ->
        {
            skillHalf = SkillPanelSlot.resize(img);
            updateBackgroundImage();
        });
        spriteManager.getSpriteAsync(SpriteID.STATS_TILE_HALF_RIGHT_BLACK, 0, img ->
        {
            statHalf = SkillPanelSlot.resize(img);
            updateBackgroundImage();
        });

        setPreferredSize(SkillPanelSlot.PANEL_FULL_SIZE);
        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        final JLabel textLabel = new JLabel("Total level:");
        textLabel.setFont(FontManager.getRunescapeSmallFont());
        textLabel.setForeground(Color.YELLOW);
        add(textLabel, c);

        if (totalLevel > 0)
        {
            levelLabel.setText(String.valueOf(totalLevel));
        }
        levelLabel.setFont(FontManager.getRunescapeSmallFont());
        levelLabel.setForeground(Color.YELLOW);
        c.gridy++;
        add(levelLabel, c);
    }

    public void updateTotalLevel(final int level)
    {
        levelLabel.setText(String.valueOf(level));
        levelLabel.repaint();
    }
}
