//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.cerberus.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.TextComponent;

public class InfoBoxComponent implements LayoutableRenderableEntity {
    private final Rectangle bounds = new Rectangle();
    private Point preferredLocation = new Point();
    private Dimension preferredSize;
    private Font font;
    private String text;
    private String title;
    private Color textColor;
    private Color backgroundColor;
    private BufferedImage image;
    private int textXOffset;
    private int textYOffset;
    private int titleXOffset;

    public InfoBoxComponent() {
        this.textColor = Color.WHITE;
        this.textXOffset = -3;
        this.textYOffset = -2;
        this.titleXOffset = 2;
    }

    public Dimension render(Graphics2D graphics2D) {
        if (this.image == null) {
            return null;
        } else {
            graphics2D.setFont(this.font);
            int baseX = this.preferredLocation.x;
            int baseY = this.preferredLocation.y;
            int size = Math.max(this.preferredSize.width, this.preferredSize.height);
            Rectangle rectangle = new Rectangle(baseX, baseY, size, size);
            (new BackgroundComponent(this.backgroundColor, rectangle, true)).render(graphics2D);
            int imageX = baseX + (size - this.image.getWidth((ImageObserver)null)) / 2;
            int imageY = baseY + (size - this.image.getHeight((ImageObserver)null)) / 2;
            graphics2D.drawImage(this.image, imageX, imageY, (ImageObserver)null);
            FontMetrics fontMetrics = graphics2D.getFontMetrics();
            TextComponent textComponent;
            if (this.title != null && !this.title.isEmpty()) {
                textComponent = new TextComponent();
                textComponent.setColor(this.textColor);
                textComponent.setText(this.title);
                textComponent.setPosition(new Point(baseX + this.titleXOffset, baseY + fontMetrics.getHeight()));
                textComponent.render(graphics2D);
            }

            if (this.text != null && !this.text.isEmpty()) {
                textComponent = new TextComponent();
                textComponent.setColor(this.textColor);
                textComponent.setText(this.text);
                textComponent.setPosition(new Point(baseX + (size - fontMetrics.stringWidth(this.text)) + this.textXOffset, baseY + size + this.textYOffset));
                textComponent.render(graphics2D);
            }

            this.bounds.setBounds(rectangle);
            return rectangle.getSize();
        }
    }

    public void setPreferredLocation(Point preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setTextXOffset(int textXOffset) {
        this.textXOffset = textXOffset;
    }

    public void setTextYOffset(int textYOffset) {
        this.textYOffset = textYOffset;
    }

    public void setTitleXOffset(int titleXOffset) {
        this.titleXOffset = titleXOffset;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }
}
