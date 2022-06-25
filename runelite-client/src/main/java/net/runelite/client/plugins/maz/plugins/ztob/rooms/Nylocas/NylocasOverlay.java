package net.runelite.client.plugins.maz.plugins.ztob.rooms.Nylocas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.HashSet;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.maz.plugins.ztob.RoomOverlay;
import net.runelite.client.plugins.maz.plugins.ztob.TheatreConfig;
import net.runelite.client.ui.overlay.OverlayUtil;

public class NylocasOverlay extends RoomOverlay
{
	@Inject
	private Nylocas nylocas;

	@Inject
	protected NylocasOverlay(TheatreConfig config)
	{
		super(config);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
        //Room instance time start
	    if (nylocas.isInstanceTimerRunning() && nylocas.isInNyloRegion() && config.nyloInstanceTimer())
        {
            Player player = client.getLocalPlayer();
            if (player != null)
            {
                Point point = player.getCanvasTextLocation(graphics, "#", player.getLogicalHeight() + 60);
                if (point != null)
                {
                    OverlayUtil.renderTextLocation(graphics, point, String.valueOf(nylocas.getInstanceTimer()), Color.CYAN);
                }
            }
        }


        if (nylocas.isNyloActive())
        {
            //add pillar health
            if (config.nyloPillars())
            {
                Map<NPC, Integer> pillars = nylocas.getNylocasPillars();
                for (NPC npc : pillars.keySet()) {
                    final int health = pillars.get(npc);
                    final String healthStr = String.valueOf(health) + "%";
                    WorldPoint p = npc.getWorldLocation();
                    LocalPoint lp = LocalPoint.fromWorld(client, p.getX() + 1, p.getY() + 1);
                    final double rMod = 130.0 * health / 100.0;
                    final double gMod = 255.0 * health / 100.0;
                    final double bMod = 125.0 * health / 100.0;
                    final Color c = new Color((int) (255 - rMod), (int) (0 + gMod), (int) (0 + bMod));
                    if (lp != null)
                    {
                        Point canvasPoint = Perspective.localToCanvas(client, lp, client.getPlane(),
                                                                      65);
                        renderTextLocation(graphics, healthStr, c, canvasPoint);
                    }
                }
            }

            //Highlights
            if (config.nyloBlasts() || config.nyloTimeAlive() || config.getHighlightMageNylo() || config.getHighlightMeleeNylo() || config.getHighlightRangeNylo() || config.nyloAggressiveOverlay()
                || config.nyloPrefiresDuoMage() || config.nyloPrefiresDuoRange() || config.nyloPrefiresTrioRange() || config.nyloPrefiresTrioMelee() || config.nyloPrefiresTrioMage()
                || config.nyloPrefires4manMage() || config.nyloPrefires4manMeleeWest() || config.nyloPrefires4manMeleeEast() || config.nyloPrefires4manRange()
                || config.nyloPrefires5manMageEast() || config.nyloPrefires5manMageWest() || config.nyloPrefires5manMeleeWest() || config.nyloPrefires5manMeleeEast() || config.nyloPrefires5manRange())
            {
                final Map<NPC, Integer> npcMap = nylocas.getNylocasNpcs();
                for (NPC npc : npcMap.keySet())
                {
                    /////////////////////////////////////////////DUO///////////////////////////////////////////////
                    //prefire highligh mage duo
                    if (config.nyloPrefiresDuoMage() && nylocas.getPrefireDuoMageNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefireDuoMageNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.CYAN, poly, 1);
                        }
                    }
                    //prefire highligh range duo
                    if (config.nyloPrefiresDuoRange() && nylocas.getPrefireDuoRangeNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefireDuoRangeNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.GREEN, poly, 1);
                        }
                    }


