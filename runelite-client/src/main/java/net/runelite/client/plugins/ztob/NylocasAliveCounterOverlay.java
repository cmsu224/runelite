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
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.ztob.rooms.Nylocas.NylocasWave;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class NylocasAliveCounterOverlay extends Overlay
{
	private static final String prefix = "Nylocas alive: ";
	private final PanelComponent panelComponent = new PanelComponent();
	private LineComponent waveComponent;
	private TheatreConfig config;
	private Client client;
	@Setter
	private Instant nyloWaveStart;

	@Getter
	private int nyloAlive = 0;
	@Getter
	private int maxNyloAlive = 12;
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
	private String weapon = "";

	@Inject
	private NylocasAliveCounterOverlay(Client client, TheatreConfig config)
	{
		this.config = config;
		this.client = client;
		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.HIGH);
		refreshPanel();
	}

	public void setNyloAlive(int aliveCount)
	{
		nyloAlive = aliveCount;
		refreshPanel();
	}

	public void setMaxNyloAlive(int maxAliveCount)
	{
		maxNyloAlive = maxAliveCount;
		refreshPanel();
	}

	public void setWave(int wave)
	{
		this.wave = wave;
		refreshPanel();
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

	private void refreshPanel()
	{
		waveComponent = LineComponent.builder().left(waveNotes).build();
		panelComponent.getChildren().clear();
		panelComponent.getChildren().add(waveComponent);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if(wave < 1 || wave > 31){
			return null;
		}
		if(waveNotes == null || waveNotes.equals("")){
			return null;
		}
		return panelComponent.render(graphics);
	}

	public String getFormattedTime()
	{
		Duration duration = Duration.between(nyloWaveStart, Instant.now());
		LocalTime localTime = LocalTime.ofSecondOfDay(duration.getSeconds());
		return localTime.format(DateTimeFormatter.ofPattern("mm:ss"));
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
			OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, 100), borderStroke);
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
}
