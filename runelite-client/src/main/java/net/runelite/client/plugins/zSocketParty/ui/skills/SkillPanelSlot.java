//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.skills;

import net.runelite.api.Constants;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.zSocketParty.ImgUtil;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SkillPanelSlot extends JPanel
{
    private static final Dimension PANEL_HALF_SIZE = new Dimension(Constants.ITEM_SPRITE_WIDTH, Constants.ITEM_SPRITE_HEIGHT + 4);
    static final Dimension PANEL_FULL_SIZE = new Dimension(PANEL_HALF_SIZE.width * 2, PANEL_HALF_SIZE.height);

    private final JLabel boostedLabel = new JLabel();
    private final JLabel baseLabel = new JLabel();
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

    SkillPanelSlot(final int boostedLevel, final int baseLevel)
    {
        super();
        setOpaque(false);

        setPreferredSize(PANEL_FULL_SIZE);
        setLayout(new BorderLayout());

        final JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        textPanel.setPreferredSize(PANEL_HALF_SIZE);
        textPanel.setOpaque(false);

        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = .5;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;

        boostedLabel.setText(String.valueOf(boostedLevel));
        boostedLabel.setVerticalAlignment(JLabel.CENTER);
        boostedLabel.setHorizontalAlignment(JLabel.LEFT);
        boostedLabel.setFont(FontManager.getRunescapeSmallFont());
        boostedLabel.setForeground(Color.YELLOW);
        boostedLabel.setBorder(new EmptyBorder(6, 3, 0, 0));
        c.anchor = GridBagConstraints.NORTHWEST;
        textPanel.add(boostedLabel, c);

        baseLabel.setText(String.valueOf(baseLevel));
        baseLabel.setVerticalAlignment(JLabel.CENTER);
        baseLabel.setHorizontalAlignment(JLabel.RIGHT);
        baseLabel.setBorder(new EmptyBorder(0, 0, 6, 6));
        baseLabel.setFont(FontManager.getRunescapeSmallFont());
        baseLabel.setForeground(Color.YELLOW);

        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridy++;
        textPanel.add(baseLabel, c);

        add(textPanel, BorderLayout.EAST);
    }

    void initImages(final BufferedImage skillIcon, final SpriteManager spriteManager)
    {
        spriteManager.getSpriteAsync(SpriteID.STATS_TILE_HALF_LEFT, 0, img ->
        {
            skillHalf = ImgUtil.overlapImages(skillIcon, SkillPanelSlot.resize(img));
            updateBackgroundImage();
        });
        spriteManager.getSpriteAsync(SpriteID.STATS_TILE_HALF_RIGHT_WITH_SLASH, 0, img ->
        {
            statHalf = SkillPanelSlot.resize(img);
            updateBackgroundImage();
        });
    }

    static BufferedImage resize(final BufferedImage img)
    {
        return ImageUtil.resizeImage(img, PANEL_HALF_SIZE.width, PANEL_HALF_SIZE.height);
    }

    public void updateBaseLevel(final int baseLevel)
    {
        baseLabel.setText(String.valueOf(baseLevel));
        baseLabel.repaint();
    }

    public void updateBoostedLevel(final int boostedLevel)
    {
        boostedLabel.setText(String.valueOf(boostedLevel));
        boostedLabel.repaint();
    }
}
