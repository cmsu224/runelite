//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import java.awt.Color;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import org.apache.commons.lang3.tuple.Pair;

public class MatomenosDetails {
    @Nonnull
    private final NPC matomenosNpc;
    @Nonnull
    private final String phaseKey0;
    @Nullable
    private final Pair<String, Boolean> identifier0;
    private int healthRatio;
    private int healthScale;
    private int frozenTicks;
    private boolean hasBeenFrozen;

    public MatomenosDetails(@Nonnull Client client, @Nonnull NPC matomenosNpc, @Nonnull String phaseKey) {
        this(matomenosNpc, phaseKey, phaseKey.equals("0") ? null : MatomenosSpawnIdentifier.of(client, matomenosNpc), matomenosNpc.getHealthRatio(), matomenosNpc.getHealthScale());
    }

    private MatomenosDetails(@Nonnull NPC matomenosNpc, @Nonnull String phaseKey, Pair<String, Boolean> identifier, int healthRatio, int healthScale) {
        this.frozenTicks = -1;
        this.matomenosNpc = matomenosNpc;
        this.phaseKey0 = phaseKey;
        this.identifier0 = identifier;
        this.healthRatio = healthRatio;
        this.healthScale = healthScale;
    }

    public void updateHitpoints1() {
        if (this.matomenosNpc.getHealthScale() > 0 && this.matomenosNpc.getHealthRatio() < 100) {
            this.healthRatio = this.matomenosNpc.getHealthRatio();
            this.healthScale = this.matomenosNpc.getHealthScale();
        }

    }

    public void updateHitpoints2() {
        if (this.matomenosNpc.getHealthScale() > 0) {
            this.healthRatio = Math.min(this.healthRatio, this.matomenosNpc.getHealthRatio());
            this.healthScale = this.matomenosNpc.getHealthScale();
        }

    }

    public float calculateHitpoints() {
        return (float)this.healthRatio / (float)this.healthScale * 100.0F;
    }

    public String formatHitpoints(float hitpoints, boolean deci) {
        return String.format(!deci ? "%.0f" : "%.1f", hitpoints);
    }

    public Color calculateHitpointsColor(float hpPercent) {
        hpPercent = Math.max(Math.min(100.0F, hpPercent), 0.0F);
        double rMod = 130.0D * (double)hpPercent / 100.0D;
        double gMod = 235.0D * (double)hpPercent / 100.0D;
        double bMod = 125.0D * (double)hpPercent / 100.0D;
        int r = (int)Math.min(255.0D, 255.0D - rMod);
        int g = Math.min(255, (int)(20.0D + gMod));
        int b = Math.min(255, (int)(0.0D + bMod));
        return new Color(r, g, b);
    }

    public int calculateDistanceTo(@Nullable NPC other) {
        return other == null ? -1 : Math.max(0, this.matomenosNpc.getWorldArea().distanceTo2D(other.getWorldArea()) - 1);
    }

    public Optional<String> getFrozenTicksOptStr() {
        return this.frozenTicks >= 0 ? Optional.of(Integer.toString(this.frozenTicks)) : Optional.empty();
    }

    public void setFrozenTicks(int frozenTicks) {
        if (this.frozenTicks < 0) {
            this.hasBeenFrozen = true;
            this.frozenTicks = frozenTicks;
        }

    }

    public void decrementFrozenTicks() {
        if (this.frozenTicks >= 0) {
            --this.frozenTicks;
        }

    }

    @Nonnull
    public NPC getMatomenosNpc() {
        return this.matomenosNpc;
    }

    @Nonnull
    public String getPhaseKey() {
        return this.phaseKey0;
    }

    public Pair<String, Boolean> getIdentifier() {
        return this.identifier0;
    }

    public int getFrozenTicks() {
        return this.frozenTicks;
    }

    public boolean isHasBeenFrozen() {
        return this.hasBeenFrozen;
    }
}
