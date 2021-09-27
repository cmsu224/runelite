package net.runelite.client.plugins.cerberus.util;

import com.google.common.base.Strings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import static net.runelite.client.ui.overlay.OverlayUtil.renderPolygon;

public class OverlayUtil
{
    public static Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
    {
        //Widget widget = client.getWidget(prayer.getWidgetInfo());
        Widget widget = getWidget(prayer, client);
        if (widget == null)
        {
            return null;
        }

        Rectangle bounds = widget.getBounds();
        renderPolygon(graphics, rectangleToPolygon(bounds), color);
        return bounds;
    }

    private static Polygon rectangleToPolygon(Rectangle rect)
    {
        int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
        int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};

        return new Polygon(xpoints, ypoints, 4);
    }

    private static Widget getWidget(Prayer prayer, Client client){
        Varbits var = prayer.getVarbit();
        //prayer group id: 541, child id - melee: 19, mage: 17, range: 18
        Widget test = client.getWidget(541, 32);
        //if melee then
        if (var.getId() == 4118){
            test = client.getWidget(541,19);
        }
        //if mage then
        if (var.getId() == 4116){
            test = client.getWidget(541,17);
        }
        //if range then
        if (var.getId() == 4117){
            test = client.getWidget(541,18);
        }

        return test;
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
                renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
            }
            renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }
    }

    public static void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color)
    {
        if (Strings.isNullOrEmpty(text))
        {
            return;
        }

        int x = txtLoc.getX();
        int y = txtLoc.getY();

        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 1, y + 1);

        graphics.setColor(color);
        graphics.drawString(text, x, y);
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
}