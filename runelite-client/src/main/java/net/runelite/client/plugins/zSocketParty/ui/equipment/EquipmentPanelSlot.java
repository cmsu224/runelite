//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.ui.equipment;

import lombok.Getter;
import net.runelite.client.plugins.zSocketParty.ImgUtil;
import net.runelite.client.plugins.zSocketParty.data.GameItem;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class EquipmentPanelSlot extends JLabel
{
    private final int IMAGE_SIZE = 48; // Background is squared at 32x32, we want 50% bigger so 48x48
    private final BufferedImage background;
    private final BufferedImage placeholder;
    @Getter
    private GameItem item = null;

    EquipmentPanelSlot(final GameItem item, final BufferedImage image, final BufferedImage background, final BufferedImage placeholder)
    {
        super();

        this.background = background;
        this.placeholder = ImageUtil.resizeImage(ImgUtil.overlapImages(placeholder, background), IMAGE_SIZE, IMAGE_SIZE);

        setVerticalAlignment(JLabel.CENTER);
        setHorizontalAlignment(JLabel.CENTER);
        setGameItem(item, image);
    }

    public void setGameItem(final GameItem item, final BufferedImage image)
    {
        this.item = item;

        if (item == null || image == null)
        {
            setIcon(new ImageIcon(placeholder));
            setToolTipText(null);
            return;
        }

        setIcon(new ImageIcon(ImageUtil.resizeImage(ImgUtil.overlapImages(image, background), IMAGE_SIZE, IMAGE_SIZE)));
        setToolTipText(item.getName());
    }
}
