//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.NonNull;
import net.runelite.client.util.RSTimeUnit;

class Round {
    private final Instant roundStartTime;
    private final Role roundRole;
    private boolean runnersKilled;
    private boolean rangersKilled;
    private boolean healersKilled;
    private boolean fightersKilled;

    @Inject
    public Round(@NonNull Role role) {
        if (role == null) {
            throw new NullPointerException("role is marked non-null but is null");
        } else {
            this.roundRole = role;
            this.roundStartTime = Instant.now().plus(Duration.of(2L, RSTimeUnit.GAME_TICKS));
        }
    }

    public int getTimeToChange() {
        return 30 + (int)Duration.between(Instant.now(), this.roundStartTime).getSeconds() % 30;
    }

    public Role getRoundRole() {
        return this.roundRole;
    }

    public boolean isRunnersKilled() {
        return this.runnersKilled;
    }

    public void setRunnersKilled(boolean runnersKilled) {
        this.runnersKilled = runnersKilled;
    }

    public boolean isRangersKilled() {
        return this.rangersKilled;
    }

    public void setRangersKilled(boolean rangersKilled) {
        this.rangersKilled = rangersKilled;
    }

    public boolean isHealersKilled() {
        return this.healersKilled;
    }

    public void setHealersKilled(boolean healersKilled) {
        this.healersKilled = healersKilled;
    }

    public boolean isFightersKilled() {
        return this.fightersKilled;
    }

    public void setFightersKilled(boolean fightersKilled) {
        this.fightersKilled = fightersKilled;
    }
}
