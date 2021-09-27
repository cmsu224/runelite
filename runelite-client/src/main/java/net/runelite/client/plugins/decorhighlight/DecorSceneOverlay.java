//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.decorhighlight;

import java.awt.*;
import java.util.Iterator;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class DecorSceneOverlay extends Overlay {
    private Client client;
    private DecorHighlightPlugin plugin;
    private DecorHighlightConfig config;
    private Boolean toggle = true;

    @Inject
    public DecorSceneOverlay(Client client, DecorHighlightPlugin plugin, DecorHighlightConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    public Dimension render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(Color.RED);
        Iterator var3 = this.client.getGraphicsObjects().iterator();
        Player player = this.client.getLocalPlayer();
        //Iterator var2 = client.getNpcs().iterator();

        //NPC npc = null;
        //NPCComposition npcComposition;
        //int size;

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
                                    if (config.debugDecor() || this.plugin.groundDecorWhitelist.contains(decor.getId())) {
                                        String str = null;
                                        if (config.debugDecor()) {
                                            str = Integer.toString(decor.getId());
                                        }

                                        LocalPoint lp = decor.getLocalLocation();
                                        int _x = (lp.getX() - 64) / 128;
                                        int _y = (lp.getY() - 64) / 128;
                                        this.tile(g, _x, _y, this.config.highlightColor(), str);
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
                                                if (config.debugGameObjects() || this.plugin.gameObjectsWhitelist.contains(gameObject.getId())) {
                                                    //OverlayUtil.renderTileOverlay(g, gameObject, "ID: " + gameObject.getId(), Color.GREEN);

                                                    String str = null;
                                                    if (config.debugGameObjects()) {
                                                        str = Integer.toString(gameObject.getId());
                                                    }
                                                    LocalPoint lp = gameObject.getLocalLocation();
                                                    int _x = (lp.getX() - 64) / 128;
                                                    int _y = (lp.getY() - 64) / 128;
                                                    this.tile(g, _x, _y, this.config.highlightColor(), str);
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
            } while(!debug && !this.plugin.graphicsObjectWhitelist.contains(go.getId()));

            String str = null;
            if (config.debugGraphicObjects()) {
                str = Integer.toString(go.getId());
            }

            LocalPoint lp = go.getLocation();
            int x = (lp.getX() - 64) / 128;
            int y = (lp.getY() - 64) / 128;
            this.tile(g, x, y, this.config.highlightColor(), str);

            /*
            //npcs
            while(var2.hasNext()) {
                npc = (NPC)var2.next();

                if(config.debugNPCs()){
                    npcComposition = npc.getTransformedComposition();
                    if (npcComposition != null) {
                        size = npcComposition.getSize();

                        lp = LocalPoint.fromWorld(client, npc.getWorldLocation());
                        if (lp != null) {
                            lp = new LocalPoint(lp.getX() + size * 128 / 2 - 64, lp.getY() + size * 128 / 2 - 64);

                            str = null;
                            if (config.debugNPCs()) {
                                str = Integer.toString(npc.getId());
                            }
                            tile(g, lp.getX(), lp.getY(), config.highlightColor(), str);
                        }
                    }
                }
            }
             */
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
}
