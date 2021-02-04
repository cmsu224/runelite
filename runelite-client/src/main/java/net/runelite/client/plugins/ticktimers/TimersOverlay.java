//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.ticktimers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.ticktimers.NPCContainer.AttackStyle;
import net.runelite.client.plugins.ticktimers.NPCContainer.BossMonsters;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.LocalPoint;
import java.awt.Polygon;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

@Singleton
public class TimersOverlay extends Overlay {
    private static final int TICK_PIXEL_SIZE = 60;
    private static final int BOX_WIDTH = 10;
    private static final int BOX_HEIGHT = 5;
    private final TickTimersPlugin plugin;
    private final TickTimersConfig config;
    private final Client client;

    @Inject
    TimersOverlay(TickTimersPlugin plugin, TickTimersConfig config, Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    public Dimension render(Graphics2D graphics) {
        TreeMap<Integer, TreeMap<Integer, Prayer>> tickAttackMap = new TreeMap();
        Iterator var3 = this.plugin.getNpcContainers().iterator();

        while(true) {
            NPCContainer npc;
            int ticksLeft;
            AttackStyle attackStyle;
            do {
                do {
                    do {
                        if (!var3.hasNext()) {
                            if (!tickAttackMap.isEmpty()) {
                                var3 = tickAttackMap.entrySet().iterator();

                                while(var3.hasNext()) {
                                    Entry<Integer, TreeMap<Integer, Prayer>> tickEntry = (Entry)var3.next();
                                    Entry<Integer, Prayer> attackEntry = ((TreeMap)tickEntry.getValue()).firstEntry();
                                    Prayer prayer = (Prayer)attackEntry.getValue();
                                    if (prayer != null) {
                                        this.renderDescendingBoxes(graphics, prayer, (Integer)tickEntry.getKey());
                                    }
                                }
                            }

                            return null;
                        }

                        npc = (NPCContainer)var3.next();
                    } while(npc.getNpc() == null);

                    ticksLeft = npc.getTicksUntilAttack();
                    List<WorldPoint> hitSquares = getHitSquares(npc.getNpc().getWorldLocation(), npc.getNpcSize(), 1, false);
                    attackStyle = npc.getAttackStyle();
                    if (this.config.showHitSquares() && attackStyle.getName().equals("Melee")) {
                        Iterator var8 = hitSquares.iterator();

                        while(var8.hasNext()) {
                            WorldPoint p = (WorldPoint)var8.next();
                            drawTiles(graphics, this.client, p, this.client.getLocalPlayer().getWorldLocation(), attackStyle.getColor(), 0, 0, 50);
                        }
                    }
                } while(ticksLeft <= 0);
            } while(this.config.ignoreNonAttacking() && npc.getNpcInteracting() != this.client.getLocalPlayer() && npc.getMonsterType() != BossMonsters.GENERAL_GRAARDOR);

            if (npc.getMonsterType() == BossMonsters.GENERAL_GRAARDOR && npc.getNpcInteracting() != this.client.getLocalPlayer()) {
                attackStyle = AttackStyle.RANGE;
            }

            String ticksLeftStr = String.valueOf(ticksLeft);
            int font = this.config.fontStyle().getFont();
            boolean shadows = this.config.shadows();
            Color color = ticksLeft <= 1 ? Color.WHITE : attackStyle.getColor();
            if (!this.config.changeTickColor()) {
                color = attackStyle.getColor();
            }

            Point canvasPoint = npc.getNpc().getCanvasTextLocation(graphics, ticksLeftStr, 0);
            renderTextLocation(graphics, ticksLeftStr, this.config.textSize(), font, color, canvasPoint, shadows, 0);
            if (this.config.showPrayerWidgetHelper() && attackStyle.getPrayer() != null) {
                Rectangle bounds = renderPrayerOverlay(graphics, this.client, attackStyle.getPrayer(), color);
                if (bounds != null) {
                    this.renderTextLocation(graphics, ticksLeftStr, 16, this.config.fontStyle().getFont(), color, this.centerPoint(bounds), shadows);
                }
            }

            if (this.config.guitarHeroMode()) {
                TreeMap<Integer, Prayer> attacks = (TreeMap)tickAttackMap.computeIfAbsent(ticksLeft, (k) -> {
                    return new TreeMap();
                });
                int priority = 999;
                switch(npc.getMonsterType()) {
                    case SERGEANT_STRONGSTACK:
                        priority = 3;
                        break;
                    case SERGEANT_STEELWILL:
                        priority = 1;
                        break;
                    case SERGEANT_GRIMSPIKE:
                        priority = 2;
                        break;
                    case GENERAL_GRAARDOR:
                        priority = 0;
                }

                attacks.putIfAbsent(Integer.valueOf(priority), attackStyle.getPrayer());
            }
        }
    }

    public static List<WorldPoint> getHitSquares(WorldPoint npcLoc, int npcSize, int thickness, boolean includeUnder)
    {
        List<WorldPoint> little = new WorldArea(npcLoc, npcSize, npcSize).toWorldPointList();
        List<WorldPoint> big = new WorldArea(npcLoc.getX() - thickness, npcLoc.getY() - thickness, npcSize + (thickness * 2), npcSize + (thickness * 2), npcLoc.getPlane()).toWorldPointList();
        if (!includeUnder)
        {
            big.removeIf(little::contains);
        }
        return big;
    }

    public static void drawTiles(Graphics2D graphics, Client client, WorldPoint point, WorldPoint playerPoint, Color color, int strokeWidth, int outlineAlpha, int fillAlpha)
    {
        if (point.distanceTo(playerPoint) >= 32)
        {
            return;
        }
        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null)
        {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null)
        {
            return;
        }
        drawStrokeAndFillPoly(graphics, color, strokeWidth, outlineAlpha, fillAlpha, poly);
    }

