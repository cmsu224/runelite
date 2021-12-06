//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.decorhighlight;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class DecorSceneOverlay extends Overlay {
    private Client client;
    private DecorHighlightPlugin plugin;
    private DecorHighlightConfig config;
    private Boolean toggle = true;
    private int hID = 0;
    private int aoeSize = 1;
    private int dColor = 1;
    private Color aColor = Color.WHITE;
    private int startTick;
    private int tickCounter;

    @Inject
    public DecorSceneOverlay(Client client, DecorHighlightPlugin plugin, DecorHighlightConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPosition(OverlayPosition.DYNAMIC);
        startTick = plugin.getLastTick();
        tickCounter = 0;
    }

    public Dimension render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(Color.RED);
        Iterator var3 = this.client.getGraphicsObjects().iterator();
        Player player = this.client.getLocalPlayer();
        Iterator var2 = client.getNpcs().iterator();
        LocalPoint lp;
        NPC npc = null;
        NPCComposition npcComposition;
        int size;
        Polygon tilePoly;

        //projectiles
        List<Projectile> projectiles = this.client.getProjectiles();
        Iterator var70 = projectiles.iterator();
        //Iterator var71 = pro

        while(var70.hasNext()) {
            Projectile projectile = (Projectile)var70.next();
            int projectileId = projectile.getId();

            if(config.debugProjectiles() || parse_string(this.plugin.gameProjectileWhitelist, projectileId)){
                String str = "";
                if (config.debugProjectiles()) {
                    str = " - "+Integer.toString(projectileId);
                    aColor = config.highlightColor();
                    aoeSize = 1;
                }
                int ticksRemaining = projectile.getRemainingCycles() / 30;
                final int tickCycle = client.getTickCount() + ticksRemaining;

                if (config.trackProjectiles()){
                    int x = (int)projectile.getX();
                    int y = (int)projectile.getY();
                    lp = new LocalPoint(x, y);

                    if (aoeSize != 0){
                        tilePoly = Perspective.getCanvasTileAreaPoly(this.client, lp, aoeSize);
                    }else
                        tilePoly = null;

                    this.renderPoly(g, aColor, tilePoly, ticksRemaining + str, lp);
                }
            }
        }

        //npcs
        while(var2.hasNext()) {
            npc = (NPC)var2.next();

            if(config.debugNPCs() || parse_string(this.plugin.gameNPCsWhitelist, npc.getId())){
                npcComposition = npc.getTransformedComposition();
                if (npcComposition != null) {
                    size = npcComposition.getSize();

                    lp = LocalPoint.fromWorld(client, npc.getWorldLocation());
                    if (lp != null) {
                        lp = new LocalPoint(lp.getX() + size * 128 / 2 - 64, lp.getY() + size * 128 / 2 - 64);

                        String str = null;
                        if (config.debugNPCs()) {
                            str = Integer.toString(npc.getId());
                            aColor = config.highlightColor();
                            aoeSize = size;
                        }
                        if (aoeSize == 0){
                            aoeSize = size;
                        }

                        tilePoly = Perspective.getCanvasTileAreaPoly(this.client, lp, aoeSize);
                        this.renderPoly(g, aColor, tilePoly, str, lp);
                    }
                }
            }

            if (this.config.displayTick()) {
                ////TICK TIMERS
                if (config.debugNPCs() || parse_string(this.plugin.gameTickWhitelist, npc.getId())) {
                    int gTick = this.plugin.getLastTick();
                    if(gTick != startTick){
                        tickCounter = tickCounter - 1;
                        startTick = gTick;
                    }

                    if(tickCounter < 1){
                        tickCounter = aoeSize;
                    }

                    String ticksLeftStr = String.valueOf(tickCounter);
                    boolean shadows = true;
                    Color color = aColor;

                    if(tickCounter > 0 && tickCounter <= aoeSize && !config.debugNPCs()){
                        Point canvasPoint = npc.getCanvasTextLocation(g, ticksLeftStr, 0);
                        renderTextLocation(g, ticksLeftStr, config.fontSize(), 1, color, canvasPoint, shadows);
                    }

                }
            }
        }

        while(true) {
            GraphicsObject go;
            boolean debug;
            do {
                if (!var3.hasNext()) {
                    for(int x = 0; x < 104; ++x) {
                        for(int y = 0; y < 104; ++y) {
                            Tile t = this.client.getScene().getTiles()[this.client.getPlane()][x][y];
                            if (t != null) {
                                GroundObject decor = t.getGroundObject();
                                if (decor != null) {
                                    debug = this.client.isKeyPressed(86) && this.client.isKeyPressed(81);

                                    if (config.debugDecor() || parse_string(this.plugin.groundDecorWhitelist, decor.getId())) {
                                        String str = null;
                                        if (config.debugDecor()) {
                                            str = Integer.toString(decor.getId());
                                            aColor = config.highlightColor();
                                            aoeSize = 1;
                                        }

                                        lp = decor.getLocalLocation();
                                        int _x = (lp.getX() - 64) / 128;
                                        int _y = (lp.getY() - 64) / 128;
                                        this.tile(g, _x, _y, aColor, str);
                                    }
                                }



                                GameObject[] gameObjects = t.getGameObjects();
                                if (gameObjects != null) {
                                    GameObject[] var5 = gameObjects;
                                    int var6 = gameObjects.length;
                                    debug = this.client.isKeyPressed(86) && this.client.isKeyPressed(81);

                                    for(int var7 = 0; var7 < var6; ++var7) {
                                        GameObject gameObject = var5[var7];
                                        if (gameObject != null && gameObject.getSceneMinLocation().equals(t.getSceneLocation())) {
                                            if (gameObject != null && player.getLocalLocation().distanceTo(gameObject.getLocalLocation()) <= 2400) {
                                                if (config.debugGameObjects() || parse_string(this.plugin.gameObjectsWhitelist, gameObject.getId())) {
                                                    //OverlayUtil.renderTileOverlay(g, gameObject, "ID: " + gameObject.getId(), Color.GREEN);

                                                    String str = null;
                                                    if (config.debugGameObjects()) {
                                                        str = Integer.toString(gameObject.getId());
                                                        aColor = config.highlightColor();
                                                        aoeSize = 1;
                                                    }
                                                    lp = gameObject.getLocalLocation();
                                                    int _x = (lp.getX() - 64) / 128;
                                                    int _y = (lp.getY() - 64) / 128;
                                                    //this.tile(g, _x, _y, this.config.highlightColor(), str);

                                                    this.renderTileObject(g, gameObject, player, aColor);
                                                }

                                            }
                                        }
                                    }

                                }



                            }
                        }
                    }

                    return null;
                }

                go = (GraphicsObject)var3.next();
                debug = this.client.isKeyPressed(86) && this.client.isKeyPressed(81);
            } while(!debug && !parse_string(this.plugin.graphicsObjectWhitelist, go.getId()));

            String str = null;
            if (config.debugGraphicObjects()) {
                str = Integer.toString(go.getId());
                aColor = config.highlightColor();
                aoeSize = 1;
            }

            lp = go.getLocation();
            int x = (lp.getX() - 64) / 128;
            int y = (lp.getY() - 64) / 128;
            this.tile(g, x, y, aColor, str);

        }
    }

    private void tile(Graphics g, int x, int y, Color c, String textLine1) {
        if(toggle){
            byte[][][] s = this.client.getTileSettings();
            int l = (s[1][x][y] & 2) != 0 ? 1 : this.client.getPlane();
            int[][] h = this.client.getTileHeights()[l];
            g.setColor(c);
            this.line(g, x, y, x + 1, y, h);
            this.line(g, x, y, x, y + 1, h);
            this.line(g, x, y + 1, x + 1, y + 1, h);
            this.line(g, x + 1, y, x + 1, y + 1, h);
            int x1 = x + 1;
            int y1 = y + 1;
            Point p0 = Perspective.localToCanvas(this.client, x << 7, y << 7, h[x][y]);
            Point p1 = Perspective.localToCanvas(this.client, x1 << 7, y << 7, h[x1][y]);
            Point p3 = Perspective.localToCanvas(this.client, x << 7, y1 << 7, h[x][y1]);
            Point p2 = Perspective.localToCanvas(this.client, x1 << 7, y1 << 7, h[x1][y1]);
            if (p0 != null && p1 != null && p3 != null && p2 != null) {
                int[] xPoints = new int[]{p0.getX(), p1.getX(), p2.getX(), p3.getX()};
                int[] yPoints = new int[]{p0.getY(), p1.getY(), p2.getY(), p3.getY()};
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 50));
                g.fillPolygon(xPoints, yPoints, 4);
            }

            if (textLine1 != null) {
                LocalPoint lp = new LocalPoint(x * 128 + 64, y * 128 + 64);
                Point p = Perspective.getCanvasTextLocation(this.client, (Graphics2D)g, lp, textLine1, 0);
                if (p != null) {
                    g.setColor(Color.BLACK);
                    g.drawString(textLine1, p.getX() + 1, p.getY() + 1);
                    g.setColor(Color.WHITE);
                    g.drawString(textLine1, p.getX(), p.getY());
                }
            }
        }
    }

    private void line(Graphics g, int x0, int y0, int x1, int y1, int[][] h) {
        Point p0 = Perspective.localToCanvas(this.client, x0 << 7, y0 << 7, h[x0][y0]);
        Point p1 = Perspective.localToCanvas(this.client, x1 << 7, y1 << 7, h[x1][y1]);
        if (p0 != null && p1 != null) {
            g.drawLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
        }

    }

    protected void toggle()
    {
        toggle = !toggle;
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon, String textLine1, LocalPoint lp1) {
        if(toggle){
            if (polygon != null) {
                graphics.setColor(color);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(polygon);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                graphics.fill(polygon);
            }

            if (textLine1 != null) {
                //LocalPoint lp = new LocalPoint(lp1.getX() * 128 + 64, lp1.getY() * 128 + 64);
                Point p = Perspective.getCanvasTextLocation(this.client, (Graphics2D)graphics, lp1, textLine1, 0);
                if (p != null) {
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(textLine1, p.getX() + 1, p.getY() + 1);
                    graphics.setColor(color);
                    graphics.drawString(textLine1, p.getX(), p.getY());
                }
            }
        }


    }

    //Draw game objects
    private void renderTileObject(Graphics2D graphics, TileObject tileObject, Player player, Color color) {
        if(toggle){
            if (tileObject != null && player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) <= 2400) {
                graphics.setStroke(new BasicStroke(1.0F));
                String label = "";
                if(config.debugGameObjects()){
                    label = ""+ tileObject.getId();
                }

                //OverlayUtil.renderTileOverlay(graphics, tileObject, label, color);
                //Polygon poly = tileObject.getCanvasTilePoly();
                Polygon poly;
                LocalPoint lp = new LocalPoint(tileObject.getX(), tileObject.getY());
                if (aoeSize == 0){
                    poly = tileObject.getCanvasTilePoly();
                }else{
                    poly = Perspective.getCanvasTileAreaPoly(this.client, lp, aoeSize);
                }

                if (poly != null)
                {
                    graphics.setColor(color);
                    graphics.setStroke(new BasicStroke(1.0F));
                    graphics.draw(poly);
                    graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                    graphics.fill(poly);
                }
                Point textLocation = tileObject.getCanvasTextLocation(graphics, label, 0);
                if (textLocation != null)
                {
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(label, textLocation.getX() + 1, textLocation.getY() + 1);
                    graphics.setColor(Color.WHITE);
                    graphics.drawString(label, textLocation.getX(), textLocation.getY());
                }
            }
        }

    }

    private boolean parse_string(Set<String> str, int hlID){
        String tID = "";
        String junk = "";

        for(String strMain : str){
            String[] sID = strMain.split(",");
            tID = sID[0].trim();
            if(tID.equals("")){
                break;
            }
            if(tID.contains("/")){
                sID = tID.split("/");
                tID = sID[0].trim();
            }
            if(tID.contains("-")){
                sID = strMain.split("-");
                tID = sID[0].trim();
            }

            try {
                if(Integer.parseInt(tID) == hlID){
                    set_vars(strMain);
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }

        }
        return false;
    }

    private void set_vars(String src) {
        String[] split = src.split("/");

        aoeSize = 0;
        aColor = config.highlightColor();

        //color
        for(int i = 1; i < split.length; ++i) {
            String s = split[i].trim();
            int c = Integer.parseInt(s);

            if(c == 1){
                aColor = config.highlightColor();
            }else if(c == 2){
                aColor = config.highlightColor2();
            }else if(c == 3){
                aColor = config.highlightColor3();
            }else if(c == 4){
                aColor = config.highlightColor4();
            }else if(c == 5){
                aColor = config.highlightColor5();
            }else if(c == 6){
                aColor = config.highlightColor6();
            }else if(c == 7){
                aColor = config.highlightColor7();
            }

        }

        //id and size
        String[] split2 = src.split("-");
        for(int i = 0; i < split2.length; ++i) {
            if(i == 0){
                String s = split2[i].trim();

                try {
                    hID = Integer.parseInt(s);
                } catch (NumberFormatException ignored) {
                }
            }
            if(i == 1){
                if(split2[i].trim().contains("/")){
                    String[] split3 = split2[i].split("/");
                    split2[i] = split3[0];
                }

                String s = split2[i].trim();

                try {
                    aoeSize = Integer.parseInt(s);
                } catch (NumberFormatException ignored) {
                }
            }

        }

    }

    //PRAYER OVERLAY
    private void renderDescendingBoxes(Graphics2D graphics, Prayer prayer, int tick) {
        Color color = tick == 1 ? Color.RED : Color.ORANGE;
        Widget prayerWidget = getWidget(prayer);
        int baseX = (int)prayerWidget.getBounds().getX();
        baseX = (int)((double)baseX + prayerWidget.getBounds().getWidth() / 2.0D);
        baseX -= 5;
        int baseY = (int)prayerWidget.getBounds().getY() - tick * 60 - 5;
        baseY = (int)((double)baseY + (60.0D - (double)(this.plugin.getLastTick() + 600L - System.currentTimeMillis()) / 600.0D * 60.0D));
        Rectangle boxRectangle = new Rectangle(10, 5);
        boxRectangle.translate(baseX, baseY);
        renderFilledPolygon(graphics, boxRectangle, color);
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

    private void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows) {
        if(!toggle){
            return;
        }
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

    public static void renderFilledPolygon(Graphics2D graphics, Shape poly, Color color)
    {
        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(poly);
        graphics.fill(poly);
        graphics.setStroke(originalStroke);
    }

}
