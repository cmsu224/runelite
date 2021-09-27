/*
 * Copyright (c) 2018, James Swindle <wilingua@gmail.com>
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.npchighlight;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

public class NpcSceneOverlay extends Overlay
{
	// Anything but white text is quite hard to see since it is drawn on
	// a dark background
	private static final Color TEXT_COLOR = Color.WHITE;

	private static final NumberFormat TIME_LEFT_FORMATTER = DecimalFormat.getInstance(Locale.US);

	static
	{
		((DecimalFormat)TIME_LEFT_FORMATTER).applyPattern("#0.0");
	}

	private final Client client;
	private final NpcIndicatorsConfig config;
	private final NpcIndicatorsPlugin plugin;
	private final ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	NpcSceneOverlay(Client client, NpcIndicatorsConfig config, NpcIndicatorsPlugin plugin,
		ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		this.modelOutlineRenderer = modelOutlineRenderer;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.showRespawnTimer())
		{
			plugin.getDeadNpcsToDisplay().forEach((id, npc) -> renderNpcRespawn(npc, graphics));
		}

		for (NPC npc : plugin.getHighlightedNpcs())
		{
			renderNpcOverlay(graphics, npc, config.getHighlightColor());
		}

		return null;
	}

	private void renderNpcRespawn(final MemorizedNpc npc, final Graphics2D graphics)
	{
		if (npc.getPossibleRespawnLocations().isEmpty())
		{
			return;
		}

		final WorldPoint respawnLocation = npc.getPossibleRespawnLocations().get(0);
		final LocalPoint lp = LocalPoint.fromWorld(client, respawnLocation.getX(), respawnLocation.getY());

		if (lp == null)
		{
			return;
		}

		final Color color = config.getHighlightColor();

		final LocalPoint centerLp = new LocalPoint(
			lp.getX() + Perspective.LOCAL_TILE_SIZE * (npc.getNpcSize() - 1) / 2,
			lp.getY() + Perspective.LOCAL_TILE_SIZE * (npc.getNpcSize() - 1) / 2);

		final Polygon poly = Perspective.getCanvasTileAreaPoly(client, centerLp, npc.getNpcSize());

		if (poly != null)
		{
			OverlayUtil.renderPolygon(graphics, poly, color);
		}

		final Instant now = Instant.now();
		final double baseTick = ((npc.getDiedOnTick() + npc.getRespawnTime()) - client.getTickCount()) * (Constants.GAME_TICK_LENGTH / 1000.0);
		final double sinceLast = (now.toEpochMilli() - plugin.getLastTickUpdate().toEpochMilli()) / 1000.0;
		final double timeLeft = Math.max(0.0, baseTick - sinceLast);
		final String timeLeftStr = TIME_LEFT_FORMATTER.format(timeLeft);

		final int textWidth = graphics.getFontMetrics().stringWidth(timeLeftStr);
		final int textHeight = graphics.getFontMetrics().getAscent();

		final Point canvasPoint = Perspective
			.localToCanvas(client, centerLp, respawnLocation.getPlane());

		if (canvasPoint != null)
		{
			final Point canvasCenterPoint = new Point(
				canvasPoint.getX() - textWidth / 2,
				canvasPoint.getY() + textHeight / 2);

			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, timeLeftStr, TEXT_COLOR);
		}
	}

	private void renderNpcOverlay(Graphics2D graphics, NPC actor, Color color)
	{
		NPCComposition npcComposition = actor.getTransformedComposition();
		if (npcComposition == null || !npcComposition.isInteractible()
			|| (actor.isDead() && config.ignoreDeadNpcs()))
		{
			return;
		}

		if (config.highlightHull())
		{
			Shape objectClickbox = actor.getConvexHull();
			renderPoly(graphics, color, objectClickbox);
		}

		if (config.highlightTile())
		{
			int size = npcComposition.getSize();
			LocalPoint lp = actor.getLocalLocation();
			Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);

			if (config.drawBehindNPC()){
				Rectangle bounds = tilePoly.getBounds();
				BufferedImage tileImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_4BYTE_ABGR);
				tilePoly.translate(-bounds.x, -bounds.y);
				Graphics2D g = tileImage.createGraphics();
				g.setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				OverlayUtil.renderPolygon(g, tilePoly, color, new BasicStroke((float) config.borderWidth()));
				removeActor(g, actor, bounds);

				if(config.drawBehindPlayer()){
					removeActor(g, client.getLocalPlayer(), bounds);
				}
				if(config.drawBehindAllNPCs()){
					List<NPC> npcList = client.getNpcs();
					for (NPC n : npcList) {
						if(n != actor){
							removeActor(g, n, bounds);
						}
					}
				}
				if(config.drawBehindAllPlayers()){
					List<Player> playerList = client.getPlayers();
					for (Player p : playerList) {
						if(p != client.getLocalPlayer()){
							removeActor(g, p, bounds);
						}
					}
				}

				graphics.drawImage(tileImage, bounds.x, bounds.y, null);
			}else{
				renderPoly(graphics, color, tilePoly);
			}
		}

		if (config.highlightSouthWestTile())
		{
			int size = npcComposition.getSize();
			LocalPoint lp = actor.getLocalLocation();

			int x = lp.getX() - ((size - 1) * Perspective.LOCAL_TILE_SIZE / 2);
			int y = lp.getY() - ((size - 1) * Perspective.LOCAL_TILE_SIZE / 2);

			Polygon southWestTilePoly = Perspective.getCanvasTilePoly(client, new LocalPoint(x, y));

			renderPoly(graphics, color, southWestTilePoly);
		}

		if (config.highlightOutline())
		{
			modelOutlineRenderer.drawOutline(actor, (int)config.borderWidth(), color, config.outlineFeather());
		}

		if (config.drawNames() && actor.getName() != null)
		{
			String npcName = Text.removeTags(actor.getName());
			Point textLocation = actor.getCanvasTextLocation(graphics, npcName, actor.getLogicalHeight() + 40);

			if (textLocation != null)
			{
				OverlayUtil.renderTextLocation(graphics, textLocation, npcName, color);
			}
		}
	}


	private void removeActor(final Graphics2D graphics, final Actor actor, Rectangle bounds)
	{
		graphics.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		Model model = actor.getModel();
		int vCount = model.getVerticesCount();
		int[] x3d = model.getVerticesX();
		int[] y3d = model.getVerticesY();
		int[] z3d = model.getVerticesZ();

		int[] x2d = new int[vCount];
		int[] y2d = new int[vCount];

		int localX = actor.getLocalLocation().getX();
		int localY = actor.getLocalLocation().getY();
		int localZ = Perspective.getTileHeight(client, client.getLocalPlayer().getLocalLocation(), client.getPlane());
		int rotation = actor.getOrientation();

		Perspective.modelToCanvas(client, vCount, localX, localY, localZ, rotation, x3d, z3d, y3d, x2d, y2d);

		int tCount = model.getTrianglesCount();
		int[] tx = model.getTrianglesX();
		int[] ty = model.getTrianglesY();
		int[] tz = model.getTrianglesZ();

		Composite orig = graphics.getComposite();
		graphics.setComposite(AlphaComposite.Clear);
		graphics.setColor(Color.WHITE);
		for (int i = 0; i < tCount; i++) {
			// Cull tris facing away from the camera and tris outside of the tile.
			if (getTriDirection(x2d[tx[i]], y2d[tx[i]], x2d[ty[i]], y2d[ty[i]], x2d[tz[i]], y2d[tz[i]]) >= 0 || (!bounds.contains(x2d[tx[i]], y2d[tx[i]]) && !bounds.contains(x2d[ty[i]], y2d[ty[i]]) && !bounds.contains(x2d[tz[i]], y2d[tz[i]]))) {
				continue;
			}
			int xShift = -bounds.x;
			int yShift = -bounds.y;
			Polygon p = new Polygon(
					new int[]{x2d[tx[i]]+xShift,x2d[ty[i]]+xShift,x2d[tz[i]]+xShift},
					new int[]{y2d[tx[i]]+yShift,y2d[ty[i]]+yShift,y2d[tz[i]]+yShift},
					3);
			graphics.fill(p);

		}
		graphics.setComposite(orig);
		graphics.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	private int getTriDirection(int x1, int y1, int x2, int y2, int x3, int y3) {
		int x4 = x2 - x1;
		int y4 = y2 - y1;
		int x5 = x3 - x1;
		int y5 = y3 - y1;
		return x4 * y5 - y4 * x5;
	}

	private void renderPoly(Graphics2D graphics, Color color, Shape polygon)
	{
		if (polygon != null)
		{
			graphics.setColor(color);
			graphics.setStroke(new BasicStroke((float) config.borderWidth()));
			graphics.draw(polygon);
			graphics.setColor(ColorUtil.colorWithAlpha(color, 20));
			graphics.fill(polygon);
		}
	}
}
