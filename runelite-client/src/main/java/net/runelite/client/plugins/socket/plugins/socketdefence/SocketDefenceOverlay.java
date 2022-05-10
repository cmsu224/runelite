/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Perspective
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.client.ui.overlay.OverlayLayer
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.OverlayPriority
 *  net.runelite.client.ui.overlay.outline.ModelOutlineRenderer
 */
package net.runelite.client.plugins.socket.plugins.socketdefence;

import com.google.common.collect.ImmutableSet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.socket.plugins.socketdefence.SocketDefenceConfig;
import net.runelite.client.plugins.socket.plugins.socketdefence.SocketDefencePlugin;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
public class SocketDefenceOverlay
extends OverlayPanel {
    private static final Set<Integer> GAP = ImmutableSet.of(34, 33, 26, 25, 18, 17, new Integer[]{10, 9, 2, 1});
    private final Client client;
    private final SocketDefencePlugin plugin;
    private final SocketDefenceConfig config;
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private SocketDefenceOverlay(Client client, SocketDefencePlugin plugin, SocketDefenceConfig config, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.corpChally() != SocketDefenceConfig.CorpTileMode.OFF) {
            for (NPC npc : this.client.getNpcs()) {
                Polygon tilePoly;
                LocalPoint lp;
                if (npc.getName() == null || !npc.getName().equalsIgnoreCase("corporeal beast")) continue;
                Color color = Color.RED;
                if (this.plugin.bossDef >= 0.0 && this.plugin.bossDef <= 10.0) {
                    color = Color.GREEN;
                }
                if (this.config.corpChally() == SocketDefenceConfig.CorpTileMode.AREA) {
                    this.renderAreaOverlay(graphics, npc, color);
                    continue;
                }
                if (this.config.corpChally() == SocketDefenceConfig.CorpTileMode.TILE) {
                    NPCComposition npcComp = npc.getComposition();
                    int size = npcComp.getSize();
                    lp = npc.getLocalLocation();
                    tilePoly = Perspective.getCanvasTileAreaPoly((Client)this.client, (LocalPoint)lp, (int)size);
                    this.renderPoly(graphics, color, tilePoly);
                    continue;
                }
                if (this.config.corpChally() == SocketDefenceConfig.CorpTileMode.HULL) {
                    Shape objectClickbox = npc.getConvexHull();
                    if (objectClickbox == null) continue;
                    graphics.setStroke(new BasicStroke(this.config.corpChallyThicc()));
                    graphics.setColor(color);
                    graphics.draw(objectClickbox);
                    continue;
                }
                if (this.config.corpChally() == SocketDefenceConfig.CorpTileMode.TRUE_LOCATION) {
                    int size = 1;
                    NPCComposition composition = npc.getTransformedComposition();
                    if (composition != null) {
                        size = composition.getSize();
                    }
                    if ((lp = LocalPoint.fromWorld((Client)this.client, (WorldPoint)npc.getWorldLocation())) == null) continue;
                    lp = new LocalPoint(lp.getX() + size * 128 / 2 - 64, lp.getY() + size * 128 / 2 - 64);
                    tilePoly = Perspective.getCanvasTileAreaPoly((Client)this.client, (LocalPoint)lp, (int)size);
                    this.renderPoly(graphics, color, tilePoly);
                    continue;
                }
                if (this.config.corpChally() != SocketDefenceConfig.CorpTileMode.OUTLINE) continue;
                this.modelOutlineRenderer.drawOutline(npc, this.config.corpChallyThicc(), color, this.config.corpGlow());
            }
        }
        if (this.config.vulnOutline() && this.plugin.vulnHit) {
            for (NPC npc : this.client.getNpcs()) {
                Shape objectClickbox;
                if (npc.getName() == null || !npc.getName().equalsIgnoreCase(this.plugin.boss) || (objectClickbox = npc.getConvexHull()) == null) continue;
                graphics.setStroke(new BasicStroke(2.0f));
                graphics.setColor(this.config.vulnColor());
                graphics.draw(objectClickbox);
            }
        }
        return null;
    }

    private void renderAreaOverlay(Graphics2D graphics, NPC actor, Color color) {
        Shape objectClickbox = actor.getConvexHull();
        if (objectClickbox != null) {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
            graphics.fill(actor.getConvexHull());
        }
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(this.config.corpChallyThicc()));
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), this.config.corpChallyOpacity()));
            graphics.fill(polygon);
        }
    }
}

