//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.plugins.aztobextra.config.FontMets;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.util.ColorUtil;

public class SituationalTickOverlay extends Overlay {
    private final Client client;
    private final AzEasyTobConfig config;
    private final AzEasyTobPlugin plugin;

    @Inject
    SituationalTickOverlay(Client client, AzEasyTobConfig config, AzEasyTobPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    public Dimension render(Graphics2D graphics) {
        Player p;
        Integer tick;
        Point canvasPoint;
        if (this.plugin.getConfig().debug()) {
            p = this.client.getLocalPlayer();
            if (p != null) {
                tick = (Integer)this.plugin.getAttAnimTicks().get(p);
                if (tick != null) {
                    canvasPoint = p.getCanvasTextLocation(graphics, tick + "", 100);
                    this.renderTextLocation(graphics, canvasPoint, tick + "", tick == 1 ? Color.green : Color.WHITE, FontManager.getRunescapeBoldFont(), true, -1);
                }
            }
        }

        if (this.plugin.isInTheatre() && this.config.situationalAttTicks()) {
            p = this.client.getLocalPlayer();
            if (p != null) {
                if (isInBloatRegion(this.client)) {
                    tick = (Integer)this.plugin.getAttAnimTicks().get(p);
                    if (tick != null) {
                        canvasPoint = p.getCanvasTextLocation(graphics, tick + "", 100);
                        this.renderTextLocation(graphics, canvasPoint, tick + "", tick == 1 ? Color.green : Color.WHITE, FontManager.getRunescapeBoldFont(), true, -1);
                    }
                } else if (isInXarpRegion(this.client)) {
                    Iterator var7 = this.plugin.getAttAnimTicks().keySet().iterator();

                    while(var7.hasNext()) {
                        Player p2 = (Player)var7.next();
                        tick = (Integer)this.plugin.getAttAnimTicks().get(p2);
                        canvasPoint = p2.getCanvasTextLocation(graphics, tick + "", 100);
                        this.renderTextLocation(graphics, canvasPoint, tick + "", tick == 1 ? Color.green : Color.WHITE);
                    }
                }
            }
        }

        return null;
    }

    private void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color) {
        this.renderTextLocation(graphics, txtLoc, text, color, (Font)null, true, -1);
    }

    private void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color, Font font, boolean shade, int size) {
        if (!Strings.isNullOrEmpty(text)) {
            if (txtLoc != null) {
                int x = txtLoc.getX();
                int y = txtLoc.getY();
                if (font == null) {
                    if (this.config.fontType() == FontMets.Bold) {
                        graphics.setFont(FontManager.getRunescapeBoldFont());
                    } else if (this.config.fontType() == FontMets.Regular) {
                        graphics.setFont(FontManager.getRunescapeFont());
                    } else if (this.config.fontType() == FontMets.Small) {
                        graphics.setFont(FontManager.getRunescapeSmallFont());
                    } else {
                        graphics.setFont(FontManager.getDefaultFont());
                    }
                } else {
                    graphics.setFont(font);
                }

                if (graphics.getFont() != null && size != -1) {
                    graphics.setFont(new Font(graphics.getFont().getName(), 1, size != -1 ? size : graphics.getFont().getSize()));
                }

                graphics.setColor(Color.BLACK);
                if (this.config.shadeText() && shade) {
                    graphics.drawString(text, x, y + 1);
                    graphics.drawString(text, x, y - 1);
                    graphics.drawString(text, x + 1, y);
                    graphics.drawString(text, x - 1, y);
                } else {
                    graphics.drawString(text, x + 1, y + 1);
                }

                graphics.setColor(ColorUtil.colorWithAlpha(color, 255));
                graphics.drawString(text, x, y);
            }
        }
    }

    private static boolean isInBloatRegion(Client client) {
        return client.getMapRegions() != null && client.getMapRegions().length > 0 && Arrays.stream(client.getMapRegions()).anyMatch((s) -> {
            return s == 13125;
        });
    }

    private static boolean isInXarpRegion(Client client) {
        return client.getMapRegions() != null && client.getMapRegions().length > 0 && Arrays.stream(client.getMapRegions()).anyMatch((s) -> {
            return s == 12612;
        });
    }
}
