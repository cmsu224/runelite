//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.vetion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

public class VetionOverlay extends Overlay {
    private static final Color RED_ALPHA;
    private static final Duration MAX_TIME;
    private final VetionPlugin plugin;
    private final Client client;
    private final VetionConfig config;

    @Inject
    private VetionOverlay(Client client, VetionPlugin plugin, VetionConfig config) {
        this.plugin = plugin;
        this.client = client;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.determineLayer();
    }

    public Dimension render(Graphics2D graphics) {
        this.plugin.getVetions().forEach((actor, timer) -> {
            LocalPoint localPos = actor.getLocalLocation();
            if (localPos != null) {
                Point position = Perspective.localToCanvas(this.client, localPos, this.client.getPlane(), actor.getLogicalHeight() + 96);
                if (position != null) {
                    position = new Point(position.getX(), position.getY());
                    ProgressPieComponent progressPie = new ProgressPieComponent();
                    progressPie.setDiameter(30);
                    progressPie.setFill(RED_ALPHA);
                    progressPie.setBorderColor(Color.RED);
                    progressPie.setPosition(position);
                    Duration duration = Duration.between(timer, Instant.now());
                    progressPie.setProgress(1.0D - (duration.compareTo(MAX_TIME) < 0 ? (double)duration.toMillis() / (double)MAX_TIME.toMillis() : 1.0D));
                    progressPie.render(graphics);
                    if (1 - duration.compareTo(MAX_TIME) < 0) {
                        this.plugin.getVetions().remove(actor);
                    }
                }
            }

        });
        return null;
    }

    public void determineLayer() {
        if (this.config.mirrorMode()) {
            this.setLayer(OverlayLayer.AFTER_MIRROR);
        }

        if (!this.config.mirrorMode()) {
            this.setLayer(OverlayLayer.ABOVE_SCENE);
        }

    }

    static {
        RED_ALPHA = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 100);
        MAX_TIME = Duration.ofSeconds(9L);
    }
}
