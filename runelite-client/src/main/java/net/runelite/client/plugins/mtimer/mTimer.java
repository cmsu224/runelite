
package net.runelite.client.plugins.mtimer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

public class mTimer
{
    @Getter
    private boolean active;
    private int ticksElapsed;
    private LocalDateTime lastElapsedTick;

    public Duration getGameTime()
    {
        return Duration.ofMillis(600 * ticksElapsed);
    }

    public Duration getRealTime()
    {
        if (active && lastElapsedTick != null)
        {
            long millisSinceTick = ChronoUnit.MILLIS.between(lastElapsedTick, LocalDateTime.now());
            return getGameTime().plus(Duration.ofMillis(millisSinceTick));
        }

        return getGameTime();
    }

    public void reset()
    {
        stop();
        ticksElapsed = 0;
        lastElapsedTick = null;
    }

    public void start()
    {
        active = true;
    }

    public void stop()
    {
        active = false;
    }

    public void tick()
    {
        if (active)
        {
            ticksElapsed++;
            lastElapsedTick = LocalDateTime.now();
        }
    }
}