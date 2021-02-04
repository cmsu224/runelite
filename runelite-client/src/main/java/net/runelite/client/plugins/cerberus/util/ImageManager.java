//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.util;

import java.awt.image.BufferedImage;
import net.runelite.api.Prayer;
import net.runelite.client.plugins.cerberus.CerberusPlugin;
import net.runelite.client.plugins.cerberus.CerberusConfig.InfoBoxComponentSize;
import net.runelite.client.plugins.cerberus.domain.Phase;
import net.runelite.client.util.ImageUtil;

public final class ImageManager {
    private static double RESIZE_FACTOR = 1.25D;
    private static final BufferedImage[][] images = new BufferedImage[3][6];

    public static BufferedImage getCerberusBufferedImage(Phase phase, Prayer prayer, InfoBoxComponentSize size) {
        return phase == Phase.AUTO ? getCerberusPrayerBufferedImage(prayer, size) : getCerberusPhaseBufferedImage(phase, size);
    }

    private static BufferedImage getCerberusPrayerBufferedImage(Prayer prayer, InfoBoxComponentSize size) {
        String path;
        byte imgIdx;
        switch(prayer) {
            case PROTECT_FROM_MAGIC:
            default:
                path = "cerberus_magic.png";
                imgIdx = 0;
                break;
            case PROTECT_FROM_MISSILES:
                path = "cerberus_range.png";
                imgIdx = 1;
                break;
            case PROTECT_FROM_MELEE:
                path = "cerberus_melee.png";
                imgIdx = 2;
        }

        return getBufferedImage(path, imgIdx, size);
    }

    private static BufferedImage getCerberusPhaseBufferedImage(Phase phase, InfoBoxComponentSize size) {
        String path;
        byte imgIdx;
        switch(phase) {
            case TRIPLE:
            default:
                path = "cerberus_triple.png";
                imgIdx = 3;
                break;
            case GHOSTS:
                path = "cerberus_ghosts.png";
                imgIdx = 4;
                break;
            case LAVA:
                path = "cerberus_lava.png";
                imgIdx = 5;
        }

        return getBufferedImage(path, imgIdx, size);
    }

    private static BufferedImage getBufferedImage(String path, int imgIdx, InfoBoxComponentSize size) {
        BufferedImage img = ImageUtil.getResourceStreamFromClass(CerberusPlugin.class, path);
        int resize = (int)((double)size.getSize() / RESIZE_FACTOR);
        switch(size) {
            case SMALL:
            default:
                if (images[0][imgIdx] == null) {
                    images[0][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
                }

                return images[0][imgIdx];
            case MEDIUM:
                if (images[1][imgIdx] == null) {
                    images[1][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
                }

                return images[1][imgIdx];
            case LARGE:
                if (images[2][imgIdx] == null) {
                    images[2][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
                }

                return images[2][imgIdx];
        }
    }

    private ImageManager() {
    }
}
