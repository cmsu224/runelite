package net.runelite.client.plugins.ztob;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.ztob.rooms.Nylocas.NylocasWave;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class NylocasNotesOverlay extends Overlay
{
    private static final String prefix = "Nylocas alive: ";
    private final PanelComponent panelComponent = new PanelComponent();
    private LineComponent waveComponent;
    private TheatreConfig config;
    private Client client;
    @Setter
    private Instant nyloWaveStart;

    @Getter
    private int wave = 0;
    @Setter
    @Getter
    private boolean hidden = false;
    @Getter
    private String waveNotes = "";
    @Getter
    private String waveStartLoc = "middle";
    @Getter
    private String nextStartLoc = "";
    @Getter
    private String weapon = "";
    @Getter
    private String nextWeapon = "";

    @Inject
    private NylocasNotesOverlay(Client client, TheatreConfig config)
    {
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void setWave(int wave)
    {
        this.wave = wave;
    }

    public void setNotes(String notes)
    {
        this.waveNotes = notes;
    }

    public void setStartLoc(String startLoc)
    {
        this.waveStartLoc = startLoc;
    }

    public void setWeapon(String weapon)
    {
        this.weapon = weapon;
    }

    public void setNextWeapon(String weapon)
    {
        this.nextWeapon = weapon;
    }

    public void setNextLoc(String nextLoc){ this.nextStartLoc = nextLoc; }
    @Override
    public Dimension render(Graphics2D graphics)
    {
        if(wave < 1 || wave > 30){
            return null;
        }
        try {
            int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
            WorldPoint wp = WorldPoint.fromRegion(regionID,24,24, 0);
            LocalPoint lp;
            String lable = wave + " " + weapon;
            Color tColor = Color.GREEN;

            if(waveStartLoc.equals("middle"))
            {
                wp = WorldPoint.fromRegion(regionID,24,24, 0);
            }
            if(waveStartLoc.equals("east"))
            {
                wp = WorldPoint.fromRegion(regionID,28,24, 0);
            }
            if(waveStartLoc.equals("south"))
            {
                wp = WorldPoint.fromRegion(regionID,24,19, 0);
            }
            if(waveStartLoc.equals("west"))
            {
                wp = WorldPoint.fromRegion(regionID,19,24, 0);
            }

            //drawTile(graphics, wp, tColor, lable, new BasicStroke((float) 1));
            lp = LocalPoint.fromWorld(client, wp);
            renderTileObject(graphics, lp, lable, tColor);

            lable = wave+1 + " " + nextWeapon;
            tColor = Color.yellow;

            if(nextStartLoc.equals("middle"))
            {
                wp = WorldPoint.fromRegion(regionID,24,24, 0);
            }
            if(nextStartLoc.equals("east"))
            {
                wp = WorldPoint.fromRegion(regionID,28,24, 0);
            }
            if(nextStartLoc.equals("south"))
            {
                wp = WorldPoint.fromRegion(regionID,24,19, 0);
            }
            if(nextStartLoc.equals("west"))
            {
                wp = WorldPoint.fromRegion(regionID,19,24, 0);
            }
            //drawTile(graphics, wp, tColor, lable, new BasicStroke((float) 1));
            lp = LocalPoint.fromWorld(client, wp);
            renderTileObject(graphics, lp, lable, tColor);
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to draw tile", e);
        }

        return null;
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color, @Nullable String label, Stroke borderStroke)
    {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        if (point.distanceTo(playerLocation) >= 32)
        {
            return;
        }

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null)
        {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly != null)
        {
            try{
                OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, 100), borderStroke);
            }
            catch(Exception e) {
                throw new RuntimeException("Unable to draw tile during renderPolygon", e);
            }
        }

        if (!Strings.isNullOrEmpty(label))
        {
            Point canvasTextLocation = Perspective.getCanvasTextLocation(client, graphics, lp, label, 0);
            if (canvasTextLocation != null)
            {
                OverlayUtil.renderTextLocation(graphics, canvasTextLocation, label, color);
            }
        }
    }

    //Draw game objects
    private void renderTileObject(Graphics2D graphics, LocalPoint lp, String label, Color color) {
        if (lp != null) {
            graphics.setStroke(new BasicStroke(1.0F));

            Polygon poly;
            poly = Perspective.getCanvasTileAreaPoly(this.client, lp, 1);

            if (poly != null)
            {
                graphics.setColor(color);
                graphics.setStroke(new BasicStroke(1.0F));
                graphics.draw(poly);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                graphics.fill(poly);

                Point textLocation = Perspective.getCanvasTextLocation(client, graphics, lp, label, 0);

                graphics.setColor(Color.BLACK);
                graphics.drawString(label, textLocation.getX() + 1, textLocation.getY() + 1);
                graphics.setColor(Color.WHITE);
                graphics.drawString(label, textLocation.getX(), textLocation.getY());
            }
        }

    }
}
