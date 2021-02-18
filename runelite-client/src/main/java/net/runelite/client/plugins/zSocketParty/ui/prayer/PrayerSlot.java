//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.prayer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.Text;
import net.runelite.client.plugins.zSocketParty.ImgUtil;
import net.runelite.client.plugins.zSocketParty.data.PrayerData;
import net.runelite.client.util.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PrayerSlot extends JLabel {
    private static final Dimension SIZE = new Dimension(40, 40);
    private BufferedImage unavailableImage;
    private BufferedImage availableImage;
    private BufferedImage activatedImage;
    private PrayerData data;

    public PrayerSlot(PrayerSprites sprites, SpriteManager spriteManager) {
        this.data = new PrayerData(sprites.getPrayer(), false, false);
        spriteManager.getSpriteAsync(sprites.getUnavailable(), 0, (img) -> {
            this.unavailableImage = img;
        });
        spriteManager.getSpriteAsync(sprites.getAvailable(), 0, (img) -> {
            this.availableImage = img;
            this.updateActivatedImage();
        });
        spriteManager.getSpriteAsync(155, 0, (img) -> {
            this.activatedImage = img;
            this.updateActivatedImage();
        });
        this.setToolTipText(Text.titleCase(sprites.getPrayer()));
        this.setVerticalAlignment(0);
        this.setHorizontalAlignment(0);
        this.setPreferredSize(SIZE);
        this.setMaximumSize(SIZE);
        this.setMinimumSize(SIZE);
        this.updatePrayerData(this.data);
    }

    private void updateActivatedImage() {
        if (this.availableImage != null && this.activatedImage != null) {
            this.activatedImage = ImgUtil.overlapImages(this.availableImage, this.activatedImage);
            this.updatePrayerData(this.data);
        }

    }

    public void updatePrayerData(PrayerData updatedData) {
        if (this.data.getPrayer().equals(updatedData.getPrayer())) {
            this.data = updatedData;
            BufferedImage icon = this.data.isAvailable() ? this.availableImage : this.unavailableImage;
            if (this.data.isActivated()) {
                icon = this.activatedImage;
            }

            if (icon != null) {
                this.setIcon(new ImageIcon(icon));
            }

            this.revalidate();
            this.repaint();
        }
    }
}
