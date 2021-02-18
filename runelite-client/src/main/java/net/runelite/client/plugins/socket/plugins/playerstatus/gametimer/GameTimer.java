//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.plugins.playerstatus.gametimer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public enum GameTimer {
    VENGEANCE(564, GameTimerImageType.SPRITE, "Vengeance", 30L, ChronoUnit.SECONDS),
    OVERLOAD(11730, GameTimerImageType.ITEM, "Overload", 5L, ChronoUnit.MINUTES, true),
    OVERLOAD_RAID(20996, GameTimerImageType.ITEM, "Overload", 5L, ChronoUnit.MINUTES, true),
    PRAYER_ENHANCE(20964, GameTimerImageType.ITEM, "Prayer enhance", 290L, ChronoUnit.SECONDS, true),
    STAMINA(12625, GameTimerImageType.ITEM, "Stamina", 2L, ChronoUnit.MINUTES, true),
    IMBUED_HEART(20724, GameTimerImageType.ITEM, "Imbued heart", 1316, 420L, ChronoUnit.SECONDS);

    private final Duration duration;
    private final Integer graphicId;
    private final String description;
    private final boolean removedOnDeath;
    private final Duration initialDelay;
    private final int imageId;
    private final GameTimerImageType imageType;

    private GameTimer(int imageId, GameTimerImageType idType, String description, Integer graphicId, long time, ChronoUnit unit, long delay, boolean removedOnDeath) {
        this.description = description;
        this.graphicId = graphicId;
        this.duration = Duration.of(time, unit);
        this.imageId = imageId;
        this.imageType = idType;
        this.removedOnDeath = removedOnDeath;
        this.initialDelay = Duration.of(delay, unit);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, Integer graphicId, long time, ChronoUnit unit, boolean removedOnDeath) {
        this(imageId, idType, description, graphicId, time, unit, 0L, removedOnDeath);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, long time, ChronoUnit unit, boolean removeOnDeath) {
        this(imageId, idType, description, (Integer)null, time, unit, removeOnDeath);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, long time, ChronoUnit unit) {
        this(imageId, idType, description, (Integer)null, time, unit, false);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, Integer graphicId, long time, ChronoUnit unit) {
        this(imageId, idType, description, graphicId, time, unit, false);
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Integer getGraphicId() {
        return this.graphicId;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isRemovedOnDeath() {
        return this.removedOnDeath;
    }

    public Duration getInitialDelay() {
        return this.initialDelay;
    }

    public int getImageId() {
        return this.imageId;
    }

    public GameTimerImageType getImageType() {
        return this.imageType;
    }
}
