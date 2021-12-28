//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.Scene;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.kit.KitType;
import net.runelite.client.plugins.aztobextra.config.FontMets;
import net.runelite.client.plugins.aztobextra.config.MaidenRedWCType;
import net.runelite.client.plugins.aztobextra.config.P1PillarTicks;
import net.runelite.client.plugins.aztobextra.config.PillarHide;
import net.runelite.client.plugins.aztobextra.config.SoteProjectileType;
import net.runelite.client.plugins.aztobextra.config.VerzYellowsMode;
import net.runelite.client.plugins.aztobextra.AzEasyTobOverlay;
import net.runelite.client.plugins.aztobextra.meta.MatomenosDetails;
import net.runelite.client.plugins.aztobextra.meta.stomp.BloatSafespot;
import net.runelite.client.plugins.aztobextra.meta.stomp.SSLine;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

public class AzEasyTobOverlay extends Overlay {
    private final Client client;
    private final AzEasyTobConfig config;
    private final AzEasyTobPlugin plugin;
    private static final OverlayLayer LAYER;
    private boolean sentHintArrow = false;

    @Inject
    AzEasyTobOverlay(Client client, AzEasyTobConfig config, AzEasyTobPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public static Color percentageToColor(int healthRatio, int healthScale) {
        float percentage = (float)healthRatio / (float)healthScale * 100.0F;
        percentage = Math.max(Math.min(100.0F, percentage), 0.0F);
        if (percentage >= 66.0F) {
            return Color.GREEN;
        } else {
            return percentage >= 33.0F ? Color.ORANGE : Color.red;
        }
    }

    public boolean removePill() {
        if (!this.plugin.isHiddenPills() && this.plugin.getVerzNPC() != null) {
            boolean foundPills = false;
            Scene scene = this.client.getScene();
            Tile[][][] tiles = scene.getTiles();
            int z = this.client.getPlane();

            for(int x = 0; x < 104; ++x) {
                for(int y = 0; y < 104; ++y) {
                    Tile tile = tiles[z][x][y];
                    if (tile != null) {
                        Player player = this.client.getLocalPlayer();
                        if (player != null) {
                            GameObject[] gameObjects = tile.getGameObjects();
                            if (gameObjects != null) {
                                GameObject[] var10 = gameObjects;
                                int var11 = gameObjects.length;

                                for(int var12 = 0; var12 < var11; ++var12) {
                                    GameObject gameObject = var10[var12];
                                    if (gameObject != null && this.plugin.getVerzNPC() != null && 32687 == gameObject.getId()) {
                                        foundPills = true;
                                        if (this.config.hidePillar() == PillarHide.All_Pillars || this.config.hidePillar() == PillarHide.Back_Pillars && gameObject.getWorldLocation().getRegionY() < 21) {
                                            this.client.getScene().removeGameObject(gameObject);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (foundPills) {
                this.plugin.setHiddenPills(true);
            }

            return false;
        } else {
            return true;
        }
    }

    public Dimension render(Graphics2D graphics) {
        boolean hintIcon = false;
        this.removePill();
        boolean ancients;
        if (this.plugin.rewardChest != null && this.config.chestRew()) {
            int region = WorldPoint.fromLocalInstance(this.client, ((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getLocalLocation()).getRegionID();
            ancients = true;
            if (region != 14386 && region != 14642) {
                ancients = false;
            }

            if (ancients && this.client.getObjectDefinition(this.plugin.rewardChest.getId()).getImpostor().getId() != 41435) {
                ancients = false;
            }

            if (ancients) {
                Shape chestOutline = this.plugin.rewardChest.getConvexHull();
                if (this.config.chestRew()) {
                    this.renderPoly(graphics, Color.YELLOW, chestOutline);
                    this.client.setHintArrow(this.plugin.rewardChest.getWorldLocation());
                    this.sentHintArrow = true;
                    hintIcon = true;
                }
            }
        }

        if (!hintIcon && this.sentHintArrow) {
            this.client.clearHintArrow();
            this.sentHintArrow = false;
        }

        int[] p1tick = new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        if (this.config.p1ticks() != P1PillarTicks.Off && this.plugin.partySize <= 2 && this.plugin.getVerzNPC() != null && !this.plugin.getVerzNPC().isDead() && this.plugin.verzikTicksUntilAttack != -1) {
            int npcId = this.plugin.getVerzNPC().getId();
            if (npcId == 8370 || npcId == 10831 || npcId == 10848) {
                int rx = this.client.getLocalPlayer().getWorldLocation().getRegionX();
                int disp = p1tick[this.plugin.verzikTicksUntilAttack - 1];
                if (disp != 0) {
                    WorldPoint p1flinch = WorldPoint.fromRegion(this.client.getLocalPlayer().getWorldLocation().getRegionID(), rx >= 16 ? 22 : 10, 21, 0);
                    if (p1flinch != null) {
                        LocalPoint lp = LocalPoint.fromWorld(this.client, p1flinch);
                        if (lp != null) {
                            Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, "#", 0);
                            if (this.config.p1ticks() == P1PillarTicks.Both || this.config.p1ticks() == P1PillarTicks.Ticks) {
                                this.renderTextLocation(graphics, point, disp + "", Color.WHITE);
                            }

                            if ((this.config.p1ticks() == P1PillarTicks.Both || this.config.p1ticks() == P1PillarTicks.Tiles) && (disp == 4 || disp == 3 || disp == 2)) {
                                Color c = disp == 2 ? Color.GREEN : Color.WHITE;
                                Polygon poly = getCanvasTileAreaPoly(this.client, lp, 1);
                                if (poly != null) {
                                    graphics.setColor(c);
                                    graphics.setStroke(new BasicStroke(1.0F));
                                    graphics.draw(poly);
                                }
                            }
                        }
                    }
                }
            }
        }

        Player p2;
        if (this.plugin.isInTheatre() && this.config.leechWarn()) {
            Player p = this.client.getLocalPlayer();
            if (p != null) {
                Iterator var28 = this.client.getPlayers().iterator();

                while(var28.hasNext()) {
                    p2 = (Player)var28.next();
                    int amy;
                    if (isInBloatRegion(this.client)) {
                        amy = p2.getPlayerComposition().getEquipmentId(KitType.AMULET);
                        if (amy != 10588 && amy != 12018 && amy != 11090) {
                            renderPinkBox(p2, this.client, graphics, this.plugin);
                        }
                    }

                    amy = p2.getAnimation();
                    int wep = p2.getPlayerComposition().getEquipmentId(KitType.WEAPON);
                    if (wep == 22486) {
                        renderOrangeBox(p2, this.client, graphics, this.plugin);
                    }
                }

                if (this.plugin.getFlagUnpot() > System.currentTimeMillis()) {
                    renderRedBox(p, this.client, graphics, this.plugin);
                }
            }
        }

        ancients = this.client.getVarbitValue(4070) == 1 || this.config.debug();
        boolean maidenRoom = WorldPoint.fromLocalInstance(this.client, this.client.getLocalPlayer().getLocalLocation()).getRegionID() == 12613;
        p2 = null;
        if (maidenRoom && this.plugin.getMaiden() != null) {
            this.displayMatomenosOverlays(graphics);
            this.maidenWheelChairOverlay(graphics);
        }

        if (!ancients) {
            p2 = null;
        }

        NPC sote = this.plugin.getSoteNPC();
        Color bombCol = this.config.getBombCol();
        String tickdownS;
        String bombCDText;
        Point canvasPoint;
        int instanceT;
        if (sote != null) {
            bombCDText = "";
            if (this.config.soteBombCountdown()) {
                bombCDText = this.plugin.getBombAttacksRemaining() <= 0 ? "NOW!" : this.plugin.getBombAttacksRemaining() + "";
            }

            if (bombCDText.length() >= 1) {
                canvasPoint = sote.getCanvasTextLocation(graphics, bombCDText, 270);
                if (canvasPoint != null && !sote.isDead()) {
                    this.renderTextLocation(graphics, canvasPoint, bombCDText, bombCol);
                }
            }

            if (sote.getId() == 8387 && this.config.soteStepCountdown()) {
                canvasPoint = sote.getCanvasTextLocation(graphics, bombCDText, 150);
                instanceT = this.plugin.getInstanceTimer();
                tickdownS = (new String[]{"0", "1", "2", "NOW!"})[instanceT];
                this.renderTextLocation(graphics, canvasPoint, tickdownS, Color.GREEN);
            }
        }

        sote = this.plugin.getXarpNPC();

        Iterator var3;
        int xarpusid;
        if (sote != null && this.config.cleanXarpExhumes()) {
            xarpusid = sote.getId();
            if (xarpusid == 8339 || xarpusid == 10767 || xarpusid == 10771) {
                bombCDText = this.plugin.getExhumesLeft() <= 0 ? "NOW!" : this.plugin.getExhumesLeft() + "";
                instanceT = this.plugin.getXarpInitiateTickdown();
                tickdownS = instanceT == 0 ? "NOW!" : instanceT + "";
                canvasPoint = sote.getCanvasTextLocation(graphics, tickdownS, 370);
                if (canvasPoint != null && instanceT >= 0 && instanceT <= 9) {
                    this.renderTextLocation(graphics, canvasPoint, tickdownS, bombCol);
                }

                canvasPoint = this.client.getLocalPlayer().getCanvasTextLocation(graphics, bombCDText, 240);
                if (canvasPoint != null) {
                    this.renderTextLocation(graphics, canvasPoint, bombCDText, bombCol);
                }

                var3 = this.plugin.getXarpusExhumeds().keySet().iterator();

                while(var3.hasNext()) {
                    GroundObject g = (GroundObject)var3.next();
                    Integer val = (Integer)this.plugin.getXarpusExhumeds().get(g);
                    canvasPoint = g.getCanvasLocation();
                    if (canvasPoint != null && val > 0) {
                        this.renderTextLocation(graphics, canvasPoint, val + "", Color.white);
                        this.drawTile(graphics, g.getWorldLocation(), new Color(255, 50, 25), 2, 220, 0);
                    }
                }
            }
        }

        sote = this.plugin.getNyloBossNPC();
        if (sote != null && this.config.easyNylo()) {
            bombCDText = this.plugin.getNyloBossChangeTick() <= 0 ? "0" : this.plugin.getNyloBossChangeTick() + "";
            canvasPoint = sote.getCanvasTextLocation(graphics, bombCDText, 100);
            if (canvasPoint != null && !sote.isDead()) {
                this.renderTextLocation(graphics, canvasPoint, bombCDText, bombCol);
            }
        }

        sote = this.plugin.getVerzNPC();
        boolean drawingPoly;

        if (sote != null) {
            if (this.config.verzBombCountdown() && (sote.getId() == 8372 || sote.getId() == 10833 || sote.getId() == 10850)) {
                bombCDText = this.plugin.getLightningAttacksRemaining() <= 0 ? "NOW!" : this.plugin.getLightningAttacksRemaining() + "";
                canvasPoint = sote.getCanvasTextLocation(graphics, bombCDText, 270);
                if (canvasPoint != null && !sote.isDead()) {
                    this.renderTextLocation(graphics, canvasPoint, bombCDText, bombCol);
                }

                if (!this.plugin.getVerzMatos().isEmpty() && this.plugin.getVerzNPC() != null) {
                    this.plugin.getVerzMatos().values().forEach((detailsx) -> {
                        NPC matomenos = detailsx.getMatomenosNpc();
                        if (!matomenos.isDead()) {
                            detailsx.updateHitpoints1();
                            detailsx.updateHitpoints2();
                            boolean DYNAMIC = true;
                            float hitpoints = detailsx.calculateHitpoints();
                            String text = detailsx.formatHitpoints(hitpoints, this.config.crab1deci());
                            Point textLocation = matomenos.getCanvasTextLocation(graphics, text, 0);
                            if (!matomenos.isDead() && textLocation != null) {
                                Color color = DYNAMIC ? detailsx.calculateHitpointsColor(hitpoints) : Color.WHITE;
                                this.renderTextLocation(graphics, new Point(textLocation.getX(), textLocation.getY() - 0), text, color);
                            }
                        }

                    });
                }
            }

            boolean size = true;
            NPCComposition composition = sote.getTransformedComposition();
            if (composition != null) {
                xarpusid = composition.getSize();
            }

            Color nadoCol = new Color(0, 200, 255);
            if (this.config.smartNado()) {
                Iterator var49 = this.plugin.getVerzikTornadoLocations().entrySet().iterator();

                Entry entry;
                NPC npc;
                WorldPoint point;
                while(var49.hasNext()) {
                    entry = (Entry)var49.next();
                    npc = (NPC)entry.getKey();
                    point = (WorldPoint)entry.getValue();
                    if (this.plugin.getMemorizedTornados().get(npc.getIndex()) != null) {
                        this.drawTile(graphics, point, nadoCol, 1, 120, 10);
                    }
                }

                var49 = this.plugin.getVerzikTornadoTrailingLocations().entrySet().iterator();

                while(var49.hasNext()) {
                    entry = (Entry)var49.next();
                    npc = (NPC)entry.getKey();
                    point = (WorldPoint)entry.getValue();
                    if (this.plugin.getMemorizedTornados().get(npc.getIndex()) != null) {
                        this.drawTile(graphics, point, nadoCol, 2, 180, 20);
                    }
                }
            }

            if (this.plugin.yellowsOut && (sote.getId() == 8374 || sote.getId() == 10835 || sote.getId() == 10852)) {
                if (this.plugin.yellowGroups.size() > 0 && this.config.yellowticks() == VerzYellowsMode.on_with_groups && sote.getId() == 10852) {
                    drawingPoly = false;
                    int group = 0;

                    for(var3 = this.plugin.yellowGroups.iterator(); var3.hasNext(); ++group) {
                        ArrayList<WorldPoint> list = (ArrayList)var3.next();
                        Iterator var5 = list.iterator();

                        while(var5.hasNext()) {
                            WorldPoint next = (WorldPoint)var5.next();
                            LocalPoint localPoint = LocalPoint.fromWorld(this.client, next);
                            if (localPoint != null) {
                                Polygon poly = Perspective.getCanvasTilePoly(this.client, localPoint);
                                if (poly != null) {
                                    Stroke originalStroke = graphics.getStroke();
                                    Color fill;
                                    if (drawingPoly) {
                                        fill = Color.BLACK;
                                        graphics.setColor(fill);
                                        graphics.setStroke(new BasicStroke(2.0F));
                                        graphics.draw(poly);
                                    }

                                    switch(group) {
                                        case 0:
                                            fill = Color.RED;
                                            break;
                                        case 1:
                                            fill = Color.BLUE;
                                            break;
                                        case 2:
                                            fill = Color.GREEN;
                                            break;
                                        case 3:
                                            fill = Color.MAGENTA;
                                            break;
                                        case 4:
                                            fill = Color.ORANGE;
                                            break;
                                        default:
                                            fill = new Color(250, 50, 100);
                                    }

                                    if (drawingPoly) {
                                        Color realFill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 130);
                                        graphics.setColor(realFill);
                                        graphics.fill(poly);
                                        graphics.setStroke(originalStroke);
                                    }

                                    int i = this.plugin.yellowTimer - 1;
                                    String text = String.valueOf(i == 0 ? "NOW!" : i);
                                    Point point = Perspective.getCanvasTextLocation(this.client, graphics, localPoint, text, 0);
                                    this.renderTextLocation(graphics, point, text, fill);
                                }
                            }
                        }
                    }
                } else if (this.config.yellowticks() == VerzYellowsMode.on) {
                    int i = this.plugin.yellowTimer - 1;
                    Iterator var54 = this.plugin.yellowsList.iterator();

                    while(var54.hasNext()) {
                        WorldPoint p = (WorldPoint)var54.next();
                        bombCDText = i == 0 ? "NOW!" : i + "";
                        Point textPoint = Perspective.getCanvasTextLocation(this.client, graphics, LocalPoint.fromWorld(this.client, p), String.valueOf(bombCDText), 0);
                        if (textPoint != null) {
                            this.renderTextLocation(graphics, textPoint, String.valueOf(bombCDText), bombCol);
                        }
                    }
                }
            }
        }

        Map<Projectile, String> projectileMap = new HashMap();
        String countdownStr;
        if (this.config.soteProjectiles() != SoteProjectileType.OFF || this.config.gbTicker()) {
            Iterator var45 = this.plugin.getSoteyProjectiles().keySet().iterator();

            label401:
            while(true) {
                Projectile proj;
                Actor interacting;
                Point point;
                BufferedImage icon;
                do {
                    int x;
                    int y;
                    int z;
                    do {
                        Integer t;
                        do {
                            do {
                                do {
                                    if (!var45.hasNext()) {
                                        this.renderProjectiles(graphics, projectileMap);
                                        break label401;
                                    }

                                    proj = (Projectile)var45.next();
                                } while(!this.config.gbTicker() && proj.getId() == 1598);

                                drawingPoly = proj.getId() >= 1606 && proj.getId() <= 1607;
                            } while(this.config.soteProjectiles() == SoteProjectileType.OFF && (drawingPoly || proj.getId() == 1604));

                            t = (Integer)this.plugin.getSoteyProjectileFilter().get(proj);
                        } while(t != null && t > this.client.getTickCount() - 1);

                        int ticksRemaining = proj.getRemainingCycles() / 30;
                        countdownStr = String.valueOf(ticksRemaining);
                        projectileMap.put(proj, countdownStr);
                        x = (int)proj.getX();
                        y = (int)proj.getY();
                        z = (int)proj.getZ();
                        interacting = this.plugin.findActor(ClientInterface.getInteractingIndex(proj));
                    } while(drawingPoly && !this.config.soteProjectiles().name().contains("HAT"));

                    point = Perspective.localToCanvas(this.client, new LocalPoint(x, y), 0, Perspective.getTileHeight(this.client, new LocalPoint(x, y), proj.getFloor()) - z);
                    icon = proj.getId() == 1606 ? AzEasyTobPlugin.MAGE_HAT : (proj.getId() == 1607 ? AzEasyTobPlugin.RANGE_HAT : (proj.getId() == 1598 ? AzEasyTobPlugin.GNUKE : (proj.getId() == 1604 ? AzEasyTobPlugin.NUKE : this.plugin.getIconManager().getSkillImage(Skill.MAGIC))));
                } while(point == null);

                if (icon != AzEasyTobPlugin.NUKE && icon != AzEasyTobPlugin.GNUKE) {
                    point = new Point(point.getX() - icon.getWidth() / 2, point.getY() - 10);
                } else {
                    point = new Point(point.getX() - icon.getWidth() / 2, point.getY() - 60);
                }

                if (proj.getId() == 1598 && interacting instanceof Player) {
                    renderGreenBox((Player)interacting, this.client, graphics, this.plugin);
                }

                OverlayUtil.renderImageLocation(graphics, point, icon);
            }
        }

        if (this.plugin.getBloatNPC() != null) {
            boolean rlFont = false;
            boolean RLPLUSconfig = this.config.rLPLUSBloat();
            drawingPoly = this.config.stompLine();
            if (RLPLUSconfig) {
                this.renderNpcTLOverlay(graphics, this.plugin.getBloatNPC(), this.plugin.getBloatStateColor(), 3, 200, 0);
                canvasPoint = this.plugin.getBloatNPC().getCanvasTextLocation(graphics, String.valueOf(this.plugin.getBloatUpTimer()), 60);
                countdownStr = "( " + (this.plugin.getBloatSecondsTimer() == 0L ? 0L : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.plugin.getBloatSecondsTimer())) + " )";
                if (this.plugin.getBloatState() != 1) {
                    bombCDText = (33 - this.plugin.getBloatDownCount() == -1 ? 0 : 33 - this.plugin.getBloatDownCount()) + countdownStr;
                    if (this.plugin.getBloatDownCount() >= 26) {
                        if (!rlFont) {
                            this.renderSteroidsTextLocation(graphics, bombCDText, 15, 1, Color.WHITE, canvasPoint);
                        }
                    } else if (!rlFont) {
                        this.renderSteroidsTextLocation(graphics, bombCDText, 15, 1, Color.WHITE, canvasPoint);
                    }
                } else {
                    Color col = this.plugin.getBloatUpTimer() > 37 ? Color.RED : Color.WHITE;
                    if (!rlFont) {
                        this.renderSteroidsTextLocation(graphics, this.plugin.getBloatUpTimer() + countdownStr, 15, 1, col, canvasPoint);
                    }
                }
            }

            if (drawingPoly && (this.plugin.getBloatState() == 2 || this.plugin.getBloatState() == 3)) {
                this.renderStompSafespots(graphics);
            }
        }

        return null;
    }

    protected void renderSteroidsTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint) {
        graphics.setFont(new Font("Arial", fontStyle, fontSize));
        if (canvasPoint != null) {
            Point canvasCenterPoint = new Point(canvasPoint.getX(), canvasPoint.getY());
            Point canvasCenterPointShadow = new Point(canvasPoint.getX() + 1, canvasPoint.getY() + 1);
            OverlayUtil.renderTextLocation(graphics, canvasCenterPointShadow, txtString, Color.BLACK);
            OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }

    }

    private List<NPC> maidenWheelChairOverlay(Graphics2D graphics) {
        int currentMaidenStage = Integer.valueOf(this.plugin.getMaidenPhase().getPhaseKey());
        if (this.config.getType() != MaidenRedWCType.OFF && !this.plugin.getMaidenMatos().isEmpty() && this.plugin.getPartySize() >= 4) {
            int n1TickLoss = -1;
            int n2TickLoss = -1;
            boolean state = true;
            NPC n1n = null;
            NPC n2n = null;
            MatomenosDetails n1d = null;
            MatomenosDetails n2d = null;
            List<NPC> n3s = new ArrayList();
            Iterator var11 = this.plugin.getMaidenMatos().keySet().iterator();

            while(true) {
                MatomenosDetails dtx;
                String spotKey;
                do {
                    while(true) {
                        do {
                            do {
                                if (!var11.hasNext()) {
                                    boolean n1Unfrozen = n1d != null && n1d.isHasBeenFrozen() && n1d.getFrozenTicks() == -1;
                                    boolean n2Unfrozen = n2d != null && n2d.isHasBeenFrozen() && n2d.getFrozenTicks() == -1;
                                    if (!n1Unfrozen && n1TickLoss <= 19 && n1TickLoss != -1) {
                                        if (!n2Unfrozen && n2TickLoss != -1) {
                                            state = false;
                                        }
                                    } else if (!n2Unfrozen && n2TickLoss <= 18 && n2TickLoss != -1) {
                                        state = false;
                                    }

                                    this.plugin.setN1Orn2Alive(n2n != null && !n2n.isDead() || n1n != null && !n1n.isDead());
                                    this.plugin.setMaidenNyloWheelchairState(state);
                                    if (state || this.config.getType() == MaidenRedWCType.INFOBOX) {
                                        n3s.clear();
                                    }

                                    return n3s;
                                }

                                Integer idx = (Integer)var11.next();
                                dtx = (MatomenosDetails)this.plugin.getMaidenMatos().get(idx);
                            } while(Integer.valueOf(dtx.getPhaseKey()) > currentMaidenStage);
                        } while(dtx.getIdentifier() == null);

                        spotKey = (String)dtx.getIdentifier().getKey();
                        if (!spotKey.equalsIgnoreCase("n3") && !spotKey.equalsIgnoreCase("s3")) {
                            break;
                        }

                        if (!dtx.isHasBeenFrozen()) {
                            n3s.add(dtx.getMatomenosNpc());
                        }
                    }
                } while(!spotKey.equalsIgnoreCase("n1") && !spotKey.equalsIgnoreCase("n2"));

                int rx = dtx.getMatomenosNpc().getWorldLocation().getRegionX();
                int ry = dtx.getMatomenosNpc().getWorldLocation().getRegionY();
                if (spotKey.equalsIgnoreCase("n1")) {
                    n1d = dtx;
                    n1n = dtx.getMatomenosNpc();
                    n1TickLoss = rx;
                } else {
                    n2d = dtx;
                    n2n = dtx.getMatomenosNpc();
                    n2TickLoss = rx;
                }
            }
        } else {
            return null;
        }
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        }

    }

    protected boolean isInSotetsegRegion() {
        return this.client.getMapRegions() != null && this.client.getMapRegions().length > 0 && Arrays.stream(this.client.getMapRegions()).anyMatch((s) -> {
            return s == 13123 || s == 13379;
        });
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

    protected void drawTile(Graphics2D graphics, WorldPoint point, Color color, int strokeWidth, int outlineAlpha, int fillAlpha) {
        WorldPoint playerLocation = this.client.getLocalPlayer().getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld(this.client, point);
        if (lp != null) {
            Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
            if (poly != null) {
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
                graphics.setStroke(new BasicStroke((float)strokeWidth));
                graphics.draw(poly);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
                graphics.fill(poly);
            }
        }

    }

    protected void renderPoly(Graphics2D graphics, Color color, Polygon polygon) {
        this.renderPoly(graphics, color, polygon, 2);
    }

    protected void renderPoly(Graphics2D graphics, Color color, Polygon polygon, int width) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke((float)width));
            graphics.draw(polygon);
        }

    }

    public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int size) {
        return getCanvasTileAreaPoly(client, localLocation, size, 0, true);
    }

    public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int size, int borderOffset) {
        return getCanvasTileAreaPoly(client, localLocation, size, borderOffset, true);
    }

    public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int size, boolean centered) {
        return getCanvasTileAreaPoly(client, localLocation, size, 0, centered);
    }

    public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int size, int borderOffset, boolean centered) {
        int plane = client.getPlane();
        int swX;
        int swY;
        int neX;
        int neY;
        if (centered) {
            swX = localLocation.getX() - size * (128 + borderOffset) / 2;
            swY = localLocation.getY() - size * (128 + borderOffset) / 2;
            neX = localLocation.getX() + size * (128 + borderOffset) / 2;
            neY = localLocation.getY() + size * (128 + borderOffset) / 2;
        } else {
            swX = localLocation.getX() - (128 + borderOffset) / 2;
            swY = localLocation.getY() - (128 + borderOffset) / 2;
            neX = localLocation.getX() - (128 + borderOffset) / 2 + size * (128 + borderOffset);
            neY = localLocation.getY() - (128 + borderOffset) / 2 + size * (128 + borderOffset);
        }

        byte[][][] tileSettings = client.getTileSettings();
        int sceneX = localLocation.getSceneX();
        int sceneY = localLocation.getSceneY();
        if (sceneX >= 0 && sceneY >= 0 && sceneX < 104 && sceneY < 104) {
            int tilePlane = plane;
            if (plane < 3 && (tileSettings[1][sceneX][sceneY] & 2) == 2) {
                tilePlane = plane + 1;
            }

            int swHeight = getHeight(client, swX, swY, tilePlane);
            int nwHeight = getHeight(client, neX, swY, tilePlane);
            int neHeight = getHeight(client, neX, neY, tilePlane);
            int seHeight = getHeight(client, swX, neY, tilePlane);
            Point p1 = Perspective.localToCanvas(client, swX, swY, swHeight);
            Point p2 = Perspective.localToCanvas(client, neX, swY, nwHeight);
            Point p3 = Perspective.localToCanvas(client, neX, neY, neHeight);
            Point p4 = Perspective.localToCanvas(client, swX, neY, seHeight);
            if (p1 != null && p2 != null && p3 != null && p4 != null) {
                Polygon poly = new Polygon();
                poly.addPoint(p1.getX(), p1.getY());
                poly.addPoint(p2.getX(), p2.getY());
                poly.addPoint(p3.getX(), p3.getY());
                poly.addPoint(p4.getX(), p4.getY());
                return poly;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static int getHeight(@Nonnull Client client, int localX, int localY, int plane) {
        int sceneX = localX >> 7;
        int sceneY = localY >> 7;
        if (sceneX >= 0 && sceneY >= 0 && sceneX < 104 && sceneY < 104) {
            int[][][] tileHeights = client.getTileHeights();
            int x = localX & 127;
            int y = localY & 127;
            int var8 = x * tileHeights[plane][sceneX + 1][sceneY] + (128 - x) * tileHeights[plane][sceneX][sceneY] >> 7;
            int var9 = tileHeights[plane][sceneX][sceneY + 1] * (128 - x) + x * tileHeights[plane][sceneX + 1][sceneY + 1] >> 7;
            return (128 - y) * var8 + y * var9 >> 7;
        } else {
            return 0;
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

    private static void renderRedBox(Player player, Client client, Graphics2D graphics, AzEasyTobPlugin pl) {
        if (pl.getConfig().leechWarn()) {
            Polygon poly = getCanvasTileAreaPoly(client, player.getLocalLocation(), 1, -15);
            if (poly != null) {
                graphics.setColor(Color.RED);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(poly);
            }

        }
    }

    private static void renderGreenBox(Player player, Client client, Graphics2D graphics, AzEasyTobPlugin pl) {
        if (pl.getConfig().leechWarn()) {
            Polygon poly = getCanvasTileAreaPoly(client, player.getLocalLocation(), 1, -30);
            if (poly != null) {
                graphics.setColor(Color.GREEN);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(poly);
            }

        }
    }

    private static void renderOrangeBox(Player player, Client client, Graphics2D graphics, AzEasyTobPlugin pl) {
        if (pl.getConfig().leechWarn()) {
            Polygon poly = getCanvasTileAreaPoly(client, player.getLocalLocation(), 1, -25);
            if (poly != null) {
                graphics.setColor(Color.ORANGE);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(poly);
            }

        }
    }

    private static void renderPinkBox(Player player, Client client, Graphics2D graphics, AzEasyTobPlugin pl) {
        if (pl.getConfig().leechWarn()) {
            Polygon poly = getCanvasTileAreaPoly(client, player.getLocalLocation(), 1, -25);
            if (poly != null) {
                graphics.setColor(JagexColors.CHAT_FC_NAME_TRANSPARENT_BACKGROUND);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(poly);
            }

        }
    }

    private static void mes(Client client, String m, AzEasyTobPlugin pl) {
        if (pl.getConfig().leechWarnMes()) {
            Long cool = (Long)pl.getWarnCooldown().get(m);
            if (cool == null || cool <= System.currentTimeMillis()) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", m, "");
                pl.getWarnCooldown().put(m, System.currentTimeMillis() + 2200L);
            }
        }
    }

    private void displayMatomenosOverlays(Graphics2D graphics) {
        if (!this.plugin.getMaidenMatos().isEmpty() && this.plugin.getMaiden() != null) {
            ArrayListMultimap<WorldPoint, MatomenosDetails> matomenosGrouped = ArrayListMultimap.create();
            this.plugin.getMaidenMatos().values().forEach((detailsx) -> {
                NPC matomenos = detailsx.getMatomenosNpc();
                if (!matomenos.isDead()) {
                    matomenosGrouped.put(matomenos.getWorldLocation(), detailsx);
                    detailsx.updateHitpoints1();
                    if (this.config.maidenRedsMenuWC() && !matomenos.isDead()) {
                        String ft;
                        Point textLocation;
                        if ((this.client.getVarbitValue(4070) == 1 || this.config.debug()) && this.config.maidenRedsMenuWC() && matomenos.getPoseAnimation() != matomenos.getIdlePoseAnimation()) {
                            int d = detailsx.calculateDistanceTo(this.plugin.getMaiden());
                            if ((!detailsx.isHasBeenFrozen() || d > 2) && !this.config.groupedMaiden()) {
                                ft = Integer.toString(d);
                                textLocation = matomenos.getCanvasTextLocation(graphics, ft, 150);
                                this.renderTextLocation(graphics, textLocation, ft, Color.YELLOW);
                            }
                        }

                        if (this.config.maidenRedsMenuWC() && detailsx.getFrozenTicksOptStr().isPresent() && matomenos.getPoseAnimation() == matomenos.getIdlePoseAnimation() && !this.config.groupedMaiden()) {
                            ft = (String)detailsx.getFrozenTicksOptStr().get();
                            textLocation = matomenos.getCanvasTextLocation(graphics, ft, 150);
                            this.renderTextLocation(graphics, textLocation, ft, Color.CYAN);
                        }
                    }
                }

            });
            if (!matomenosGrouped.isEmpty()) {
                Iterator var3 = matomenosGrouped.keys().iterator();

                while(var3.hasNext()) {
                    WorldPoint worldPoint = (WorldPoint)var3.next();
                    int offset = 0;

                    for(Iterator var6 = matomenosGrouped.get(worldPoint).iterator(); var6.hasNext(); offset += graphics.getFontMetrics().getHeight()) {
                        MatomenosDetails details = (MatomenosDetails)var6.next();
                        //this.drawMatomenosOverlay(graphics, details, offset);
                    }
                }
            }
        }

    }

    private void drawMatomenosOverlay(Graphics2D graphics, MatomenosDetails details, int offset) {
        details.updateHitpoints2();
        if (this.config.maidenRedsMenuWC() || this.config.p2redsHp()) {
            NPC matomenos = details.getMatomenosNpc();
            float hitpoints = details.calculateHitpoints();
            //List<String> textPieces = new 1(this, details, hitpoints, matomenos);

            if (hitpoints > 0) {
                boolean DYNAMIC = true;
                String text = details.toString();
                Point textLocation = matomenos.getCanvasTextLocation(graphics, text, 0);
                if (!matomenos.isDead() && textLocation != null) {
                    Color color = DYNAMIC ? details.calculateHitpointsColor(hitpoints) : Color.WHITE;
                    this.renderTextLocation(graphics, new Point(textLocation.getX(), textLocation.getY() - offset), text, color);
                }
            }
        }

    }

    protected void renderProjectiles(Graphics2D graphics, Map<Projectile, String> projectiles) {
        Iterator itr = projectiles.entrySet().iterator();

        while(itr.hasNext()) {
            Entry<Projectile, String> entry = (Entry)itr.next();
            int projectileId = ((Projectile)entry.getKey()).getId();
            String text = (String)entry.getValue();
            int x = (int)((Projectile)entry.getKey()).getX();
            int y = (int)((Projectile)entry.getKey()).getY();
            LocalPoint projectilePoint = new LocalPoint(x, y);
            Point textLocation = Perspective.getCanvasTextLocation(this.client, graphics, projectilePoint, text, 0);
            if (textLocation != null) {
                if (projectileId == 1607) {
                    this.renderTextLocation(graphics, textLocation, text, new Color(57, 255, 20, 255));
                } else if (projectileId == 1606) {
                    this.renderTextLocation(graphics, textLocation, text, new Color(64, 224, 208, 255));
                } else if (projectileId == 1604) {
                    if (text.equals("0")) {
                        text = "NOW!";
                    }

                    this.renderTextLocation(graphics, textLocation, text, Color.ORANGE);
                } else if (projectileId == 1598) {
                    if (text.equals("0")) {
                        text = "NOW!";
                    }

                    this.renderTextLocation(graphics, textLocation, text, Color.GREEN);
                } else {
                    this.renderTextLocation(graphics, textLocation, text, Color.WHITE);
                }
            }
        }

    }

    private void findGreenBall(Graphics2D graphics, AzEasyTobPlugin plugin) {
        Iterator var2 = this.client.getProjectiles().iterator();
        if (this.config.verzBombCountdown()) {
            while(var2.hasNext()) {
                Projectile p = (Projectile)var2.next();
                Actor interacting = plugin.findActor(Maintainance.getInteractingIndex(p));
                if (p.getId() == 1598 && interacting != null) {
                    String text = String.valueOf(p.getRemainingCycles() / 30);
                    if (text.equals("0")) {
                        text = "NOW!";
                    }

                    LocalPoint lp = interacting.getLocalLocation();
                    Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, text, 0);
                    this.renderTextLocation(graphics, point, text, Color.GREEN);
                }
            }

        }
    }

    private void renderStompSafespots(Graphics2D graphics) {
        if (this.plugin.getBloatDown() != null) {
            BloatSafespot safespot = this.plugin.getBloatDown().getBloatSafespot();
            safespot.getSafespotLines().forEach((line) -> {
                Color color = Color.GRAY.darker();
                this.drawLine(graphics, line, color, 1);
            });
        }

    }

    protected void drawLine(Graphics2D graphics, @Nullable SSLine safespotLine, @Nonnull Color lineColor, int lineStroke) {
        if (safespotLine != null) {
            Point pointA = safespotLine.getTranslatedPointA(this.client);
            Point pointB = safespotLine.getTranslatedPointB(this.client);
            if (pointA != null && pointB != null) {
                graphics.setStroke(new BasicStroke((float)lineStroke));
                graphics.setColor(lineColor);
                graphics.drawLine(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
            }
        }

    }

    protected void renderNpcTLOverlay(Graphics2D graphics, NPC actor, Color color, int outlineWidth, int outlineAlpha, int fillAlpha) {
        int size = 1;
        NPCComposition composition = actor.getTransformedComposition();
        if (composition != null) {
            size = composition.getSize();
        }

        LocalPoint lp = LocalPoint.fromWorld(this.client, actor.getWorldLocation());
        if (lp != null) {
            lp = new LocalPoint(lp.getX() + size * 128 / 2 - 64, lp.getY() + size * 128 / 2 - 64);
            Polygon tilePoly = Perspective.getCanvasTileAreaPoly(this.client, lp, size);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
            graphics.setStroke(new BasicStroke((float)outlineWidth));
            graphics.draw(tilePoly);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
            graphics.fill(tilePoly);
        }

    }

    static {
        LAYER = OverlayLayer.ABOVE_SCENE;
    }
}