    public static void drawStrokeAndFillPoly(Graphics2D graphics, Color color, int strokeWidth, int outlineAlpha, int fillAlpha, Polygon poly)
    {
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
        graphics.setStroke(new BasicStroke(strokeWidth));
        graphics.draw(poly);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
        graphics.fill(poly);
    }

    private void renderDescendingBoxes(Graphics2D graphics, Prayer prayer, int tick) {
        Color color = tick == 1 ? Color.RED : Color.ORANGE;
        Widget prayerWidget = getWidget2(prayer);
        int baseX = (int)prayerWidget.getBounds().getX();
        baseX = (int)((double)baseX + prayerWidget.getBounds().getWidth() / 2.0D);
        baseX -= 5;
        int baseY = (int)prayerWidget.getBounds().getY() - tick * 60 - 5;
        baseY = (int)((double)baseY + (60.0D - (double)(this.plugin.getLastTickTime() + 600L - System.currentTimeMillis()) / 600.0D * 60.0D));
        Rectangle boxRectangle = new Rectangle(10, 5);
        boxRectangle.translate(baseX, baseY);
        renderFilledPolygon(graphics, boxRectangle, color);
    }

    public static void renderFilledPolygon(Graphics2D graphics, Shape poly, Color color)
    {
        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(poly);
        graphics.fill(poly);
        graphics.setStroke(originalStroke);
    }

    private void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows) {
        graphics.setFont(new Font("Arial", fontStyle, fontSize));
        if (canvasPoint != null) {
            Point canvasCenterPoint = new Point(canvasPoint.getX() - 3, canvasPoint.getY() + 6);
            Point canvasCenterPoint_shadow = new Point(canvasPoint.getX() - 2, canvasPoint.getY() + 7);
            if (shadows) {
                OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
            }

            OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }

    }

    public static void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows, int yOffset)
    {
        graphics.setFont(new Font("Arial", fontStyle, fontSize));
        if (canvasPoint != null)
        {
            final Point canvasCenterPoint = new Point(
                    canvasPoint.getX(),
                    canvasPoint.getY() + yOffset);
            final Point canvasCenterPoint_shadow = new Point(
                    canvasPoint.getX() + 1,
                    canvasPoint.getY() + 1 + yOffset);
            if (shadows)
            {
                OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
            }
            OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }
    }

    private static Polygon rectangleToPolygon(Rectangle rect)
    {
        int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
        int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};

        return new Polygon(xpoints, ypoints, 4);
    }

    public Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
    {
        Widget widget = getWidget2(prayer);
        Rectangle bounds = widget.getBounds();
        OverlayUtil.renderPolygon(graphics, rectangleToPolygon(bounds), color);
        return bounds;
    }

    public Widget getWidget2(Prayer prayer){
        Varbits var = prayer.getVarbit();
        //prayer group id: 541, child id - melee: 19, mage: 17, range: 18
        Widget test = this.client.getWidget(541, 32);
        //if melee then
        if (var.getId() == 4118){
            test = this.client.getWidget(541,19);
        }
        //if mage then
        if (var.getId() == 4116){
            test = this.client.getWidget(541,17);
        }
        //if range then
        if (var.getId() == 4117){
            test = this.client.getWidget(541,18);
        }

        return test;
    }

    private Point centerPoint(Rectangle rect) {
        int x = (int)(rect.getX() + rect.getWidth() / 2.0D);
        int y = (int)(rect.getY() + rect.getHeight() / 2.0D);
        return new Point(x, y);
    }
}
