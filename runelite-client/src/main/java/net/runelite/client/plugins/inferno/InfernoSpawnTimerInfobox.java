//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.inferno;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Instant;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

class InfernoSpawnTimerInfobox extends InfoBox {
    private static final long SPAWN_DURATION = 210L;
    private static final long SPAWN_DURATION_INCREMENT = 105L;
    private static final long SPAWN_DURATION_WARNING = 120L;
    private static final long SPAWN_DURATION_DANGER = 30L;
    private long timeRemaining;
    private long startTime;
    private boolean running;

    InfernoSpawnTimerInfobox(BufferedImage image, InfernoPlugin plugin) {
        super(image, plugin);
        this.setPriority(InfoBoxPriority.HIGH);
        this.running = false;
        this.timeRemaining = 210L;
    }

    void run() {
        this.startTime = Instant.now().getEpochSecond();
        this.running = true;
    }

    void reset() {
        this.running = false;
        this.timeRemaining = 210L;
    }

    void pause() {
        if (this.running) {
            this.running = false;
            long timeElapsed = Instant.now().getEpochSecond() - this.startTime;
            this.timeRemaining = Math.max(0L, this.timeRemaining - timeElapsed);
            this.timeRemaining += 105L;
        }
    }

    public String getText() {
        long seconds = this.running ? Math.max(0L, this.timeRemaining - (Instant.now().getEpochSecond() - this.startTime)) : this.timeRemaining;
        long minutes = seconds % 3600L / 60L;
        long secs = seconds % 60L;
        return String.format("%02d:%02d", minutes, secs);
    }

    public Color getTextColor() {
        long seconds = this.running ? Math.max(0L, this.timeRemaining - (Instant.now().getEpochSecond() - this.startTime)) : this.timeRemaining;
        return seconds <= 30L ? Color.RED : (seconds <= 120L ? Color.ORANGE : Color.GREEN);
    }

    public boolean render() {
        return true;
    }

    public boolean cull() {
        return false;
    }

    boolean isRunning() {
        return this.running;
    }
}
