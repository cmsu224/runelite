package net.runelite.client.plugins.maz.plugins.countdown;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class CountDownOverlay extends OverlayPanel {
    private final CountDownPlugin plugin;
    private final CountDownConfig config;
    private String longestString;

    @Inject
    private CountDownOverlay(CountDownPlugin plugin, CountDownConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_CENTER);
        this.setPriority(OverlayPriority.LOW);
        this.plugin = plugin;
        this.config = config;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "CountDown Overlay"));
    }

    public Dimension render(Graphics2D graphics) {
        Color timeColor = Color.GREEN;
        Color nameColor = Color.WHITE;
        LocalDateTime cdDateTime = LocalDateTime.parse(this.config.date());
        LocalDateTime cdCurrentTimer = LocalDateTime.now();
        int days = (int)ChronoUnit.DAYS.between(cdDateTime, cdCurrentTimer);
        int hours = (int)ChronoUnit.HOURS.between(cdDateTime, cdCurrentTimer) % 24;
        int mins = (int)ChronoUnit.MINUTES.between(cdDateTime, cdCurrentTimer) % 60;
        int secs = (int)ChronoUnit.SECONDS.between(cdDateTime, cdCurrentTimer) % 60;
        String text = Math.abs(days) + "d | " + Math.abs(hours) + "h | " + Math.abs(mins) + "m | " + Math.abs(secs) + "s";
        if (this.config.cname().length() > 0) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text(this.config.cname()).color(nameColor).build());
        }

        if (text.length() > 0) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text(text).color(timeColor).build());
        }

        this.panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(text + nameColor) + 10, 0));
        return super.render(graphics);
    }

    private void setLongestString(String string) {
        if (this.longestString == null || string.length() > this.longestString.length()) {
            this.longestString = string;
        }

    }

    private static String createDurationString(Duration duration) {
        if (duration == null) {
            return "--:--.-";
        } else {
            long hours = Math.abs(duration.getSeconds() / 3600L);
            long minutes = Math.abs(duration.getSeconds() % 3600L / 60L);
            long seconds = Math.abs(duration.getSeconds() % 60L);
            long millis = Math.abs(duration.toMillis() - duration.getSeconds() * 1000L) / 100L;
            return (duration.isNegative() ? "-" : "") + (hours > 0L ? String.format("%1d:%02d:%02d.%1d", hours, minutes, seconds, millis) : String.format("%02d:%02d.%1d", minutes, seconds, millis));
        }
    }
}
