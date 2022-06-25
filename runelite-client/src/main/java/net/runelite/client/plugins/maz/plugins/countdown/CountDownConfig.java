package net.runelite.client.plugins.maz.plugins.countdown;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("countdown")
public interface CountDownConfig extends Config {
    @ConfigItem(
            keyName = "cdate",
            name = "Count Down Date & Time",
            description = "",
            position = 0
    )
    default String date() {
        return "2022-08-24T06:00:00";
    }

    @ConfigItem(
            position = 1,
            keyName = "cname",
            name = "Count Down of",
            description = ""
    )
    default String cname() {
        return "Raids 3 Count Down: ";
    }
}