                    /////////////////////////////////////////////TRIO///////////////////////////////////////////////
                    //prefire highligh mage trio
                    if (config.nyloPrefiresTrioMage() && nylocas.getPrefireTrioMageNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefireTrioMageNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.CYAN, poly, 1);
                        }
                    }
                    //prefire highligh range trio
                    if (config.nyloPrefiresTrioRange() && nylocas.getPrefireTrioRangeNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefireTrioRangeNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.GREEN, poly, 1);
                        }
                    }

                    //prefire highligh melee trio
                    if (config.nyloPrefiresTrioMelee() && nylocas.getPrefireTrioMeleeNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefireTrioMeleeNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.WHITE, poly, 1);
                        }
                    }

                    /////////////////////////////////////////////4MAN///////////////////////////////////////////////
                    //prefire highligh mage 4man
                    if (config.nyloPrefires4manMage() && nylocas.getPrefire4manMageNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire4manMageNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.CYAN, poly, 1);
                        }
                    }
                    //prefire highligh range 4man
                    if (config.nyloPrefires4manRange() && nylocas.getPrefire4manRangeNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire4manRangeNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.GREEN, poly, 1);
                        }
                    }

                    //prefire highligh melee west 4man
                    if (config.nyloPrefires4manMeleeWest() && nylocas.getPrefire4manMeleeWestNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire4manMeleeWestNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.WHITE, poly, 1);
                        }
                    }

                    //prefire highligh melee east 4man
                    if (config.nyloPrefires4manMeleeEast() && nylocas.getPrefire4manMeleeEastNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire4manMeleeEastNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.WHITE, poly, 1);
                        }
                    }

                    /////////////////////////////////////////////5MAN///////////////////////////////////////////////
                    //prefire highligh mage west 5man
                    if (config.nyloPrefires5manMageWest() && nylocas.getPrefire5manMageWestNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire5manMageWestNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.CYAN, poly, 1);
                        }
                    }
                    //prefire highligh mage east 5man
                    if (config.nyloPrefires5manMageEast() && nylocas.getPrefire5manMageEastNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire5manMageEastNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.CYAN, poly, 1);
                        }
                    }
                    //prefire highligh range 5man
                    if (config.nyloPrefires5manRange() && nylocas.getPrefire5manRangeNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire5manRangeNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.GREEN, poly, 1);
                        }
                    }

                    //prefire highligh melee west 5man
                    if (config.nyloPrefires5manMeleeWest() && nylocas.getPrefire5manMeleeWestNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire5manMeleeWestNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.WHITE, poly, 1);
                        }
                    }

                    //prefire highligh melee east 5man
                    if (config.nyloPrefires5manMeleeEast() && nylocas.getPrefire5manMeleeEastNylos().contains(npc) && !npc.isDead())
                    {
                        HashSet<NPC> prefires = nylocas.getPrefire5manMeleeEastNylos();
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.WHITE, poly, 1);
                        }
                    }

                    //aggressive highlight
                    if (config.nyloAggressiveOverlay() && nylocas.getAggressiveNylocas().contains(npc) && !npc.isDead())
                    {
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            Polygon poly = getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -25);
                            renderPoly(graphics, Color.RED, poly, 1);
                        }
                    }

                    //explosion highlight
                    int ticksLeft = npcMap.get(npc);
                    if (ticksLeft > -1)
                    {
                        if (config.nyloTimeAlive() && !npc.isDead())
                        {
                            int ticksAlive = 52 - ticksLeft;
                            Point textLocation = npc.getCanvasTextLocation(graphics, String.valueOf(ticksAlive), 60);
                            if (textLocation != null)
                            {
                                OverlayUtil.renderTextLocation(graphics, textLocation, String.valueOf(ticksAlive), Color.WHITE);
                            }
                        }

                        if (config.nyloBlasts() && ticksLeft <= 6) {
                            LocalPoint lp = npc.getLocalLocation();
                            if (lp != null)
                            {
                                renderPoly(graphics, Color.YELLOW, getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize(), -15), 1);
                            }
                        }
                    }

                    //Name highlight
                    String name = npc.getName();

                    if (config.nyloOverlay() && !npc.isDead())
                    {
                        LocalPoint lp = npc.getLocalLocation();
                        if (lp != null)
                        {
                            if (config.getHighlightMeleeNylo() && "Nylocas Ischyros".equals(name))
                            {
                                renderPoly(graphics, new Color(255, 188, 188), Perspective.getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize()), 1);
                            }
                            else if (config.getHighlightRangeNylo() && "Nylocas Toxobolos".equals(name))
                            {
                                renderPoly(graphics, Color.GREEN, Perspective.getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize()), 1);
                            }
                            else if (config.getHighlightMageNylo() && "Nylocas Hagios".equals(name))
                            {
                                renderPoly(graphics, Color.CYAN, Perspective.getCanvasTileAreaPoly(client, lp, npc.getComposition().getSize()), 1);
                            }
                        }
                    }
                }
            }
        }
		return null;
	}
}
