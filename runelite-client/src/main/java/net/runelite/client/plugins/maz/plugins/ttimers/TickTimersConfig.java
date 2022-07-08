//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.maz.plugins.ttimers;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("TickTimers")
public interface TickTimersConfig extends Config {

    @ConfigItem(
            position = 0,
            keyName = "prayerWidgetHelper",
            name = "Prayer Widget Helper",
            description = "Shows you which prayer to click and the time until click."
    )
    default boolean showPrayerWidgetHelper() {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "showHitSquares",
            name = "Show Hit Squares",
            description = "Shows you where the melee bosses can hit you from."
    )
    default boolean showHitSquares() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "changeTickColor",
            name = "Change Tick Color",
            description = "If this is enabled, it will change the tick color to white<br> at 1 tick remaining, signaling you to swap."
    )
    default boolean changeTickColor() {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "ignoreNonAttacking",
            name = "Ignore Non-Attacking",
            description = "Ignore monsters that are not attacking you"
    )
    default boolean ignoreNonAttacking() {
        return false;
    }

    @ConfigItem(
            position = 4,
            keyName = "guitarHeroMode",
            name = "Guitar Hero Mode",
            description = "Render \"Guitar Hero\" style prayer helper"
    )
    default boolean guitarHeroMode() {
        return false;
    }

    @ConfigItem(
            position = 0,
            keyName = "gwd",
            name = "God Wars Dungeon",
            description = "Show tick timers for GWD Bosses. This must be enabled before you zone in."
    )
    default boolean gwd() {
        return true;
    }


    @ConfigItem(
            position = 0,
            keyName = "fontStyle",
            name = "Font Style",
            description = "Plain | Bold | Italics"
    )
    default TickTimersConfig.FontStyle fontStyle() {
        return TickTimersConfig.FontStyle.BOLD;
    }

    @Range(
            min = 1,
            max = 40
    )
    @ConfigItem(
            position = 1,
            keyName = "textSize",
            name = "Text Size",
            description = "Text Size for Timers."
    )
    default int textSize() {
        return 32;
    }

    @ConfigItem(
            position = 2,
            keyName = "shadows",
            name = "Shadows",
            description = "Adds Shadows to text."
    )
    default boolean shadows() {
        return false;
    }

    @ConfigItem(
            position = 21,
            keyName = "gameTickOverlay",
            name = "NPCs to Display ticks (ID-style/attackspeed@animationIDs) 1-melee, 2-mage, 3-range",
            description = ""
    )
    default String gameTickOverlay() {
        return "3272-3/6@426@345,\n" +
                "3269-1/5@386,\n" +
                "3271-1/6@395";
    }

    public static enum FontStyle {
        BOLD("Bold", 1),
        ITALIC("Italic", 2),
        PLAIN("Plain", 0);

        private String name;
        private int font;

        public String toString() {
            return this.getName();
        }

        String getName() {
            return this.name;
        }

        int getFont() {
            return this.font;
        }

        private FontStyle(String name, int font) {
            this.name = name;
            this.font = font;
        }
    }
}
