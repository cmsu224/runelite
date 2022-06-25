package net.runelite.client.plugins.maz.plugins.customtimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("customtimer")
public interface customTimerConfig extends  Config{

    @ConfigItem(
            keyName = "reminderTime",
            name = "Reminder Time",
            description = "Remind every X minutes (Stars when logged in/Stops when logged out)",
            position = 0
    )
    default int reminderTime()
    {
        return 120;
    }

    @ConfigItem(
            position = 1,
            keyName = "reminder",
            name = "Reminder Message",
            description = ""
    )
    default String reminder()
    {
        return "Go get your blood checked you filthy animal.";
    }

    @ConfigItem(
            position = 2,
            keyName = "overlay",
            name = "Show Overlay",
            description = ""
    )
    default boolean overlay()
    {
        return false;
    }
}
