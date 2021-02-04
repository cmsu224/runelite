//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.barbarianassault;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("barbarianAssault")
public interface BarbarianAssaultConfig extends Config {
    @ConfigItem(
            keyName = "showTimer",
            name = "Show call change timer",
            description = "Show time to next call change"
    )
    default boolean showTimer() {
        return true;
    }

    @ConfigItem(
            keyName = "showHealerBars",
            name = "Show health bars for teammates when healer",
            description = "Displays team health for healer"
    )
    default boolean showHealerBars() {
        return true;
    }

    @ConfigItem(
            keyName = "waveTimes",
            name = "Show wave and game duration",
            description = "Displays wave and game duration"
    )
    default boolean waveTimes() {
        return true;
    }
}
