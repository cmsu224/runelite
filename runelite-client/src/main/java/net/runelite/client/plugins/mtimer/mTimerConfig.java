package net.runelite.client.plugins.mtimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("mtimer")
public interface mTimerConfig extends  Config{

    @ConfigItem(
            keyName = "toggleKeybind",
            name = "Toggle Overlay",
            description = "Binds a key (combination) to toggle the overlay.",
            position = 0
    )
    default Keybind toggleKeybind()
    {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            position = 1,
            keyName = "getStartMsgs",
            name = "Timer Start Messages (Ex. 'You enter the Theatre of Blood (Hard Mode), The nightmare has woken...')",
            description = "List of messages to start the timer"
    )
    default String getStartMsgs()
    {
        return "";
    }

    @ConfigItem(
            position = 2,
            keyName = "getStopMsgs",
            name = "Timer Stop Messages (Ex. 'Theatre of Blood total completion time:')",
            description = "List of messages to stop the timer"
    )
    default String getStopMsgs()
    {
        return "";
    }

}
