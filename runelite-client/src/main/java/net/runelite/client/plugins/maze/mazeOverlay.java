package net.runelite.client.plugins.maze;

import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class mazeOverlay extends Overlay
{
    private static final int MAX_DRAW_DISTANCE = 32;

    private final Client client;
    private final mazeConfig config;
    private final mazePlugin plugin;

    @Inject
    private mazeOverlay(Client client, mazeConfig config, mazePlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
            for (GroundObject bog : plugin.getMazeList())
            {
                Polygon bogPoly = bog.getCanvasTilePoly();
                OverlayUtil.renderPolygon(graphics, bogPoly, Color.WHITE);
            }
        return null;
    }
}
