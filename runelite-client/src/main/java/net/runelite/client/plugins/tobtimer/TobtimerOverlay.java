package net.runelite.client.plugins.tobtimer;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.*;
import java.time.Duration;

public class TobtimerOverlay extends OverlayPanel
{
    private Client client;
    private TobtimerPlugin plugin;
    @Inject
    private TobtimerConfig config;
    private String longestString;

    @Inject
    private TobtimerOverlay(Client client, TobtimerPlugin plugin) {
        this.setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (plugin.isShowOverlay())
        {
            longestString = "";
            Duration elapsedTime = plugin.getTimer().getRealTime();

            Color timeColor = config.timeColor();

            if (!plugin.getTimer().isActive())
            {
                timeColor = Color.GREEN;
            }

            String currentTime = createDurationString(elapsedTime);

            panelComponent.getChildren().clear();
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(currentTime)
                    .leftColor(timeColor)
                    .build());
            setLongestString(currentTime);

            // Some characters are wider than others. Replace all of them with "9" in order to prevent shaking of the panel overlay.
            longestString = longestString.replaceAll("([0-8]|[ ]|[-])", "9");

            panelComponent.setPreferredSize(new Dimension(
                    graphics.getFontMetrics().stringWidth(longestString) + 6, 0));
        }

        return super.render(graphics);
    }

    private void setLongestString(String string)
    {
        if (longestString == null || string.length() > longestString.length())
        {
            longestString = string;
        }
    }

    private static String createDurationString(Duration duration)
    {
        if (duration == null)
        {
            return "--:--.-";
        }

        long hours = Math.abs(duration.getSeconds() / 3600);
        long minutes = Math.abs((duration.getSeconds() % 3600) / 60);
        long seconds = Math.abs(duration.getSeconds() % 60);
        long millis = Math.abs(duration.toMillis() - (duration.getSeconds() * 1000)) / 100;

        return (duration.isNegative() ? "-" : "") + (hours > 0
                ? String.format("%1d:%02d:%02d.%1d", hours, minutes, seconds, millis)
                : String.format("%02d:%02d.%1d", minutes, seconds, millis));
    }
}