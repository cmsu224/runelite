//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.runelite.client.util.RSTimeUnit;

class GameTimer {
    private final Instant startTime = Instant.now();
    private Instant prevWave;

    GameTimer() {
        this.prevWave = this.startTime;
    }

    String getTime(boolean waveTime) {
        Instant now = Instant.now();
        Duration elapsed;
        if (waveTime) {
            elapsed = Duration.between(this.prevWave, now);
        } else {
            elapsed = Duration.between(this.startTime, now).minus(Duration.of(1L, RSTimeUnit.GAME_TICKS));
        }

        return formatTime(LocalTime.ofSecondOfDay(elapsed.getSeconds()));
    }

    void setWaveStartTime() {
        this.prevWave = Instant.now();
    }

    private static String formatTime(LocalTime time) {
        if (time.getHour() > 0) {
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return time.getMinute() > 9 ? time.format(DateTimeFormatter.ofPattern("mm:ss")) : time.format(DateTimeFormatter.ofPattern("m:ss"));
        }
    }
}
