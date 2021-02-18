//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus.marker;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class AbstractMarker {
    private BufferedImage baseImage;

    public AbstractMarker() {
    }

    public BufferedImage getImage(int size) {
        BufferedImage baseImage = this.getBaseImage();
        if (baseImage == null) {
            return null;
        } else {
            double height = baseImage.getHeight() > 0 ? (double)baseImage.getHeight() : 1.0D;
            double scale = (double)size / height;
            int newWidth = (int)Math.ceil(scale * (double)baseImage.getWidth());
            BufferedImage realImage = new BufferedImage(newWidth, size, baseImage.getType());
            Graphics2D g2d = realImage.createGraphics();
            g2d.drawImage(baseImage, 0, 0, newWidth, size, (ImageObserver)null);
            g2d.dispose();
            return realImage;
        }
    }

    public void setBaseImage(BufferedImage baseImage) {
        this.baseImage = baseImage;
    }

    public BufferedImage getBaseImage() {
        return this.baseImage;
    }
}
