//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.inferno;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.inferno.InfernoNPC.Attack;
import net.runelite.client.plugins.inferno.InfernoNPC.Type;
import net.runelite.client.plugins.inferno.displaymodes.InfernoPrayerDisplayMode;
import net.runelite.client.plugins.inferno.displaymodes.InfernoSafespotDisplayMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class InfernoOverlay extends Overlay {
    private static final int TICK_PIXEL_SIZE = 60;
    private static final int BOX_WIDTH = 10;
    private static final int BOX_HEIGHT = 5;
    private final InfernoPlugin plugin;
    private final InfernoConfig config;
    private final Client client;

    @Inject
    private InfernoOverlay(Client client, InfernoPlugin plugin, InfernoConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    public Dimension render(Graphics2D graphics) {
        Widget meleePrayerWidget = this.client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
        Widget rangePrayerWidget = this.client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
        Widget magicPrayerWidget = this.client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
        if (this.config.indicateObstacles()) {
            this.renderObstacles(graphics);
        }

        if (this.config.safespotDisplayMode() == InfernoSafespotDisplayMode.AREA) {
            this.renderAreaSafepots(graphics);
        } else if (this.config.safespotDisplayMode() == InfernoSafespotDisplayMode.INDIVIDUAL_TILES) {
            this.renderIndividualTilesSafespots(graphics);
        }

        Iterator var5 = this.plugin.getInfernoNpcs().iterator();

        while(var5.hasNext()) {
            InfernoNPC infernoNPC = (InfernoNPC)var5.next();
            if (infernoNPC.getNpc().getConvexHull() != null) {
                if (this.config.indicateNonSafespotted() && this.plugin.isNormalSafespots(infernoNPC) && infernoNPC.canAttack(this.client, this.client.getLocalPlayer().getWorldLocation())) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.RED);
                }

                if (this.config.indicateTemporarySafespotted() && this.plugin.isNormalSafespots(infernoNPC) && infernoNPC.canMoveToAttack(this.client, this.client.getLocalPlayer().getWorldLocation(), this.plugin.getObstacles())) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.YELLOW);
                }

                if (this.config.indicateSafespotted() && this.plugin.isNormalSafespots(infernoNPC)) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.GREEN);
                }

                if (this.config.indicateNibblers() && infernoNPC.getType() == Type.NIBBLER && (!this.config.indicateCentralNibbler() || this.plugin.getCentralNibbler() != infernoNPC)) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
                }

                if (this.config.indicateCentralNibbler() && infernoNPC.getType() == Type.NIBBLER && this.plugin.getCentralNibbler() == infernoNPC) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.BLUE);
                }

                if (this.config.indicateActiveHealerJad() && infernoNPC.getType() == Type.HEALER_JAD && infernoNPC.getNpc().getInteracting() != this.client.getLocalPlayer()) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
                }

                if (this.config.indicateActiveHealerZuk() && infernoNPC.getType() == Type.HEALER_ZUK && infernoNPC.getNpc().getInteracting() != this.client.getLocalPlayer()) {
                    OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
                }
            }

            if (this.plugin.isIndicateNpcPosition(infernoNPC)) {
                this.renderNpcLocation(graphics, infernoNPC);
            }

            if (this.plugin.isTicksOnNpc(infernoNPC) && infernoNPC.getTicksTillNextAttack() > 0) {
                this.renderTicksOnNpc(graphics, infernoNPC, infernoNPC.getNpc());
            }

            if (this.config.ticksOnNpcZukShield() && infernoNPC.getType() == Type.ZUK && this.plugin.getZukShield() != null && infernoNPC.getTicksTillNextAttack() > 0) {
                this.renderTicksOnNpc(graphics, infernoNPC, this.plugin.getZukShield());
            }
        }

        boolean prayerWidgetHidden = meleePrayerWidget == null || rangePrayerWidget == null || magicPrayerWidget == null || meleePrayerWidget.isHidden() || rangePrayerWidget.isHidden() || magicPrayerWidget.isHidden();
        if ((this.config.prayerDisplayMode() == InfernoPrayerDisplayMode.PRAYER_TAB || this.config.prayerDisplayMode() == InfernoPrayerDisplayMode.BOTH) && (!prayerWidgetHidden || this.config.alwaysShowPrayerHelper())) {
            this.renderPrayerIconOverlay(graphics);
            if (this.config.descendingBoxes()) {
                this.renderDescendingBoxes(graphics);
            }
        }

        return null;
    }

    private void renderObstacles(Graphics2D graphics) {
        Iterator var2 = this.plugin.getObstacles().iterator();

        while(var2.hasNext()) {
            WorldPoint worldPoint = (WorldPoint)var2.next();
            LocalPoint localPoint = LocalPoint.fromWorld(this.client, worldPoint);
            if (localPoint != null) {
                Polygon tilePoly = Perspective.getCanvasTilePoly(this.client, localPoint);
                if (tilePoly != null) {
                    OverlayUtil.renderPolygon(graphics, tilePoly, Color.BLUE);
                }
            }
        }

    }

    private void renderAreaSafepots(Graphics2D graphics) {
        Iterator var2 = this.plugin.getSafeSpotAreas().keySet().iterator();

        while(true) {
            Color colorEdge1;
            Color colorEdge2;
            ArrayList allEdges;
            int edgeSizeSquared;
            int[][] checkEdge;
            do {
                int safeSpotId;
                Color colorFill;
                label56:
                while(true) {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        safeSpotId = (Integer)var2.next();
                    } while(safeSpotId > 6);

                    colorEdge2 = null;
                    switch(safeSpotId) {
                        case 0:
                            colorEdge1 = Color.WHITE;
                            colorFill = Color.WHITE;
                            break label56;
                        case 1:
                            colorEdge1 = Color.RED;
                            colorFill = Color.RED;
                            break label56;
                        case 2:
                            colorEdge1 = Color.GREEN;
                            colorFill = Color.GREEN;
                            break label56;
                        case 3:
                            colorEdge1 = Color.BLUE;
                            colorFill = Color.BLUE;
                            break label56;
                        case 4:
                            colorEdge1 = Color.RED;
                            colorEdge2 = Color.GREEN;
                            colorFill = Color.YELLOW;
                            break label56;
                        case 5:
                            colorEdge1 = Color.RED;
                            colorEdge2 = Color.BLUE;
                            colorFill = new Color(255, 0, 255);
                            break label56;
                        case 6:
                            colorEdge1 = Color.GREEN;
                            colorEdge2 = Color.BLUE;
                            colorFill = new Color(0, 255, 255);
                            break label56;
                    }
                }

                allEdges = new ArrayList();
                edgeSizeSquared = 0;
                Iterator var9 = ((List)this.plugin.getSafeSpotAreas().get(safeSpotId)).iterator();

                while(var9.hasNext()) {
                    WorldPoint worldPoint = (WorldPoint)var9.next();
                    LocalPoint localPoint = LocalPoint.fromWorld(this.client, worldPoint);
                    if (localPoint != null) {
                        Polygon tilePoly = Perspective.getCanvasTilePoly(this.client, localPoint);
                        if (tilePoly != null) {
                            graphics.setColor(new Color(colorFill.getRed(), colorFill.getGreen(), colorFill.getBlue(), 10));
                            graphics.fill(tilePoly);

                            int[][] edge1 = new int[][]{{tilePoly.xpoints[0], tilePoly.ypoints[0]}, {tilePoly.xpoints[1], tilePoly.ypoints[1]}};
                            edgeSizeSquared = (int)((double)edgeSizeSquared + Math.pow((double)(tilePoly.xpoints[0] - tilePoly.xpoints[1]), 2.0D) + Math.pow((double)(tilePoly.ypoints[0] - tilePoly.ypoints[1]), 2.0D));
                            allEdges.add(edge1);
                            checkEdge = new int[][]{{tilePoly.xpoints[1], tilePoly.ypoints[1]}, {tilePoly.xpoints[2], tilePoly.ypoints[2]}};
                            edgeSizeSquared = (int)((double)edgeSizeSquared + Math.pow((double)(tilePoly.xpoints[1] - tilePoly.xpoints[2]), 2.0D) + Math.pow((double)(tilePoly.ypoints[1] - tilePoly.ypoints[2]), 2.0D));
                            allEdges.add(checkEdge);
                            int[][] edge3 = new int[][]{{tilePoly.xpoints[2], tilePoly.ypoints[2]}, {tilePoly.xpoints[3], tilePoly.ypoints[3]}};
                            edgeSizeSquared = (int)((double)edgeSizeSquared + Math.pow((double)(tilePoly.xpoints[2] - tilePoly.xpoints[3]), 2.0D) + Math.pow((double)(tilePoly.ypoints[2] - tilePoly.ypoints[3]), 2.0D));
                            allEdges.add(edge3);
                            int[][] edge4 = new int[][]{{tilePoly.xpoints[3], tilePoly.ypoints[3]}, {tilePoly.xpoints[0], tilePoly.ypoints[0]}};
                            edgeSizeSquared = (int)((double)edgeSizeSquared + Math.pow((double)(tilePoly.xpoints[3] - tilePoly.xpoints[0]), 2.0D) + Math.pow((double)(tilePoly.ypoints[3] - tilePoly.ypoints[0]), 2.0D));
                            allEdges.add(edge4);
                        }
                    }
                }
            } while(allEdges.size() <= 0);

            edgeSizeSquared /= allEdges.size();
            int toleranceSquared = (int)Math.ceil((double)(edgeSizeSquared / 6));

            for(int i = 0; i < allEdges.size(); ++i) {
                int[][] baseEdge = (int[][])allEdges.get(i);
                boolean duplicate = false;

                for(int j = 0; j < allEdges.size(); ++j) {
                    if (i != j) {
                        checkEdge = (int[][])allEdges.get(j);
                        if (this.edgeEqualsEdge(baseEdge, checkEdge, toleranceSquared)) {
                            duplicate = true;
                            break;
                        }
                    }
                }

                if (!duplicate) {
                    renderFullLine(graphics, baseEdge, colorEdge1);
                    if (colorEdge2 != null) {
                        renderDashedLine(graphics, baseEdge, colorEdge2);
                    }
                }
            }
        }
    }

    public static void renderDashedLine(Graphics2D graphics, int[][] line, Color color)
    {
        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        graphics.drawLine(line[0][0], line[0][1], line[1][0], line[1][1]);
        graphics.setStroke(originalStroke);
    }

    public static void renderFullLine(Graphics2D graphics, int[][] line, Color color)
    {
        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.drawLine(line[0][0], line[0][1], line[1][0], line[1][1]);
        graphics.setStroke(originalStroke);
    }

    private void renderIndividualTilesSafespots(Graphics2D graphics) {
        Iterator var2 = this.plugin.getSafeSpotMap().keySet().iterator();

        while(true) {
            Polygon tilePoly;
            Color color;
            label41:
            while(true) {
                int safeSpotId;
                do {
                    LocalPoint localPoint;
                    do {
                        WorldPoint worldPoint;
                        do {
                            if (!var2.hasNext()) {
                                return;
                            }

                            worldPoint = (WorldPoint)var2.next();
                            safeSpotId = (Integer)this.plugin.getSafeSpotMap().get(worldPoint);
                        } while(safeSpotId > 6);

                        localPoint = LocalPoint.fromWorld(this.client, worldPoint);
                    } while(localPoint == null);

                    tilePoly = Perspective.getCanvasTilePoly(this.client, localPoint);
                } while(tilePoly == null);

                switch(safeSpotId) {
                    case 0:
                        color = Color.WHITE;
                        break label41;
                    case 1:
                        color = Color.RED;
                        break label41;
                    case 2:
                        color = Color.GREEN;
                        break label41;
                    case 3:
                        color = Color.BLUE;
                        break label41;
                    case 4:
                        color = new Color(255, 255, 0);
                        break label41;
                    case 5:
                        color = new Color(255, 0, 255);
                        break label41;
                    case 6:
                        color = new Color(0, 255, 255);
                        break label41;
                }
            }

            OverlayUtil.renderPolygon(graphics, tilePoly, color);
        }
    }

    private void renderTicksOnNpc(Graphics2D graphics, InfernoNPC infernoNPC, NPC renderOnNPC) {
        Color color = infernoNPC.getTicksTillNextAttack() != 1 && (infernoNPC.getType() != Type.BLOB || infernoNPC.getTicksTillNextAttack() != 4) ? infernoNPC.getNextAttack().getNormalColor() : infernoNPC.getNextAttack().getCriticalColor();
        Point canvasPoint = renderOnNPC.getCanvasTextLocation(graphics, String.valueOf(infernoNPC.getTicksTillNextAttack()), 0);
        renderTextLocation(graphics, String.valueOf(infernoNPC.getTicksTillNextAttack()), this.plugin.getTextSize(), this.plugin.getFontStyle().getFont(), color, canvasPoint, false, 0);
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

    private void renderNpcLocation(Graphics2D graphics, InfernoNPC infernoNPC) {
        LocalPoint localPoint = LocalPoint.fromWorld(this.client, infernoNPC.getNpc().getWorldLocation());
        if (localPoint != null) {
            Polygon tilePolygon = Perspective.getCanvasTilePoly(this.client, localPoint);
            if (tilePolygon != null) {
                OverlayUtil.renderPolygon(graphics, tilePolygon, Color.BLUE);
            }
        }

    }

    private void renderDescendingBoxes(Graphics2D graphics) {
        Iterator var2 = this.plugin.getUpcomingAttacks().keySet().iterator();

        while(var2.hasNext()) {
            Integer tick = (Integer)var2.next();
            Map<Attack, Integer> attackPriority = (Map)this.plugin.getUpcomingAttacks().get(tick);
            int bestPriority = 999;
            Attack bestAttack = null;
            Iterator var7 = attackPriority.entrySet().iterator();

            while(var7.hasNext()) {
                Entry<Attack, Integer> attackEntry = (Entry)var7.next();
                if ((Integer)attackEntry.getValue() < bestPriority) {
                    bestAttack = (Attack)attackEntry.getKey();
                    bestPriority = (Integer)attackEntry.getValue();
                }
            }

            var7 = attackPriority.keySet().iterator();

            while(var7.hasNext()) {
                Attack currentAttack = (Attack)var7.next();
                Color color = tick == 1 && currentAttack == bestAttack ? Color.RED : Color.ORANGE;
                //Widget prayerWidget = this.client.getWidget(currentAttack.getPrayer().getWidgetInfo());
                Widget prayerWidget = getWidget(currentAttack.getPrayer());
                int baseX = (int)prayerWidget.getBounds().getX();
                baseX = (int)((double)baseX + prayerWidget.getBounds().getWidth() / 2.0D);
                baseX -= 5;
                int baseY = (int)prayerWidget.getBounds().getY() - tick * 60 - 5;
                baseY = (int)((double)baseY + (60.0D - (double)(this.plugin.getLastTick() + 600L - System.currentTimeMillis()) / 600.0D * 60.0D));
                Rectangle boxRectangle = new Rectangle(10, 5);
                boxRectangle.translate(baseX, baseY);
                if (currentAttack == bestAttack) {
                    renderFilledPolygon(graphics, boxRectangle, color);
                } else if (this.config.indicateNonPriorityDescendingBoxes()) {
                    renderOutlinePolygon(graphics, boxRectangle, color);
                }
            }
        }

    }

    public static void renderOutlinePolygon(Graphics2D graphics, Shape poly, Color color)
    {
        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(poly);
        graphics.setStroke(originalStroke);
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

    private void renderPrayerIconOverlay(Graphics2D graphics) {
        if (this.plugin.getClosestAttack() != null) {
            Attack prayerForAttack = null;
            if (this.client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC)) {
                prayerForAttack = Attack.MAGIC;
            } else if (this.client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES)) {
                prayerForAttack = Attack.RANGED;
            } else if (this.client.isPrayerActive(Prayer.PROTECT_FROM_MELEE)) {
                prayerForAttack = Attack.MELEE;
            }

            if (this.plugin.getClosestAttack() != prayerForAttack || this.config.indicateWhenPrayingCorrectly()) {
                //Widget prayerWidget = this.client.getWidget(this.plugin.getClosestAttack().getPrayer().getWidgetInfo());
                Widget prayerWidget = getWidget(this.plugin.getClosestAttack().getPrayer());
                Rectangle prayerRectangle = new Rectangle((int)prayerWidget.getBounds().getWidth(), (int)prayerWidget.getBounds().getHeight());
                prayerRectangle.translate((int)prayerWidget.getBounds().getX(), (int)prayerWidget.getBounds().getY());
                Color prayerColor;
                if (this.plugin.getClosestAttack() == prayerForAttack) {
                    prayerColor = Color.GREEN;
                } else {
                    prayerColor = Color.RED;
                }

                renderOutlinePolygon(graphics, prayerRectangle, prayerColor);
            }
        }

    }

    private Widget getWidget(Prayer prayer){
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

    private boolean edgeEqualsEdge(int[][] edge1, int[][] edge2, int toleranceSquared) {
        return this.pointEqualsPoint(edge1[0], edge2[0], toleranceSquared) && this.pointEqualsPoint(edge1[1], edge2[1], toleranceSquared) || this.pointEqualsPoint(edge1[0], edge2[1], toleranceSquared) && this.pointEqualsPoint(edge1[1], edge2[0], toleranceSquared);
    }

    private boolean pointEqualsPoint(int[] point1, int[] point2, int toleranceSquared) {
        double distanceSquared = Math.pow((double)(point1[0] - point2[0]), 2.0D) + Math.pow((double)(point1[1] - point2[1]), 2.0D);
        return distanceSquared <= (double)toleranceSquared;
    }

}
